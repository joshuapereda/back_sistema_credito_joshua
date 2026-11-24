package com.codeper.ControlClientes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codeper.ControlClientes.dto.DashboardResumenDto;
import com.codeper.ControlClientes.service.IDashboardService;

@RestController
@RequestMapping("api/dashboard")
public class DashboardController {

    @Autowired
    private IDashboardService dashboardService;

    // GET /api/dashboard/resumen (pide token, como el resto)
    @GetMapping("/resumen")
    public ResponseEntity<DashboardResumenDto> obtenerResumen() {
        return ResponseEntity.ok(dashboardService.obtenerResumen());
    }
}
