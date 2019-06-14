package com.servicios.orquestador.controllers;

/**
 * Clase encargada de manejar el formato que entrega el API de Cuentas de ahorro
 * @author jjparra
 *
 */
public class ResponseCuenta {

	/**
	 * Nombres de variables como las solicita el API
	 */
	private String numCuenta;
	private double cantidadDebitada;
	private double saldo;
	

	public ResponseCuenta() {}
	
	
	public ResponseCuenta(String numCuenta, double cantidadDebitada, double saldo) {
		super();
		this.numCuenta = numCuenta;
		this.cantidadDebitada = cantidadDebitada;
		this.saldo = saldo;
	}


	public String getNumCuenta() {
		return numCuenta;
	}


	public void setNumCuenta(String numCuenta) {
		this.numCuenta = numCuenta;
	}


	public double getCantidadDebitada() {
		return cantidadDebitada;
	}


	public void setCantidadDebitada(double cantidadDebitada) {
		this.cantidadDebitada = cantidadDebitada;
	}


	public double getSaldo() {
		return saldo;
	}


	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}


	
}