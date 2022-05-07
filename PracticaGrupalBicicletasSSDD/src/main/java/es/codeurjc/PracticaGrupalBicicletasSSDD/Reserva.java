package es.codeurjc.PracticaGrupalBicicletasSSDD;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Reserva {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id_reserva;
	private long id_usuario;
	private long id_bicicleta;
	private long id_estacion;
	
	public Reserva() {
		
	}
	
	public Reserva(long id_usuario,long id_bicicleta,long id_estacion) {
		this.id_bicicleta = id_bicicleta;
		this.id_estacion = id_estacion;
		this.id_usuario = id_usuario;
	}
	
	public long getId_reserva() {
		return id_reserva;
	}
	public void setId_reserva(long id_reserva) {
		this.id_reserva = id_reserva;
	}
	public long getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(long id_usuario) {
		this.id_usuario = id_usuario;
	}
	public long getId_bicicleta() {
		return id_bicicleta;
	}
	public void setId_bicicleta(long id_bicicleta) {
		this.id_bicicleta = id_bicicleta;
	}
	public long getId_estacion() {
		return id_estacion;
	}
	public void setId_estacion(long id_estacion) {
		this.id_estacion = id_estacion;
	}
	
}
