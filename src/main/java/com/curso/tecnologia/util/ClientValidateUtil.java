package com.curso.tecnologia.util;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import com.curso.tecnologia.dto.CustomerPurchaseDTO;
import com.curso.tecnologia.exception.ResourceNotFoundException;

import java.time.Year;
import java.util.List;

/** Classe utilitária responsável por validações relacionadas o usuário / cliente. */
public class ClientValidateUtil {

    /**
     * Valida o formato do CPF usando a biblioteca Stella da Caelum.
     *
     * @param cpf o CPF a ser validado.
     * @throws IllegalArgumentException se o CPF for inválido.
     */
    public static void isValidCpf(String cpf) {
        CPFValidator cpfValidator = new CPFValidator();
        try {
            cpfValidator.assertValid(cpf);
        } catch (InvalidStateException e) {
            throw new IllegalArgumentException("CPF inválido: " + cpf);
        }
    }

    /**
     * Verifica se o CPF informado existe na lista de clientes fornecida.
     * @param cpf       o CPF a ser verificado.
     * @param customers a lista de clientes a ser pesquisada.
     * @throws ResourceNotFoundException se o CPF não for encontrado na base de dados.
     */
    public static void isCpfExistInDataBase(String cpf, List<CustomerPurchaseDTO> customers) {
        boolean exists = customers.stream().anyMatch(c -> c.getCpf().equals(cpf));
        if (!exists) {
            throw new ResourceNotFoundException("Cliente com CPF " + cpf + " não encontrado");
        }
    }

    /**
     * Realiza a validação completa de um CPF, verificando se o formato é válido
     * e se o CPF existe na base de dados fornecida.
     * @param cpf       o CPF a ser validado.
     * @param customers a lista de clientes a ser usada na validação de existência.
     * @throws IllegalArgumentException     se o CPF for inválido.
     * @throws ResourceNotFoundException se o CPF não for encontrado na base de dados.
     */
    public static void validateCpf(String cpf, List<CustomerPurchaseDTO> customers) {
        isValidCpf(cpf);
        isCpfExistInDataBase(cpf, customers);
    }

    /**
     * Valida se o ano informado está dentro de um intervalo plausível.
     *
     * @param year o ano a ser validado
     * @throws IllegalArgumentException se o ano for inválido
     */
    public static void isValidYear(int year) {
        int currentYear = Year.now().getValue();
        if (year < 1900 || year > currentYear) {
            throw new IllegalArgumentException("Ano inválido: " + year);
        }
    }

}
