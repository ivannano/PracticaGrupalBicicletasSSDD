package es.codeurjc.PracticaGrupalBicicletasSSDD.Reserva;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import es.codeurjc.PracticaGrupalBicicletasSSDD.Estaciones.StationService;

@RestController
public class RestControllerReserva {
	
	String URL_API_USUARIOS = "http://localhost:8081/users/";
	
	@Autowired
	private ReservaService reservaService;
	@Autowired
	private StationService stationService;
	@Autowired
	private BicycleService bicycleService;
	
	@GetMapping("/reserva")
	public Collection<Reserva> getBicycles(){
		return reservaService.findAll();
	}
	
	@GetMapping("/reserva/{idBicycle}")
	public ResponseEntity<Reserva> getBicycle(@PathVariable long idBicycle){
		Optional<Reserva> r = reservaService.findById(idBicycle);
		if (r.isPresent()) {
			return ResponseEntity.ok(r.get());
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/reserva")
	public ResponseEntity<Reserva> newReserve(@RequestBody Reserva newReserve){
		
		long id_estacion = newReserve.getId_estacion();
		long id_bicicleta = newReserve.getId_bicicleta();
		long id_usuario = newReserve.getId_usuario();
		
		
		
		/*String url = URL_API_USUARIOS + id_usuario; //http://localhost:8081/users/id_usuario
		
		RestTemplate restTemplate = new RestTemplate();
		
		ObjectNode data = restTemplate.getForObject(url,ObjectNode.class);
		double saldo = data.get("saldo").asDouble();
		JSONObject jObject = new JSONObject();
		
		jObject.put("id", data.get("id"));
		jObject.put("login", data.get("login"));
		jObject.put("password", data.get("password"));
		jObject.put("name", data.get("name"));
		jObject.put("date", data.get("date"));
		jObject.put("estado", data.get("estado"));
		jObject.put("saldo", saldo-10);*/
		
		
	
		
		Optional<Station> s = stationService.findById(id_estacion);
		Optional<Bicycle> b = bicycleService.findById(id_bicicleta);
		
		if (s.isPresent() && b.isPresent()) {
			if (s.get().getEstado().equals(EstadoStation.ACTIVO) && 
												b.get().getEstado().equals(Estado.EN_BASE) && b.get().getEstacionAsig().getId() == id_estacion) {
				reservaService.save(newReserve);
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(newReserve.getId_reserva()).toUri();
				//restTemplate.put(url, jObject);
				return ResponseEntity.created(location).body(newReserve);
			}else
				return ResponseEntity.notFound().build();
			
		}else
			return ResponseEntity.notFound().build();

	}
}
