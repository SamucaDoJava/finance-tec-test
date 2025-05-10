package com.curso.tecnologia.service;

import com.curso.tecnologia.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PurchaseAnalysisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseAnalysisService.class);
    private final CustomerService customerService;
    private final ProductService productService;

    public PurchaseAnalysisService(CustomerService customerService, ProductService productService) {
        this.customerService = customerService;
        this.productService = productService;
    }

    public List<PurchaseResponseDTO> getAllPurchasesSortedByValue() {
        LOGGER.info("Ordenando todas as compras por valor total (crescente)...");

        List<PurchaseResponseDTO> sortedPurchases = buildAllPurchases().stream()
                .sorted(Comparator.comparingDouble(PurchaseResponseDTO::getTotalValue))
                .collect(Collectors.toList());

        LOGGER.debug("Total de compras ordenadas: {}", sortedPurchases.size());
        return sortedPurchases;
    }

    public PurchaseResponseDTO getBiggestPurchaseByYear(int year) {
        LOGGER.info("Buscando a maior compra do ano {}", year);

        PurchaseResponseDTO biggest = buildAllPurchases().stream()
                .filter(p -> p.getProduct().getPurchaseYear() == year)
                .max(Comparator.comparingDouble(PurchaseResponseDTO::getTotalValue))
                .orElse(null);

        if (biggest != null) {
            LOGGER.info("Maior compra de {}: {} - R${}", year, biggest.getCustomerName(), biggest.getTotalValue());
        } else {
            LOGGER.warn("Nenhuma compra encontrada para o ano {}", year);
        }

        return biggest;
    }

    public List<LoyalCustomerDTO> getTop3LoyalCustomers() {
        LOGGER.info("Iniciando cálculo dos 3 clientes mais fiéis...");

        Map<String, Double> customerTotals = buildAllPurchases().stream()
                .collect(Collectors.groupingBy(
                        PurchaseResponseDTO::getCustomerCpf,
                        Collectors.summingDouble(PurchaseResponseDTO::getTotalValue)
                ));

        LOGGER.debug("Total de clientes com compras registradas: {}", customerTotals.size());

        List<LoyalCustomerDTO> topCustomers = customerTotals.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(3)
                .map(entry -> {
                    CustomerPurchaseDTO customer = customerService.fetchCustomerPurchases()
                            .stream()
                            .filter(c -> c.getCpf().equals(entry.getKey()))
                            .findFirst()
                            .orElse(null);

                    String name = customer != null ? customer.getName() : "Unknown";
                    LOGGER.debug("Cliente fiel identificado: {} ({}) - Total gasto: {}", name, entry.getKey(), entry.getValue());

                    return new LoyalCustomerDTO(name, entry.getKey(), entry.getValue());
                })
                .collect(Collectors.toList());

        LOGGER.info("Top 3 clientes fiéis calculados com sucesso.");
        return topCustomers;
    }

    public String getWineRecommendationByCustomerCpf(String cpf) {
        LOGGER.info("Buscando recomendação de vinho para CPF {}", cpf);

        List<CustomerPurchaseDTO> customers = customerService.fetchCustomerPurchases();
        List<ProductDTO> products = productService.fetchProducts();

        Map<Integer, ProductDTO> productMap = products.stream()
                .collect(Collectors.toMap(ProductDTO::getCode, p -> p));

        String recommendation = customers.stream()
                .filter(c -> c.getCpf().equals(cpf))
                .flatMap(c -> c.getPurchases().stream())
                .map(p -> {
                    ProductDTO product = productMap.get(Integer.parseInt(p.getCode()));
                    if (product == null) {
                        LOGGER.warn("Produto com código {} não encontrado para CPF {}", p.getCode(), cpf);
                        return null;
                    }
                    return new AbstractMap.SimpleEntry<>(product.getWineType(), p.getQuantity());
                })
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Sem recomendação");

        LOGGER.info("Recomendação para CPF {}: {}", cpf, recommendation);
        return recommendation;
    }


    private List<PurchaseResponseDTO> buildAllPurchases() {
        LOGGER.info("Iniciando montagem da lista de compras...");

        List<ProductDTO> products = productService.fetchProducts();
        LOGGER.debug("Produtos carregados: {}", products.size());

        Map<Integer, ProductDTO> productMap = products.stream()
                .collect(Collectors.toMap(ProductDTO::getCode, p -> p));

        List<CustomerPurchaseDTO> customers = customerService.fetchCustomerPurchases();
        LOGGER.debug("Clientes carregados: {}", customers.size());

        List<PurchaseResponseDTO> result = new ArrayList<>();

        for (CustomerPurchaseDTO customer : customers) {
            for (PurchaseDTO purchase : customer.getPurchases()) {
                ProductDTO product = productMap.get(Integer.parseInt(purchase.getCode()));
                if (product != null) {
                    double unitPrice = Double.parseDouble(product.getRawPrice().replaceAll("[^\\d.]", ""));
                    double total = unitPrice * purchase.getQuantity();

                    result.add(new PurchaseResponseDTO(
                            customer.getName(),
                            customer.getCpf(),
                            product,
                            purchase.getQuantity(),
                            total
                    ));
                } else {
                    LOGGER.warn("Produto com código {} não encontrado para cliente {}", purchase.getCode(), customer.getCpf());
                }
            }
        }
        LOGGER.info("Total de compras processadas: {}", result.size());
        return result;
    }


}
