package org.iesvdm.mapper;

import org.iesvdm.dto.PedidoDTO;
import org.iesvdm.modelo.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PedidoMapper {
    @Mapping(source = "pedido.cliente.id", target = "id_cliente")
    @Mapping(source = "pedido.comercial.id", target = "id_comercial")
    public PedidoDTO pedidoAPedidoFormDTO(Pedido pedido);

    @Mapping(source = "id_cliente", target = "cliente.id")
    @Mapping(source = "id_comercial", target = "comercial.id")
    public Pedido pedidoFormDTOAPedido(PedidoDTO pedidoFormDTO);
}
