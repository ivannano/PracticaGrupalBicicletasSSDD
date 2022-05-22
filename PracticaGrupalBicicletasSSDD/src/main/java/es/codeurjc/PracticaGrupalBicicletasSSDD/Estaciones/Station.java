package es.codeurjc.PracticaGrupalBicicletasSSDD.Estaciones;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.codeurjc.PracticaGrupalBicicletasSSDD.Bicicletas.Bicycle;


@Entity
public class Station {
	//ATRIBUTOS
		@Id 
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long Id=null;
		@Column(unique = true)
		private String numSerie;
		private String coordenadas;
		private String fechaInstalacion;
		private EstadoStation estado;
		private int capacidad;
		@OneToMany(mappedBy="estacionAsig")
		@JsonIgnore
		private List<Bicycle> bicicletas;
		
		public enum EstadoStation{
			ACTIVO,INACTIVO
		}
		
		public Station () {
		
		}
		
		public Station(String numSerie, String coordenadas, int capacidad) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			this.capacidad = capacidad;
			this.coordenadas = coordenadas;
			this.numSerie = numSerie;
			this.fechaInstalacion = dtf.format(LocalDateTime.now());
			this.estado = EstadoStation.ACTIVO;
		}
		
		public String getNumSerie() {
			return numSerie;
		}

		public void setNumSerie(String numSerie) {
			this.numSerie = numSerie;
		}

		public String getCoordenadas() {
			return coordenadas;
		}

		public void setCoordenadas(String coordenadas) {
			this.coordenadas = coordenadas;
		}

		public String getFechaInstalacion() {
			return fechaInstalacion;
		}

		public EstadoStation getEstado() {
			return estado;
		}
		
		public void setEstadoActivo() {
			this.estado = EstadoStation.ACTIVO;
		}
		
		public void setEstadoInactivo() {
			this.estado = EstadoStation.INACTIVO;
		}
		
		public int getCapacidad() {
			return capacidad;
		}

		public void setCapacidad(int capacidad) {
			this.capacidad = capacidad;
		}

		public Long getId() {
			return Id;
		}
		public void setId(long id) {
			this.Id = id; 
		}
		
		public void addBicycle(Bicycle b) {
			if (this.bicicletas.size()<this.getCapacidad()) {
				this.bicicletas.add(b);
			}
		}
		
		public void remove(Bicycle b) {
			if (this.bicicletas.contains(b)) {
				this.bicicletas.remove(b);
			}
		}
		public List<Bicycle> getBicicletas(){
			return this.bicicletas;
		}
		public void NullBicicletas(){
			this.bicicletas = null;
		}


		@Override
		public String toString() {
			return "Estacion [numSerie=" + numSerie + ", coordenadas=" + coordenadas + ", fechaInstalacion="
					+ fechaInstalacion + ", estado=" + estado + ", capacidad=" + capacidad + "]";
		}
}
