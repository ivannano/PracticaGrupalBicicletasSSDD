package es.codeurjc.PracticaGrupalBicicletasSSDD.Reserva;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ReservaService {
	
	@Autowired
	private ReservaRepository repo;
	
	public Collection<Reserva> findAll(){
		return repo.findAll();
	}
	
	public Optional<Reserva> findById(long id) {
		return repo.findById(id);
	}
	
	public void save(Reserva b) {
		repo.save(b);
	}
	
	public void deleteById(Long id) {
		repo.deleteById(id);
	}
}
