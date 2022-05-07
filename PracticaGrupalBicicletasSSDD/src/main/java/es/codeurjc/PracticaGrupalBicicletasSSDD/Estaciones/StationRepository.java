package es.codeurjc.PracticaGrupalBicicletasSSDD.Estaciones;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StationRepository extends JpaRepository<Station, Long> {

}
