package com.servicios.orquestador.models;

public class OrdenPago {

	
	private String numCuenta;
	private String numTarjeta;
	private Double monto;
	
	
	public OrdenPago(String numCuenta, String numTarjeta, Double monto) {
		this.numCuenta = numCuenta;
		this.numTarjeta = numTarjeta;
		this.monto = monto;
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
}
