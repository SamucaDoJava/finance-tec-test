package com.curso.tecnologia.controller;

import com.curso.tecnologia.dto.AggregatedPurchaseResponseDTO;
import com.curso.tecnologia.dto.LoyalCustomerDTO;
import com.curso.tecnologia.dto.PurchaseResponseDTO;
import com.curso.tecnologia.exception.ResourceNotFoundException;
import com.curso.tecnologia.service.PurchaseAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PurchaseAnalysisController {

    private final PurchaseAnalysisService purchaseService;

    public PurchaseAnalysisController(PurchaseAnalysisService service) {
        this.purchaseService = service;
    }

    @GetMapping("/compras")
    public List<AggregatedPurchaseResponseDTO> getSortedPurchases() {
        return purchaseService.getAllPurchasesSortedByValue();
    }

    @GetMapping("/maior-compra/{ano}")
    public PurchaseResponseDTO getBiggestPurchaseByYear(@PathVariable("ano") int year) {
        return purchaseService.getBiggestPurchaseByYear(year);
    }

    @GetMapping("/clientes-fieis")
    public List<LoyalCustomerDTO> getTopLoyalCustomers() {
        return purchaseService.getTop3LoyalCustomers();
    }

    @GetMapping("/recomendacao/{cpf}")
    public String getWineRecommendation(@PathVariable("cpf") String cpf) {
        return purchaseService.getWineRecommendationByCustomerCpf(cpf);
    }

}
