package com.curso.tecnologia.controller;

import com.curso.tecnologia.dto.AggregatedPurchaseResponseDTO;
import com.curso.tecnologia.indicator.SortDirection;
import com.curso.tecnologia.service.PurchaseAnalysisService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseAnalysisControllerTest {
    @Mock
    private PurchaseAnalysisService service;
    @InjectMocks
    private PurchaseAnalysisController controller;

    @Test
    public void should_get_all_purchased_order_by_value() {
        List<AggregatedPurchaseResponseDTO> lista = List.of(mock(AggregatedPurchaseResponseDTO.class));

        when(service.getAllPurchasesSortedByValue(1, 10, SortDirection.ASC)).thenReturn(lista);
        assertEquals(lista, controller.getAllPurchasesSortedByValue(1, 10, SortDirection.ASC));
        verify(service).getAllPurchasesSortedByValue(1, 10, SortDirection.ASC);
    }

    @Test
    public void should_throw_exception_when_get_all() {
        when(service.getAllPurchasesSortedByValue(eq(1), eq(10), any())).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> controller.getAllPurchasesSortedByValue(1, 10, SortDirection.ASC));
    }

}