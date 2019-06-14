package com.servicios.orquestador.controllers;

public class Registro {

	private String numCuenta;
	private String numTarjeta;
	private Double monto;
	private String fecha;
	
	
	public Registro(String numCuenta, String numTarjeta, Double monto, String fecha) {
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
	
	
}
