package com.servicios.orquestador.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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
	//private String urlRegistros = "http://localhost:8083/transaccion";
	public boolean existeDeuda;
	
	
	/**
	 *  Método que llama al microservicio de cuentas de depósito
	 * @param cuenta Número de cuenta a debitar
	 * @param monto Monto a debitar
	 * @return Saldo Actual
	 */
	public Mono<Double> llamarCuenta(String cuenta, double monto){

		OrdenCuenta orden = new OrdenCuenta(cuenta, monto);
		
		WebClient cliente = WebClient.create(urlCuentas);
		
		Mono<Double> response = cliente.put()
				.uri("/debitar/{cuenta}", cuenta)
				.syncBody(orden)
				.retrieve()
				.bodyToMono(Double.class);
		
		return response.map(valor -> valor.doubleValue());
	}
	
	/**
	 * Devuelve fondos a una cuenta de depósito
	 * @param cuenta Número de cuenta a devolver fondos
	 * @param monto Cantidad a devolver
	 * @return Saldo actual
	 */
	public Double devolverFondos(String cuenta, double monto) {
		OrdenCuenta orden = new OrdenCuenta(cuenta, monto);
			
		
		HttpEntity<OrdenCuenta> request = new HttpEntity<OrdenCuenta>(orden);
		RestTemplate restTemplate = new RestTemplate();
		
		
		ResponseEntity<ResponseCuenta> res = restTemplate.exchange(urlCuentas + "acreditar/" + cuenta, HttpMethod.PUT, request, ResponseCuenta.class);
		
		double valor = res.getBody().getSaldo();
		
		return valor;
	}
	
	/**
	 * Reduce la deuda de una tarjeta de crédito específica
	 * @param tarjeta Número de tarjeta a reducir deuda
	 * @param monto Cantitad de deuda a reducir
	 * @return Deuda restante
	 */
	public Mono<Double> llamarTarjeta(String tarjeta, double monto) {
		
		OrdenTarjeta orden = new OrdenTarjeta(tarjeta, monto);
		
		WebClient cliente = WebClient.create(urlTarjetas);
		
		Mono<Double> response = cliente.put().uri("/pago/{tarjeta}", tarjeta)
				.syncBody(orden)
				.retrieve()
				.bodyToMono(Double.class);
		
		return response.map(valor -> valor.doubleValue());
	}
	
	/**
	 * Retorna la deuda restante de una tarjeta de crédito específica
	 * @param tarjeta Número de tarjeta de crédito a consultar
	 * @return Deuda restante
	 */
	public Double getDeuda(String tarjeta) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Double> res = restTemplate.exchange(urlTarjetas + "deuda/" + tarjeta, HttpMethod.GET, null, Double.class);
		
		double valor = res.getBody();
		
		if(res.getStatusCodeValue() == 200) {
			return valor;
		} else {
			return -valor;
		}
	}
	
	/* Método aún no integrado
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
