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
			return ResponseEntity.ok(r.get());
		}else {
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
	public ResponseEntity<Reserva> newReserve(@RequestBody Reserva newReserve){
		
		long id_estacion = newReserve.getId_estacion();			//Id de la estacion
		long id_bicicleta = newReserve.getId_bicicleta();		//Id de la bicicleta
		long id_usuario = newReserve.getId_usuario();	 		//Id del usuario
		
		String url = URL_API_USUARIOS + id_usuario; 			//http://localhost:8081/users/id_usuario
		
		RestTemplate restTemplate = new RestTemplate();			//Objeto RestTemplate para conectarnos a la API
		
		/*HttpHeaders headers = new HttpHeaders();					
	    headers.setContentType(MediaType.APPLICATION_JSON);				 
	    HttpEntity<Object> entity = new HttpEntity<Object>(headers);
	    
	    ResponseEntity<String> out = restTemplate.exchange(url, HttpMethod.GET, entity, String.class); //Respuesta de la llamada a la API
	    */
	    /*if(!out.getStatusCode().equals(ResponseEntity.ok())) {  	//Comprobamos que el codigo de estado es el esperado
	    	return ResponseEntity.notFound().build();				//Mandamos la respuesta de NO ENCONTRADO
	    }else {*/
			//User user = restTemplate.getForObject(url,User.class);
			ObjectNode user = restTemplate.getForObject(url,ObjectNode.class); 	//Obtenemos el objecto de la peticion
	
			Optional<Station> s = stationService.findById(id_estacion);			//Obtenemos la estacion con el ID
			Optional<Bicycle> b = bicycleService.findById(id_bicicleta);		//Obtenemos la bicicleta con el ID
			
			if (s.isPresent() && b.isPresent()) { 								//Comprobamos que ambos objetos estan presentes
				if (s.get().getEstado().equals(EstadoStation.ACTIVO) && 		//Comprobamos los estados de la estacion y la estacion a la que se asigna la bicicleta		
							b.get().getEstado().equals(Estado.EN_BASE) && b.get().getEstacionAsig().getId() == id_estacion) {
					
					reservaService.save(newReserve);							//Guardamos la reserva en la base de datos
					URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")	
							.buildAndExpand(newReserve.getId_reserva()).toUri();
					//user.setSaldo(user.getSaldo()-10);
					//restTemplate.put(url, user);	
					double nuevoSaldo = user.get("saldo").asDouble()-10;		//Le restamos lo que cuesta la reserva de la bicicleta
					user.put("saldo", nuevoSaldo);								//Modificamos el campo "saldo" con el nuevo saldo
					restTemplate.put(url, user);								//Modificamos el objeto de la API extenrna con el nuevo objeto modificado
					b.get().setEstado(Estado.RESERVADA);
					bicycleService.save(b.get());								//Ponemos la bicicleta como reservada
					
					s.get().remove(b.get());									//Quitamos la bicicleta de su estacion
					stationService.save(s.get());
					
					return ResponseEntity.created(location).body(newReserve);
				}else
					return ResponseEntity.notFound().build();
				
			}else
				return ResponseEntity.notFound().build();
	
		//}
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
		if (reserva.isPresent()) {
			long id_estacion = reserva.get().getId_estacion();		//Id de la estacion
			long id_bicicleta = reserva.get().getId_bicicleta();		//Id de la bicicleta
			long id_usuario = reserva.get().getId_usuario();	 		//Id del usuario
			String url = URL_API_USUARIOS + id_usuario; 			//http://localhost:8081/users/id_usuario
			
			RestTemplate restTemplate = new RestTemplate();			//Objeto RestTemplate para conectarnos a la API
			
			ObjectNode user = restTemplate.getForObject(url,ObjectNode.class); 	//Obtenemos el objecto de la peticion
			
			Optional<Station> s = stationService.findById(id_estacion);			//Obtenemos la estacion con el ID
			Optional<Bicycle> b = bicycleService.findById(id_bicicleta);		//Obtenemos la bicicleta con el ID
				
			if (s.isPresent() && b.isPresent()) { 
				if (s.get().getEstado().equals(EstadoStation.ACTIVO) && 																
						b.get().getEstado().equals(Estado.RESERVADA) && b.get().getEstacionAsig().getId() == id_estacion 
									&& s.get().getBicicletas().size()<s.get().getCapacidad()) { 

					reservaService.deleteById(reserva.get().getId_reserva()); 

					double nuevoSaldo = user.get("saldo").asDouble() + 2; 
																			
					user.put("saldo", nuevoSaldo); 
					restTemplate.put(url, user); 
					b.get().setEstado(Estado.EN_BASE);
					bicycleService.save(b.get()); 

					s.get().addBicycle(b.get());
					stationService.save(s.get());

					//return ResponseEntity.created(location).body(newReserve);
					return ResponseEntity.ok().build();
					
				} else
					return ResponseEntity.notFound().build();

			} else
				return ResponseEntity.notFound().build();
		}else {
			return ResponseEntity.notFound().build();
		}
				
	}
		
}
