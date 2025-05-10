package com.curso.tecnologia.controller;

import com.curso.tecnologia.dto.AggregatedPurchaseResponseDTO;
import com.curso.tecnologia.dto.LoyalCustomerDTO;
import com.curso.tecnologia.dto.PurchaseResponseDTO;
import com.curso.tecnologia.indicator.SortDirection;
import com.curso.tecnologia.service.PurchaseAnalysisService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BonusController {

    private final PurchaseAnalysisService purchaseService;

    public BonusController(PurchaseAnalysisService service) {
        this.purchaseService = service;
    }


    @GetMapping("/maiores-compras")
    public List<PurchaseResponseDTO> getPurchasesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") SortDirection direction
    ) {
        return purchaseService.getBiggestPurchasePerYearSorted(direction, page, size);
    }

}
