package com.servicios.orquestador.controllers;

import java.util.Date;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class PagosController {
	
	
	private String urlCuentas = "http://localhost:8082/cuentaDeDepositos/";
	private String urlTarjetas = "http://localhost:8081/tarjetaCredito/";
	private String urlRegistros = "http://localhost:8083/transaccion/";
	
	private boolean existeDeuda;
	
	
	@PutMapping("/pago")
	public Pago pagar(@RequestBody OrdenPago ordenPago) {
		double saldo;
		double deuda;
		
		String cuenta = ordenPago.getCuenta();
		String tarjeta = ordenPago.getTarjeta();
		double monto = ordenPago.getMonto();
		

		saldo = llamarCuenta(cuenta, monto);
		if (saldo < 0){
			deuda = getDeuda(tarjeta);
			return new Pago(-saldo, deuda, "Rechazado: Fondos insuficientes");
		} 
		
		
		deuda = llamarTarjeta(tarjeta, monto);
		if (deuda < 0 || this.existeDeuda == false) {
			saldo = devolverFondos(ordenPago.getCuenta(), ordenPago.getMonto());
			return new Pago(saldo, -deuda, "Rechazado: La tarjeta indicada no posee tal deuda");	
		}
		
		String date = new Date() + "";
		Registro registro = new Registro(cuenta, tar, monto, date);
		
		if(llamarRegistro(registro)) {
			return new Pago(saldo, deuda, "Aceptado: Pago exitoso");
			
		} else {
			return new Pago(saldo, deuda, "Aceptado: Pago exitoso (No se pudo registrar el movimiento)");	
		}
	}
	
	private Double llamarCuenta(String cuenta, double monto) {

		OrdenCuenta orden = new OrdenCuenta(cuenta, monto);
		
		
		HttpEntity<OrdenCuenta> request = new HttpEntity<OrdenCuenta>(orden);
		RestTemplate restTemplate = new RestTemplate();
		
		
		ResponseEntity<ResponseCuenta> res = restTemplate.exchange(urlCuentas + "debitar/" + cuenta, HttpMethod.PUT, request, ResponseCuenta.class);
		
		double valor = res.getBody().getSaldo();
		
		if(res.getStatusCodeValue() == 200) {
			return valor;
		} else {
			return -valor;
		}
	}
	
	private Double devolverFondos(String cuenta, double monto) {
		OrdenCuenta orden = new OrdenCuenta(cuenta, monto);
		
		
		HttpEntity<OrdenCuenta> request = new HttpEntity<OrdenCuenta>(orden);
		RestTemplate restTemplate = new RestTemplate();
		
		
		ResponseEntity<ResponseCuenta> res = restTemplate.exchange(urlCuentas + "acreditar/" + cuenta, HttpMethod.PUT, request, ResponseCuenta.class);
		
		double valor = res.getBody().getSaldo();
		
		return valor;
	}
	
	private double llamarTarjeta(String tarjeta, double monto) {
		
		OrdenTarjeta orden = new OrdenTarjeta(tarjeta, monto);
		
		HttpEntity<OrdenTarjeta> request = new HttpEntity<OrdenTarjeta>(orden);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Double> res = restTemplate.exchange(urlTarjetas + "pago/" + tarjeta, HttpMethod.PUT, request, Double.class);
		
		double valor = res.getBody();
		
		if(res.getStatusCodeValue() == 200) {
			this.existeDeuda = true;
			return valor;
		} else {
			if(valor == 0){
				this.existeDeuda = false;
				return 0;
			}
			this.existeDeuda = true;
			return -valor;
		}
	}
	
	private Double getDeuda(String tarjeta) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Double> res = restTemplate.exchange(urlTarjetas + "deuda/" + tarjeta, HttpMethod.GET, null, Double.class);
		
		double valor = res.getBody();
		
		if(res.getStatusCodeValue() == 200) {
			return valor;
		} else {
			return -valor;
		}
	}
	
	private boolean llamarRegistro(Registro registro){

		HttpEntity<Registro> request = new HttpEntity<Registro>(registro);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Object> res = restTemplate.postForEntity(urlRegistros, request, Object.class);
		
		if(res.getStatusCodeValue() == 200) {
			return true;
		} else {
			return false;
		}
	}
}
