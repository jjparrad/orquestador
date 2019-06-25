package com.servicios.orquestador.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.servicios.orquestador.models.OrdenPago;
import com.servicios.orquestador.models.Pago;
import com.servicios.orquestador.services.PagosService;

import reactor.core.publisher.Mono;

@RestController
public class PagosController {
	
	@Autowired
	private PagosService pagosService;
	
	@PutMapping("/pago")
	public Mono<Pago> pagar(@RequestBody OrdenPago ordenPago) {
		Mono<Double> monoSaldo;
		Mono<Double> monoDeuda;
		
		String cuenta = ordenPago.getCuenta();
		String tarjeta = ordenPago.getTarjeta();
		double monto = ordenPago.getMonto();
		
		
		monoSaldo = pagosService.llamarCuenta(cuenta, monto);
		monoDeuda = pagosService.llamarTarjeta(tarjeta, monto);
				
		
		Mono<Pago> pago = monoSaldo.zipWith(monoDeuda)
				.map(tupla -> {
					double saldo = tupla.getT1().doubleValue();
					double deuda = tupla.getT2().doubleValue();
					
					pagosService.llamarRegistro(ordenPago);
					
					return new Pago(saldo, deuda, "Prueba");
				});
		
		return pago;
	}
}