package com.servicios.orquestador.controllers;


public class Pago {

	/**
	 * Saldo restante después del pago realizado
	 */
	private double saldo;
	
	/**
	 * Deuda restante después del pago realizado
	 */
	private double deuda;
	
	/**
	 * Respuesta del pago solicitado (Exitoso, Rechazado)
	 */
	private String respuesta;
	
	
	/**
	 * Constructor sin parámetros
	 */
	public Pago() {
		saldo = 0;
		deuda = 0;
		respuesta = "No recibido";
	}
	
	/**
	 * Constructor con parámetros
	 */
	public Pago(double saldo, double deuda, String respuesta) {
		this.saldo = saldo;
		this.deuda = deuda;
		this.respuesta = respuesta;
	}
		
	public String getEstado() {
		return respuesta;
	}

	public void setEstado(String estado) {
		this.respuesta = estado;
	}
	
	public double getSaldo() {
		return saldo;
	}
	
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	public double getDeuda() {
		return deuda;
	}
	
	public void setDeuda(double deuda) {
		this.deuda = deuda;
	}
}
