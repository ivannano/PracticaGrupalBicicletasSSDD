package es.codeurjc.PracticaGrupalBicicletasSSDD;

@Rest
public class RestControllerBicycle {

	@Autowired
	BicycleRepository bicycleRepository;
	
	@GetMapping("/Bicycles")
	public Collection<Bicycle> getBicycles(){
		reuturn bicycleRepository.findAll();
	}
	
	@GetMapping("/Bicycles/{idBicycle}")
	public ResponseEntity<Bicycle> getBicycle(@PathVariable long idBicycle){
		Bicycle bici = bicycleRepository.findById(idBicycle);
		if( bici != null) {
			return ResponseEntity.ok(bici);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
}
