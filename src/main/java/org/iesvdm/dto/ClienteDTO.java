package org.iesvdm.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.Pedido;
import org.iesvdm.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//La anotación @Data de lombok proporcionará el código de:
//getters/setters, toString, equals y hashCode
//propio de los objetos POJOS o tipo Beans
@Data
//Para generar un constructor con lombok con todos los args
@AllArgsConstructor
public class ClienteDTO {
    private int id;

    @NotBlank(message = "{error.introducir.nombre}")
    @Size(min=4, message = "{error.nombre.size.min}")
    @Size(max=30, message = "{error.nombre.size.max}")
    private String nombre;

    @NotBlank(message = "{error.introducir.apellido}")
    @Size(min=4, message = "{error.apellido.size.min}")
    @Size(max=30, message = "{error.apellido.size.max}")
    private String apellido1;
    private String apellido2;

    @NotBlank(message = "{error.introducir.ciudad}")
    @Size(max=50, message = "{error.ciudad.size.max}")
    private String ciudad;

    //NotBlank no es valido para tipos numericos
    @Min(value=100, message = "{error.categoria.size.min}")
    @Max(value=1000, message = "{error.categoria.size.max}")
    private int categoria;

    @Email(message = "{error.email}", regexp="^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,5}")
    private String email;

    private List<Pedido> pedidos;
    private List<Comercial> comerciales;
    private int pedidosComercial;
    private int semestre;
    private int trimestre;
    private int anio;
    private int lustro;


    //Constructor vacio para poder hacer crear-cliente
    public ClienteDTO() {
    }

    public int getPedidosComercial(Comercial comercial) {

        List<Pedido> pedidosComercial = this.pedidos.stream().filter(p -> p.getComercial().equals(comercial) && p.getCliente().getId() == this.id)
                .collect(Collectors.toList());

        return pedidosComercial.size();
    }

    public List<Comercial> getComerciales() {

        List<Comercial> comerciales = this.pedidos.stream().filter(pedido -> pedido.getCliente().getId() == this.id)
                .map(pedido -> pedido.getComercial())
                .distinct()
                .collect(Collectors.toList());

        return comerciales;
    }

    public int getSemestre(Comercial comercial){

        Calendar unaSemana = Calendar.getInstance();
        unaSemana.add(Calendar.WEEK_OF_YEAR, -1);

        List<Pedido> pedidos = this.pedidos.stream()
                .filter(pedido -> pedido.getComercial().equals(comercial) && pedido.getCliente().getId() == this.id)
                .filter(pedido -> pedido.getFecha().after(unaSemana.getTime()))
                .collect(Collectors.toList());

        return pedidos.size();
    }

    public int getTrimestre(Comercial comercial){

        Calendar unTrimestre = Calendar.getInstance();
        unTrimestre.add(Calendar.MONTH, -3);

        List<Pedido> pedidos = this.pedidos.stream()
                .filter(pedido -> pedido.getComercial().equals(comercial) && pedido.getCliente().getId() == this.id)
                .filter(pedido -> pedido.getFecha().after(unTrimestre.getTime()))
                .collect(Collectors.toList());

        return pedidos.size();
    }

    public int getAnio(Comercial comercial){

        Calendar unAnio = Calendar.getInstance();
        unAnio.add(Calendar.YEAR, -1);

        List<Pedido> pedidos = this.pedidos.stream()
                .filter(pedido -> pedido.getComercial().equals(comercial) && pedido.getCliente().getId() == this.id)
                .filter(pedido -> pedido.getFecha().after(unAnio.getTime()))
                .collect(Collectors.toList());

        return pedidos.size();
    }

    public int getLustro(Comercial comercial){

        Calendar unLustro = Calendar.getInstance();
        unLustro.add(Calendar.YEAR, -5);

        List<Pedido> pedidos = this.pedidos.stream()
                .filter(pedido -> pedido.getComercial().equals(comercial) && pedido.getCliente().getId() == this.id)
                .filter(pedido -> pedido.getFecha().after(unLustro.getTime()))
                .collect(Collectors.toList());

        return pedidos.size();
    }

}
