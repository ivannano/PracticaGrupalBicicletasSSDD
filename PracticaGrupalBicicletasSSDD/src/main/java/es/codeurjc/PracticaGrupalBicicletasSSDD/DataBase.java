package es.codeurjc.PracticaGrupalBicicletasSSDD;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import es.codeurjc.PracticaGrupalBicicletasSSDD.Bicicletas.Bicycle;
import es.codeurjc.PracticaGrupalBicicletasSSDD.Bicicletas.BicycleRepository;
import es.codeurjc.PracticaGrupalBicicletasSSDD.Bicicletas.Bicycle.Estado;
import es.codeurjc.PracticaGrupalBicicletasSSDD.Estaciones.Station;
import es.codeurjc.PracticaGrupalBicicletasSSDD.Estaciones.StationRepository;
import es.codeurjc.PracticaGrupalBicicletasSSDD.Reserva.Reserva;
import es.codeurjc.PracticaGrupalBicicletasSSDD.Reserva.ReservaRepository;







@Component
@Profile("local")
public class DataBase {
	
	@Autowired
	private BicycleRepository bicycleRepo;
	@Autowired
	private StationRepository stationRepo;
	@Autowired
	private ReservaRepository	reservaRepo;
	
	
	
	@PostConstruct
	public void init() {

		
	
		//Estaciones
		
		
		
		Station s1 = stationRepo.save(new Station("124","52° 31' 28'' N; 13° 24' 38'' E",5));
		Station s2 = stationRepo.save(new Station("300","48° 51' 12'' N; 02° 20' 56'' E",10));
			
		//Bicicletas
		Bicycle b1 = bicycleRepo.save(new Bicycle("abc123def456aa11", "ROCKRIDER RACE 900 29"));
		Bicycle b2 = bicycleRepo.save(new Bicycle("abc123def456bb22", "ROCKRIDER RACE 900 20"));
		Bicycle b3 = bicycleRepo.save(new Bicycle("abc123def456cc33", "ROCKRIDER RACE 900 18"));
		Bicycle b4 = bicycleRepo.save(new Bicycle("abc123def456aa44", "ROCKRIDER RACE 900 32"));
		Bicycle b5 = bicycleRepo.save(new Bicycle("abc123def456aa55", "ROCKRIDER RACE 900 45"));
		Bicycle b6 = bicycleRepo.save(new Bicycle("abc123def456aa66", "ROCKRIDER RACE 100 00"));
		Bicycle b7 = bicycleRepo.save(new Bicycle("abc123def456aa77", "ROCKRIDER RACE 200 11"));
		Bicycle b8 = bicycleRepo.save(new Bicycle("abc123def456aa88", "ROCKRIDER RACE 300 22"));
		Bicycle b9 = bicycleRepo.save(new Bicycle("abc123def456aa99", "ROCKRIDER RACE 400 33"));
		
		b1.setEstado(Estado.EN_BASE);
		b2.setEstado(Estado.EN_BASE);
		b3.setEstado(Estado.EN_BASE);
		b4.setEstado(Estado.EN_BASE);
		
		bicycleRepo.save(b1.setEstacionAsig(s1));
		bicycleRepo.save(b2.setEstacionAsig(s1));
		bicycleRepo.save(b3.setEstacionAsig(s1));
		bicycleRepo.save(b4.setEstacionAsig(s1));
		
		b5.setEstado(Estado.EN_BASE);
		b6.setEstado(Estado.EN_BASE);
		b7.setEstado(Estado.EN_BASE);
		
		
		bicycleRepo.save(b5.setEstacionAsig(s2));
		bicycleRepo.save(b6.setEstacionAsig(s2));
		bicycleRepo.save(b7.setEstacionAsig(s2));
		
		long id1 = 1;
		long id2 = 2;
		long id3 = 1;
		Reserva r1 = new Reserva(id1,id2,id3);
		reservaRepo.save(r1);
	}
}
