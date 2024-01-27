package org.iesvdm.controlador;

import jakarta.validation.Valid;
import org.iesvdm.dto.PedidoDTO;
import org.iesvdm.mapper.PedidoMapper;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.Pedido;
import org.iesvdm.service.ClienteService;
import org.iesvdm.service.ComercialService;
import org.iesvdm.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
//Se puede fijar ruta base de las peticiones de este controlador.
//Los mappings de los métodos tendrían este valor /pedidos como
//prefijo.
//@RequestMapping("/pedidos")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;
	@Autowired
	private ComercialService comercialService;
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private PedidoMapper pedidoMapper;
	//Se utiliza inyección automática por constructor del framework Spring.
	//Por tanto, se puede omitir la anotación Autowired

	//@RequestMapping(value = "/pedidos", method = RequestMethod.GET)
	//equivalente a la siguiente anotación
	@GetMapping("/pedidos") //Al no tener ruta base para el controlador, cada método tiene que tener la ruta completa
	public String listar(Model model) {
		
		List<Pedido> listaPedidos =  pedidoService.listAll();
		List<Map.Entry<Cliente, Double>> listaClienteOrdenPedido = pedidoService.listadoOrden();

		model.addAttribute("listaPedidos", listaPedidos);
		model.addAttribute("listaClienteOrdenPedido", listaClienteOrdenPedido);

		return "pedidos";// es el nombre de templates, lo redirigue all
	}

	@GetMapping("/pedidos/crear-sinBusqueda")
	public String crear(Model model) {

		PedidoDTO pedidoDTO = new PedidoDTO();
		model.addAttribute("pedidoDTO", pedidoDTO);

		List<Cliente> listaClientes = this.clienteService.listAll();
		model.addAttribute("listaClientes", listaClientes);

		List<Comercial> listaComerciales = this.comercialService.listAll();
		model.addAttribute("listaComerciales", listaComerciales);

		return "crear-pedido-sin-busqueda";

	}
	@PostMapping("/pedidos/crear-sinBusqueda")
	public String submitCrear(@Valid @ModelAttribute("pedidoDTO") PedidoDTO pedidoDTO, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("pedidoDTO", pedidoDTO);

			List<Cliente> listaClientes = this.clienteService.listAll();
			model.addAttribute("listaClientes", listaClientes);

			List<Comercial> listaComerciales = this.comercialService.listAll();
			model.addAttribute("listaComerciales", listaComerciales);

			return "crear-pedido-sin-busqueda";
		}

		Pedido pedido = pedidoMapper.pedidoFormDTOAPedido(pedidoDTO);

		pedidoService.newPedido(pedido);

		return "redirect:/pedidos";
	}

//	para crear mediante busqueda AHORA NO LE DOY USO--------------------------------------------------------vv
	@GetMapping("/pedidos/crear/{id_cliente}/{id_comercial}")
	public String crear(Model model, @PathVariable Integer id_cliente, @PathVariable Integer id_comercial) {

		Pedido pedido = new Pedido();

		model.addAttribute("id_cliente", id_cliente);
		model.addAttribute("id_comercial", id_comercial);
		model.addAttribute("pedido", pedido);

		return "crear-pedido";
	}

	@PostMapping("/pedidos/crear/{clienteId}/{comercialId}")
	public String submitCrear(@Valid @ModelAttribute("pedido") Pedido pedido, Errors errors, @PathVariable String clienteId, @PathVariable String comercialId) {

		Integer id_cliente = Integer.parseInt(clienteId);
		Integer id_comercial = Integer.parseInt(comercialId);

		if(errors.hasErrors()){
			return "crear-pedido";
		}

		pedidoService.newPedidoIds(pedido, id_cliente, id_comercial);

		return "redirect:/pedidos";

	}

	@GetMapping("/pedidos/buscar")
	public String buscar(Model model) {

		Pedido pedido = new Pedido();
		model.addAttribute("pedido", pedido);

		return "crear-pedido-busquedaComercial";
	}

	@PostMapping("/pedidos/buscar")
	public String buscar(Model model, @RequestParam("nombreBuscar")String nombreBuscar){

			//obtengo la lista de los comerciales y filtro mediante streams
			List<Comercial> listaBusqueda = this.comercialService.listAll();

			listaBusqueda = listaBusqueda.stream()
					.filter(c -> c.getNombre().toLowerCase().contains(nombreBuscar.toLowerCase()))
					.collect(Collectors.toList());

			if(listaBusqueda.isEmpty()) {

				model.addAttribute("sinResultados", "No se encontraron resultados.");
			}

			model.addAttribute("listaBusqueda", listaBusqueda);

			return "lista-busqueda-comercial";

	}

	@PostMapping("/pedidos/crear/{id_comercial}")
	public String buscarCrear(Model model, @RequestParam("nombreBuscar")String nombreBuscar, @PathVariable Integer id_comercial ,@RequestParam("accion")String accion){

		//obtengo la lista de los comerciales y filtro mediante streams
		List<Cliente> listaBusqueda = this.clienteService.listAll();

		listaBusqueda = listaBusqueda.stream()
				.filter(c -> c.getNombre().toLowerCase().contains(nombreBuscar.toLowerCase()))
				.collect(Collectors.toList());

		if(listaBusqueda.isEmpty()) {

			model.addAttribute("sinResultados", "No se encontraron resultados.");
		}

		model.addAttribute("accion", accion);
		model.addAttribute("id_comercial", id_comercial);
		model.addAttribute("listaBusqueda", listaBusqueda);

		return "lista-busqueda-cliente";

	}

	@PostMapping("/pedidos/buscar/{id_comercial}")
	public String buscar(Model model, @PathVariable Integer id_comercial){


		model.addAttribute("id_comercial", id_comercial);

		return "crear-pedido-busquedaCliente";

	}
	//--------------------------------------------------------------------------------------------------^^^^^^

	@GetMapping("/pedidos/{id}")
	public String detalle(Model model, @PathVariable Integer id ) {

		Pedido pedido = pedidoService.one(id);
		model.addAttribute("pedido", pedido);

		return "detalle-pedido";

	}

	@GetMapping("/pedidos/editar/{id}")
	public String editar(Model model, @PathVariable Integer id) {

		Pedido pedido = pedidoService.one(id);
		model.addAttribute("pedido", pedido);

		return "editar-pedido";

	}

	@PostMapping("/pedidos/borrar/{id}")
	public RedirectView submitBorrar(@PathVariable Integer id) {

		pedidoService.deletePedido(id);
		return new RedirectView("/pedidos");

	}



}
