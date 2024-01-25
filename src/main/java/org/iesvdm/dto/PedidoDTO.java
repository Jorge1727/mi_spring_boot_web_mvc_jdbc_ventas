package org.iesvdm.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
//Para generar un constructor con lombok con todos los args
@AllArgsConstructor
public class PedidoDTO {
    private int id;

    @NotNull(message = "El campo no puede estar vacio")
    @Positive(message = "El valor debe ser mayor que 0")
    private double total;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "El campo no puede estar vacio")
    private Date fecha;

    @Min(value=1, message = "{error.seleccione} {cliente}")
    private int id_cliente;

    @Min(value=1, message = "error")
    private int id_comercial;
    public PedidoDTO(){}

}
