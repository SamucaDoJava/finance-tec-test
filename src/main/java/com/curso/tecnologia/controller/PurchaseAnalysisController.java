package com.curso.tecnologia.controller;

import com.curso.tecnologia.dto.AggregatedPurchaseResponseDTO;
import com.curso.tecnologia.dto.LoyalCustomerDTO;
import com.curso.tecnologia.dto.PurchaseResponseDTO;
import com.curso.tecnologia.indicator.SortDirection;
import com.curso.tecnologia.service.PurchaseAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PurchaseAnalysisController {

    private final PurchaseAnalysisService purchaseService;

    @GetMapping("/compras")
    public List<AggregatedPurchaseResponseDTO> getAllPurchasesSortedByValue(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ASC") SortDirection direction
    ) {
        return purchaseService.getAllPurchasesSortedByValue(page, size, direction);
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
        return purchaseService.getMostFrequentWineTypeByCustomerCpf(cpf);
    }

}
