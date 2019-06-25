package com.servicios.orquestador.services;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.servicios.orquestador.models.OrdenCuenta;
import com.servicios.orquestador.models.OrdenPago;
import com.servicios.orquestador.models.OrdenTarjeta;
import com.servicios.orquestador.models.Registro;

import reactor.core.publisher.Mono;

@Service
public class PagosService {
	
	
	private String urlCuentas = "http://localhost:8086/api";
	private String urlTarjetas = "http://localhost:8085/tarjeta";
	private String urlRegistros = "http://localhost:8087/api";
	
	
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

	
	public boolean llamarRegistro(OrdenPago ordenPago){
		
		String date = new Date() + "";
		Registro registro = new Registro(Long.parseLong(1+""), (ordenPago.getCuenta()), (ordenPago.getTarjeta()), ordenPago.getMonto(), date);
		
		
		WebClient cliente = WebClient.create(urlRegistros);
		
		cliente.post()
				.uri("/save/{numCuenta}/{numTarjeta}", ordenPago.getCuenta(), ordenPago.getTarjeta())
				.body(BodyInserters.fromObject(registro))
				.retrieve()
				.bodyToMono(String.class)
				.subscribe();
		
		return true;
	}
	
}
