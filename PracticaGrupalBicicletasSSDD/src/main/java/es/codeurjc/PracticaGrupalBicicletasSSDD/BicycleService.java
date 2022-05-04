package es.codeurjc.PracticaGrupalBicicletasSSDD;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BicycleService {
	
	@Autowired
	private BicycleRepository bicycleRepo;
	private ConcurrentMap<Long, Bicycle> bicycles = new ConcurrentHashMap<>();
	private AtomicLong nextId = new AtomicLong(1);
	
	public BicycleService() {
		save (new Bicycle("abc123def456aa11", "ROCKRIDER RACE 900 29"));
	
		}
	
	public Collection<Bicycle> findAll(){
		return bicycles.values();
	}
	
	public Bicycle findById(long id) {
		return bicycles.get(id);
	}
	
	public void save(Bicycle b) {
		if(b.getId() == null || b.getId() == 0) {
		long id = nextId.getAndIncrement();
		b.setId(id);
		}
		this.bicycles.put(b.getId(), b);
		}
		public void deleteById(long id) {
		this.bicycles.remove(id);
		}

}
