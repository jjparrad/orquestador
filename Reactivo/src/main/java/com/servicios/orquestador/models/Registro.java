package com.servicios.orquestador.models;

public class Registro {

	private long id;
	private String numCuenta;
	private String numTarjeta;
	private Double monto;
	private String fecha;
	
	
	public Registro(long id, String numCuenta, String numTarjeta, Double monto, String fecha) {
		this.setId(id);
		this.numCuenta = numCuenta;
		this.numTarjeta = numTarjeta;
		this.monto = monto;
		this.fecha = fecha;
	}
	
	public String getCuenta() {
		return numCuenta;
	}
	public void setCuenta(String cuenta) {
		this.numCuenta = cuenta;
	}
	public String getTarjeta() {
		return numTarjeta;
	}
	public void setTarjeta(String tarjeta) {
		this.numTarjeta = tarjeta;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
}