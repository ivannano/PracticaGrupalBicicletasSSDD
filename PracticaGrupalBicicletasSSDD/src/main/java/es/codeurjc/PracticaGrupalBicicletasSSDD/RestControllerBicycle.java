package es.codeurjc.PracticaGrupalBicicletasSSDD;

import java.util.Collection;

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
		Bicycle bici = bicycleService.findById(idBicycle);
		
		if( bici != null) {
			return ResponseEntity.ok(bici);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	
	
}
