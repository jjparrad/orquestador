package com.servicios.orquestador.controllers;

/**
 * Clase encargada de manejar el formato que recibe el API de Tarjetas de Crédito
 * @author jjparra
 *
 */
public class OrdenTarjeta {

	/**
	 * Nombres de variables como las solicita el API
	 */
	private String numTarjeta;
	private double monto;
	
	
	public OrdenTarjeta() {}
	
	public OrdenTarjeta(String numTarjeta, Double monto) {
		this.numTarjeta = numTarjeta;
		this.monto = monto;
	}
	
	public String getTarjeta() {
		return numTarjeta;
	}
	public void setTarjeta(String tarjeta) {
		this.numTarjeta = tarjeta;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
}
