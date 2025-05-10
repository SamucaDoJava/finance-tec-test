package com.curso.tecnologia.controller;

import com.curso.tecnologia.dto.LoyalCustomerDTO;
import com.curso.tecnologia.dto.PurchaseResponseDTO;
import com.curso.tecnologia.service.PurchaseAnalysisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PurchaseAnalysisController {

    private final PurchaseAnalysisService service;

    public PurchaseAnalysisController(PurchaseAnalysisService service) {
        this.service = service;
    }

    @GetMapping("/compras")
    public List<PurchaseResponseDTO> getSortedPurchases() {
        return service.getAllPurchasesSortedByValue();
    }

    @GetMapping("/maior-compra/{ano}")
    public PurchaseResponseDTO getBiggestPurchaseByYear(@PathVariable("ano") int ano) {
        return service.getBiggestPurchaseByYear(ano);
    }

    @GetMapping("/clientes-fieis")
    public List<LoyalCustomerDTO> getTopLoyalCustomers() {
        return service.getTop3LoyalCustomers();
    }

    @GetMapping("/recomendacao/{cpf}")
    public String getWineRecommendation(@PathVariable("cpf") String cpf) {
        return service.getWineRecommendationByCustomerCpf(cpf);
    }

}
