package es.codeurjc.PracticaGrupalBicicletasSSDD;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BicycleService {
	
	@Autowired
	private BicycleRepository bicycleRepo;

	
	public Collection<Bicycle> findAll(){
		return bicycleRepo.findAll();
	}
	
	public Optional<Bicycle> findById(long id) {
		return bicycleRepo.findById(id);
	}
	
	public void save(Bicycle b) {
		bicycleRepo.save(b);
	}

}
