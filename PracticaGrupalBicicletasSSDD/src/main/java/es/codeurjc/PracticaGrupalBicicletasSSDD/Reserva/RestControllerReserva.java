package es.codeurjc.PracticaGrupalBicicletasSSDD.Reserva;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

import org.slf4j.*;
import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.codeurjc.PracticaGrupalBicicletasSSDD.Bicicletas.Bicycle;
import es.codeurjc.PracticaGrupalBicicletasSSDD.Bicicletas.BicycleService;
import es.codeurjc.PracticaGrupalBicicletasSSDD.Bicicletas.Bicycle.Estado;
import es.codeurjc.PracticaGrupalBicicletasSSDD.Estaciones.Station;
import es.codeurjc.PracticaGrupalBicicletasSSDD.Estaciones.Station.EstadoStation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import es.codeurjc.PracticaGrupalBicicletasSSDD.Estaciones.StationService;

@RestController
public class RestControllerReserva {
	private Logger log = LoggerFactory.getLogger(RestControllerReserva.class);
	String URL_API_USUARIOS = "http://localhost:8081/users/";
	
	@Autowired
	private ReservaService reservaService;
	@Autowired
	private StationService stationService;
	@Autowired
	private BicycleService bicycleService;
	
//*************************************************************************************************
	@Operation(summary = "Get a list of bookings")
	@GetMapping("/reserva")
	public Collection<Reserva> getBicycles(){
		return reservaService.findAll();
	}
	
//*************************************************************************************************	
	@Operation(summary = "Get a booking by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the booking", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Booking not found", content = @Content) })
	
	@GetMapping("/reserva/{idBicycle}")
	public ResponseEntity<Reserva> getBicycle(@PathVariable long idBicycle){
		Optional<Reserva> r = reservaService.findById(idBicycle);
		if (r.isPresent()) {
			log.info("Reserva encontrada");
			return ResponseEntity.ok(r.get());
		}else {
			log.error("Reserva no encontrada");
			return ResponseEntity.notFound().build();
		}
	}
	
//*************************************************************************************************
	
	@Operation(summary = "Create a new booking")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Booking created succesfully", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", description = "Station and bicycle don´t exist or don´t meet the conditions", content = @Content) })
	
	@PostMapping("/reserva")
	
	public ResponseEntity<Reserva> newReserve(@RequestBody Reserva newReserve) {
		
		long id_estacion = newReserve.getId_estacion();			//Id de la estacion
		long id_bicicleta = newReserve.getId_bicicleta();		//Id de la bicicleta
		long id_usuario = newReserve.getId_usuario();	 		//Id del usuario
		
		RestTemplate restTemplate = new RestTemplate();			//Objeto RestTemplate para conectarnos a la API
	
			Optional<Station> s = stationService.findById(id_estacion);			//Obtenemos la estacion con el ID
			Optional<Bicycle> b = bicycleService.findById(id_bicicleta);		//Obtenemos la bicicleta con el ID
			
			if (s.isPresent() && b.isPresent()) { 								//Comprobamos que ambos objetos estan presentes
				if (s.get().getEstado().equals(EstadoStation.ACTIVO) && 		//Comprobamos los estados de la estacion y la estacion a la que se asigna la bicicleta		
							b.get().getEstado().equals(Estado.EN_BASE) && b.get().getEstacionAsig().getId() == id_estacion) {
					
					URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")	
							.buildAndExpand(newReserve.getId_reserva()).toUri();
					
					String url = "http://localhost:8081/users/bookings/" + id_usuario;
					ResponseEntity<ObjectNode> respuesta = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(new HttpHeaders()), ObjectNode.class);
					if(respuesta.getStatusCode().equals(HttpStatus.OK)) {
						reservaService.save(newReserve);							//Guardamos la reserva en la base de datos
						b.get().setEstado(Estado.RESERVADA);
						bicycleService.save(b.get());								//Ponemos la bicicleta como reservada
						s.get().remove(b.get());									//Quitamos la bicicleta de su estacion
						stationService.save(s.get());
						log.info("Reserva creada correctamente");
						return ResponseEntity.created(location).body(newReserve);
					}
					else {
						return ResponseEntity.badRequest().build();
					}
					
				}
				else {
					log.error("Estación inactiva, bicicleta no está en base o la bicicleta no pertenece a la estación");
					return ResponseEntity.notFound().build();
				}
				
			}else
				log.error("La estacion o la bicicleta no encontradas");
				return ResponseEntity.notFound().build();
	
	}
	
//*************************************************************************************************
	
	@Operation(summary = "Delete an existing booking by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the user", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", description = "Booking not found, or station and bicycle don´t meet the conditions", content = @Content) })
	@DeleteMapping("/reserva/{id_reserva}")
	
	public ResponseEntity<Reserva> deleteReserve(@PathVariable Long id_reserva){		
		Optional<Reserva> reserva = reservaService.findById(id_reserva);
		if(reserva.isPresent()) {
			long id_estacion = reserva.get().getId_estacion();			//Id de la estacion
			long id_bicicleta = reserva.get().getId_bicicleta();		//Id de la bicicleta
			long id_usuario = reserva.get().getId_usuario();	 		//Id del usuario
			
			String url = "http://localhost:8081/users/devolucion_bookings/" + id_usuario;
			
			RestTemplate restTemplate = new RestTemplate();			//Objeto RestTemplate para conectarnos a la API
			
			Optional<Station> s = stationService.findById(id_estacion);			//Obtenemos la estacion con el ID
			Optional<Bicycle> b = bicycleService.findById(id_bicicleta);		//Obtenemos la bicicleta con el ID
			
			if (s.isPresent() && b.isPresent()) { 								//Comprobamos que ambos objetos estan presentes
				if (s.get().getEstado().equals(EstadoStation.ACTIVO) &&
                        b.get().getEstado().equals(Estado.RESERVADA) && b.get().getEstacionAsig().getId() == id_estacion 
                                    && s.get().getBicicletas().size()<s.get().getCapacidad()) {
					
					ResponseEntity<ObjectNode> respuesta = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(new HttpHeaders()), ObjectNode.class);
					if(respuesta.getStatusCode().equals(HttpStatus.OK)) {
						reservaService.deleteById(reserva.get().getId_reserva());	//Guardamos la reserva en la base de datos
						b.get().setEstado(Estado.EN_BASE);
						bicycleService.save(b.get());								//Ponemos la bicicleta como reservada
						s.get().addBicycle(b.get());								//Quitamos la bicicleta de su estacion
						stationService.save(s.get());
						log.info("Reserva finalizada correctamente");
						return ResponseEntity.ok().build();
					}
					else {
						return ResponseEntity.badRequest().build();
					}
					
				}
				else {
					log.error("Estación inactiva, bicicleta no reservada o estación llena");
					return ResponseEntity.notFound().build();
				}
				
			}else
				log.error("La estacion o la bicicleta no encontradas");
				return ResponseEntity.notFound().build();
		}
		else {
			log.error("Reserva no encontrada");
			return ResponseEntity.notFound().build();
		}
						
	}
		
}
