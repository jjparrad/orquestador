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
		return pagosService.pagar(ordenPago);
	}
}