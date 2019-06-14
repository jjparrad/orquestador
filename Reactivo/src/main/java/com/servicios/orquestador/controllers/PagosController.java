package com.servicios.orquestador.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.servicios.orquestador.models.OrdenPago;
import com.servicios.orquestador.models.Pago;
import com.servicios.orquestador.services.PagosService;

@RestController
public class PagosController {
	
	@Autowired
	private PagosService pagosService;
	
	@PutMapping("/pago")
	public Pago pagar(@RequestBody OrdenPago ordenPago) {
		double saldo;
		double deuda;
		
		String cuenta = ordenPago.getCuenta();
		String tarjeta = ordenPago.getTarjeta();
		double monto = ordenPago.getMonto();
		

		saldo = pagosService.llamarCuenta(cuenta, monto);
		if (saldo < 0){
			
			deuda = pagosService.getDeuda(tarjeta);
			return new Pago(-saldo, deuda, "Rechazado: Fondos insuficientes");
		} 
		
		
		deuda = pagosService.llamarTarjeta(tarjeta, monto);
		if (deuda < 0 || pagosService.existeDeuda == false) {
			
			saldo = pagosService.devolverFondos(cuenta, monto);
			return new Pago(saldo, -deuda, "Rechazado: La tarjeta indicada no posee tal deuda");	
		}
		
		//llamarRegistro(ordenPago);
		return new Pago(saldo, deuda, "Aceptado: Pago exitoso");
	}
}