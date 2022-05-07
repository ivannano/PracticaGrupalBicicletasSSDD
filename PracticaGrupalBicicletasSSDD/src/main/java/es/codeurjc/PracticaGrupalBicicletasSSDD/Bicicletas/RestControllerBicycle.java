package es.codeurjc.PracticaGrupalBicicletasSSDD.Bicicletas;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestControllerBicycle {

	@Autowired
	private BicycleService bicycleService;
	
	@GetMapping("/bicycles")
	public Collection<Bicycle> getBicycles(){
		return bicycleService.findAll();
	}
	
	@GetMapping("/bicycles/{idBicycle}")
	public ResponseEntity<Bicycle> getBicycle(@PathVariable long idBicycle){
		Optional<Bicycle> bici = bicycleService.findById(idBicycle);
		if (bici.isPresent()) {
			return ResponseEntity.ok(bici.get());
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	
	
	
}
