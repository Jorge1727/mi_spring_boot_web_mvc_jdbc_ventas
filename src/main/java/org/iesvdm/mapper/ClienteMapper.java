package org.iesvdm.mapper;

import org.iesvdm.dto.ClienteDTO;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "pedidos", source = "listaPedidos")
    public ClienteDTO clienteAClienteDTO(Cliente cliente, List<Pedido> listaPedidos);

    public Cliente clienteDTOACliente(ClienteDTO clienteDTO);
}
