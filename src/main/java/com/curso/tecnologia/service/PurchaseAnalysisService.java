package com.curso.tecnologia.service;

import com.curso.tecnologia.dto.*;
import com.curso.tecnologia.exception.ResourceNotFoundException;
import com.curso.tecnologia.indicator.SortDirection;
import com.curso.tecnologia.util.ClientValidateUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PurchaseAnalysisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseAnalysisService.class);
    private final CustomerService customerService;
    private final ProductService productService;

    public List<AggregatedPurchaseResponseDTO> getAllPurchasesSortedByValue(int page, int size, SortDirection direction) {
        LOGGER.info("Listando compras agregadas com ordenação [{}], página [{}], tamanho [{}]", direction, page, size);

        size = size <= 0 ? 10 : size;
        page = Math.max(page, 1);

        List<PurchaseResponseDTO> flatPurchases = buildAllPurchases();
        Map<String, AggregatedPurchaseResponseDTO> groupedPurchases = groupPurchasesByCustomer(flatPurchases);
        List<AggregatedPurchaseResponseDTO> sorted = sortByTotalValue(groupedPurchases);

        if (direction == SortDirection.DESC) {
            Collections.reverse(sorted);
        }

        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, sorted.size());

        if (fromIndex >= sorted.size()) {
            return Collections.emptyList();
        }
        return sorted.subList(fromIndex, toIndex);
    }

    private Map<String, AggregatedPurchaseResponseDTO> groupPurchasesByCustomer(List<PurchaseResponseDTO> flatPurchases) {
        LOGGER.info("Iniciando agrupamento de compras por cliente, percorrendo todas as compras individuais...");
        Map<String, AggregatedPurchaseResponseDTO> grouped = new HashMap<>();

        for (PurchaseResponseDTO item : flatPurchases) {
            try {
                String key = item.getCustomerCpf();
                if (key == null || item.getCustomerName() == null || item.getProduct() == null) {
                    throw new IllegalArgumentException("Dados obrigatórios ausentes no item de compra: " + item);
                }

                grouped.putIfAbsent(
                        key,
                        new AggregatedPurchaseResponseDTO(
                                item.getCustomerName(),
                                item.getCustomerCpf(),
                                new ArrayList<>(),
                                BigDecimal.ZERO
                        )
                );

                AggregatedPurchaseResponseDTO aggregate = grouped.get(key);
                BigDecimal subtotal = item.getTotalValue();

                aggregate.getItems().add(
                        new PurchasedItemDTO(
                                item.getProduct(),
                                item.getQuantity(),
                                subtotal
                        )
                );
                BigDecimal updatedTotal = aggregate.getTotalValue().add(subtotal);
                aggregate.setTotalValue(updatedTotal);
            } catch (Exception e) {
                LOGGER.error("Erro ao processar item de compra: [{}]. Erro: [{}]", item, e.getMessage());
            }
        }
        LOGGER.info("Agrupamento concluído. Total de clientes processados: [{}]", grouped.size());
        return grouped;
    }

    private List<AggregatedPurchaseResponseDTO> sortByTotalValue(Map<String, AggregatedPurchaseResponseDTO> grouped) {
        return grouped.values().stream()
                .sorted(Comparator.comparing(AggregatedPurchaseResponseDTO::getTotalValue))
                .collect(Collectors.toList());
    }

    public PurchaseResponseDTO getBiggestPurchaseByYear(int year) {
        LOGGER.info("Buscando a maior compra do ano [{}]", year);
        ClientValidateUtil.isValidYear(year);

        PurchaseResponseDTO biggest = buildAllPurchases().stream()
                .filter(p -> p.getProduct().getPurchaseYear() == year)
                .max(Comparator.comparing(PurchaseResponseDTO::getTotalValue))
                .orElse(null);

        if (biggest != null) {
            LOGGER.info("Maior compra de [{}]: [{}] - R$:[{}]", year, biggest.getCustomerName(), biggest.getTotalValue());
            return biggest;
        } else {
            LOGGER.warn("Nenhuma compra encontrada para o ano [{}]", year);
            throw new ResourceNotFoundException("Nenhuma compra encontrada para o ano " + year);
        }
    }

    public List<LoyalCustomerDTO> getTop3LoyalCustomers() {
        LOGGER.info("Iniciando cálculo dos 3 clientes mais fiéis...");

        Map<String, BigDecimal> customerTotals = buildAllPurchases().stream()
                .collect(Collectors.groupingBy(
                        PurchaseResponseDTO::getCustomerCpf,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                PurchaseResponseDTO::getTotalValue,
                                BigDecimal::add
                        )
                ));
        LOGGER.debug("Total de clientes com compras registradas: [{}]", customerTotals.size());
        List<LoyalCustomerDTO> topCustomers = customerTotals.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .limit(3)
                .map(entry -> {
                    CustomerPurchaseDTO customer = customerService.fetchCustomerPurchases()
                            .stream()
                            .filter(c -> c.getCpf().equals(entry.getKey()))
                            .findFirst()
                            .orElse(null);

                    String name = customer != null ? customer.getName() : "Unknown";
                    LOGGER.debug("Cliente fiel identificado: [{}] ([{}]) - Total gasto: [{}]", name, entry.getKey(), entry.getValue());
                    return new LoyalCustomerDTO(name, entry.getKey(), entry.getValue());
                })
                .collect(Collectors.toList());

        LOGGER.info("Top 3 clientes fiéis calculados com sucesso.");
        return topCustomers;
    }

    /** Shows the history of the highest amount of wine consumed by a customer for a specific type of wine. */
    public String getMostPurchasedWineTypeByCustomerCpf(String cpf) {
        LOGGER.info("Buscando recomendação de vinho para CPF [{}] A maior quantidade de vinho de um tipo será o vinho mais frequente.", cpf);

        List<CustomerPurchaseDTO> customers = customerService.fetchCustomerPurchases();
        ClientValidateUtil.validateCpf(cpf, customers);
        List<ProductDTO> products = productService.fetchProducts();

        Map<Integer, ProductDTO> productMap = products.stream()
                .collect(Collectors.toMap(ProductDTO::getCode, p -> p));

        String recommendation = customers.stream()
                .filter(c -> c.getCpf().equals(cpf))
                .flatMap(c -> c.getPurchases().stream())
                .map(p -> {
                    ProductDTO product = productMap.get(p.getCode());
                    if (product == null) {
                        LOGGER.warn("Produto com código [{}] não encontrado para CPF [{}]", p.getCode(), cpf);
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

        LOGGER.info("Recomendação para CPF [{}]: [{}]", cpf, recommendation);
        return recommendation;
    }

    /** Show the customer's most frequent wine purchase, considers the most frequent purchase and not necessarily the largest quantities. */
    public String getMostFrequentWineTypeByCustomerCpf(String cpf) {
        LOGGER.info("Buscando recomendação de vinho mais frequente para CPF [{}] sera os mais frequentes e não a quantidade maior. ", cpf);

        List<CustomerPurchaseDTO> customers = customerService.fetchCustomerPurchases();
        ClientValidateUtil.validateCpf(cpf, customers);
        List<ProductDTO> products = productService.fetchProducts();

        Map<Integer, ProductDTO> productMap = products.stream()
                .collect(Collectors.toMap(ProductDTO::getCode, p -> p));

        String recommendation = customers.stream()
                .filter(c -> c.getCpf().equals(cpf))
                .flatMap(c -> c.getPurchases().stream())
                .map(p -> {
                    ProductDTO product = productMap.get(p.getCode());
                    if (product == null) {
                        LOGGER.warn("Produto com código [{}] não encontrado para CPF [{}]", p.getCode(), cpf);
                        return null;
                    }
                    return product.getWineType();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Sem recomendação");

        LOGGER.info("Recomendação por frequência para CPF [{}]: [{}]", cpf, recommendation);
        return recommendation;
    }

    private List<PurchaseResponseDTO> buildAllPurchases() {
        LOGGER.info("Iniciando montagem da lista de compras...");

        List<ProductDTO> products = productService.fetchProducts();
        LOGGER.debug("Produtos carregados: [{}]", products.size());

        Map<Integer, ProductDTO> productMap = products.stream()
                .collect(Collectors.toMap(ProductDTO::getCode, p -> p));

        List<CustomerPurchaseDTO> customers = customerService.fetchCustomerPurchases();
        LOGGER.debug("Clientes carregados: [{}]", customers.size());

        List<PurchaseResponseDTO> result = new ArrayList<>();

        for (CustomerPurchaseDTO customer : customers) {
            for (PurchaseDTO purchase : customer.getPurchases()) {
                ProductDTO product = productMap.get(purchase.getCode());
                if (product != null) {
                    try {
                        BigDecimal unitPrice = product.getRawPrice();
                        BigDecimal quantity = BigDecimal.valueOf(purchase.getQuantity());
                        BigDecimal total = unitPrice.multiply(quantity);

                        result.add(new PurchaseResponseDTO(
                                customer.getName(),
                                customer.getCpf(),
                                product,
                                purchase.getQuantity(),
                                total
                        ));
                    } catch (NumberFormatException e) {
                        LOGGER.error("Erro ao converter preço do produto código [{}]: [{}]", product.getCode(), e.getMessage());
                    }
                } else {
                    LOGGER.warn("Produto com código [{}] não encontrado para cliente [{}]", purchase.getCode(), customer.getCpf());
                }
            }
        }
        LOGGER.info("Total de compras processadas: [{}]", result.size());
        return result;
    }

    public List<PurchaseResponseDTO> getBiggestPurchasePerYearSorted(SortDirection direction, int page, int size) {
        LOGGER.info("Buscando maior compra por ano - direção [{}], página [{}], tamanho [{}]", direction, page, size);

        size = size <= 0 ? 1 : size;
        page = Math.max(page, 1);

        List<PurchaseResponseDTO> result = buildAllPurchases().stream()
                .collect(Collectors.groupingBy(p -> p.getProduct().getPurchaseYear()))
                .values().stream()
                .map(purchases -> purchases.stream()
                        .max(Comparator.comparing(PurchaseResponseDTO::getTotalValue))
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .sorted((p1, p2) -> {
                    int compare = p1.getTotalValue().compareTo(p2.getTotalValue());
                    if (compare == 0) {
                        // desempate por ano de compra
                        compare = Integer.compare(p1.getProduct().getPurchaseYear(), p2.getProduct().getPurchaseYear());
                    }
                    return direction == SortDirection.DESC ? -compare : compare;
                })
                .collect(Collectors.toList());

        int totalPages = (int) Math.ceil((double) result.size() / size);
        if (page > totalPages) {
            return Collections.emptyList();
        }

        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, result.size());

        return result.subList(fromIndex, toIndex);
    }




}
