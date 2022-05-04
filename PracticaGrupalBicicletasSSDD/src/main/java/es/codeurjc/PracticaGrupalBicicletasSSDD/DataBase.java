package es.codeurjc.PracticaGrupalBicicletasSSDD;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;




@Component
@Profile("local")
public class DataBase {
	
	@Autowired
	private BicycleRepository bicycleRepo;
	
	@PostConstruct
	public void init() {

		
	
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
		
	}
}
