package es.codeurjc.PracticaGrupalBicicletasSSDD;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
@RestController
public class RestControllerReserva {
	@Autowired
	private ReservaService reservaService;
	
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
		
		reservaService.save(newReserve);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newReserve.getId_reserva()).toUri();
		
		return ResponseEntity.created(location).body(newReserve);
	}
}
