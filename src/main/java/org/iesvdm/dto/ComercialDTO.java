package org.iesvdm.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ComercialDTO {

    private String nombre;
    private BigDecimal total;
    private BigDecimal media;
    private BigDecimal maximo;
    private BigDecimal minimo;

    public ComercialDTO(){}

}
