package es.codeurjc.PracticaGrupalBicicletasSSDD.Bicicletas;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class RestControllerBicycle {

	private Logger log = LoggerFactory.getLogger(RestControllerBicycle.class);
	
	@Autowired
	private BicycleService bicycleService;
	
	
//*************************************************************************************************	
	@Operation(summary = "Get a list of bicycles")
	@GetMapping("/bicycles")
	public Collection<Bicycle> getBicycles(){
		return bicycleService.findAll(); 
	}
	
//*************************************************************************************************
	@Operation(summary = "Get a list of bookings")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the bicycle", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", description = "Bicycle not found", content = @Content) })
	
	@GetMapping("/bicycles/{idBicycle}")
	public ResponseEntity<Bicycle> getBicycle(@PathVariable long idBicycle){
		Optional<Bicycle> bici = bicycleService.findById(idBicycle);
		if (bici.isPresent()) {
			log.info("Bicicleta encontrada");
			return ResponseEntity.ok(bici.get());
		}
		else {
			log.error("Bicicleta no encontrada");
			return ResponseEntity.notFound().build();
		}
	}
		
}
