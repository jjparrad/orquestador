package com.servicios.orquestador.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.servicios.orquestador.models.OrdenCuenta;
import com.servicios.orquestador.models.OrdenPago;
import com.servicios.orquestador.models.OrdenTarjeta;
import com.servicios.orquestador.models.Pago;
import com.servicios.orquestador.models.ResponseCuenta;

import reactor.core.publisher.Mono;

@Service
public class PagosService {
	
	
	private String urlCuentas = "http://localhost:8082/cuentaDeDepositos/";
	private String urlTarjetas = "http://localhost:8081/tarjetaCredito/";
	//private String urlRegistros = "http://www.mocky.io/v2/5cec66a5330000165f6d7a8a";
	
	private boolean existeDeuda;
	
	
	public Pago pagar(OrdenPago ordenPago) {
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
			saldo = devolverFondos(cuenta, monto);
			return new Pago(saldo, -deuda, "Rechazado: La tarjeta indicada no posee tal deuda");	
		}
		
		//llamarRegistro(ordenPago);
		return new Pago(saldo, deuda, "Aceptado: Pago exitoso");
		
	}
	
	private Double llamarCuenta(String cuenta, double monto){

		OrdenCuenta orden = new OrdenCuenta(cuenta, monto);
			
		
		HttpEntity<OrdenCuenta> request = new HttpEntity<OrdenCuenta>(orden);
		RestTemplate restTemplate = new RestTemplate();
		
		
		ResponseEntity<ResponseCuenta> res = restTemplate.exchange(urlCuentas + "debitar/" + cuenta, HttpMethod.PUT, request, ResponseCuenta.class);
		
		double valor = res.getBody().getSaldo();
		
		return valor;
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
/*
		OrdenTarjeta orden = new OrdenTarjeta(tarjeta, monto);
		
		WebClient cliente = WebClient.create(urlTarjetas);
		
		Mono<ClientResponse> response = cliente.put().uri("/pago/{tarjeta}", tarjeta)
				.syncBody(orden)
				.exchange();
		
		response.subscribe((res) -> {
			HttpStatus status = res.statusCode();
			
			Mono<Double> bodyToMono = res.bodyToMono(Double.class);
			
	        bodyToMono.subscribe((body) -> {
	        	
	        	System.out.println("BODY: " + body);
	        	System.out.println("STATUS CODE: " + status);

	        	
	        });
		});
		
		
		return 1.0; */
		
		
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
	/*
	private boolean llamarRegistro(OrdenPago orden){
		
		HttpEntity<String> request = new HttpEntity<String>(orden.toString());
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> res = restTemplate.exchange(urlRegistros, HttpMethod.POST, request, String.class);
		
		if(res.getStatusCodeValue() == 201) {
			return true;
		} else {
			return false;
		}
	}*/
	
}
