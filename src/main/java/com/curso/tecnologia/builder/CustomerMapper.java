package com.curso.tecnologia.builder;

import com.curso.tecnologia.dto.CustomerPurchaseDTO;
import com.curso.tecnologia.dto.PurchaseDTO;
import com.curso.tecnologia.model.Customer;
import com.curso.tecnologia.model.Purchase;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    private final ModelMapper modelMapper;

    public CustomerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Customer toEntity(CustomerPurchaseDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setCpf(dto.getCpf());

        List<Purchase> purchases = dto.getPurchases().stream()
                .map(this::toPurchaseEntity)
                .collect(Collectors.toList());

        customer.setPurchases(purchases);
        return customer;
    }

    public List<Customer> toListEntity(List<CustomerPurchaseDTO> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    private Purchase toPurchaseEntity(PurchaseDTO dto) {
        return modelMapper.map(dto, Purchase.class);
    }
}
