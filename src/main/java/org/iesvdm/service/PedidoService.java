package org.iesvdm.service;

import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
public class PedidoService {

	@Autowired
	private PedidoDAO pedidoDAO;

	//Se utiliza inyección automática por constructor del framework Spring.
	//Por tanto, se puede omitir la anotación Autowired

	public List<Pedido> listAll() {
		return pedidoDAO.getAll();
	}

	public List<Map.Entry<Cliente, Double>> listadoOrden(){
		List<Pedido> listaPedidos = pedidoDAO.getAll();

		Map<Cliente, Double> sumaPorCliente = listaPedidos.stream()
				.collect(Collectors.groupingBy(Pedido::getCliente, Collectors.summingDouble(Pedido::getTotal)));

		List<Map.Entry<Cliente, Double>> clientesOrdenadosPorSuma = sumaPorCliente.entrySet().stream()
				.sorted((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()))
				.collect(Collectors.toList());

		return clientesOrdenadosPorSuma;
	}

	public Pedido one(Integer id) {
		Optional<Pedido> optPedido = pedidoDAO.find(id);
		if (optPedido.isPresent())
			return optPedido.get();
		else
			return null;
	}

	public void newPedido(Pedido pedido) {

		pedidoDAO.create(pedido);
	}

	// Para crear mediante busqueda ya que no sabia la existencia del mapper y dto
	public void newPedidoIds(Pedido pedido, Integer id_cliente, Integer id_comercial) {

		pedidoDAO.createCliCom(pedido, id_cliente, id_comercial);
	}

	public void replacePedido(Pedido pedido) {

		pedidoDAO.update(pedido);
	}

	public void replacePedidoIds(Pedido pedido, Integer id_cliente, Integer id_comercial) {

		pedidoDAO.updateCliCom(pedido, id_cliente, id_comercial);
	}

	public void deletePedido(int id){
		pedidoDAO.delete(id);
	}
}
