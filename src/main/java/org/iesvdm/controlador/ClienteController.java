package org.iesvdm.controlador;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.iesvdm.dto.ClienteDTO;
import org.iesvdm.mapper.ClienteMapper;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.Pedido;
import org.iesvdm.service.ClienteService;
import org.iesvdm.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
//Se puede fijar ruta base de las peticiones de este controlador.
//Los mappings de los métodos tendrían este valor /clientes como
//prefijo.
//@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;
	@Autowired
	private ClienteMapper clienteMapper;
	@Autowired
	private PedidoService pedidoService;
	//Se utiliza inyección automática por constructor del framework Spring.
	//Por tanto, se puede omitir la anotación Autowired

	//@RequestMapping(value = "/clientes", method = RequestMethod.GET)
	//equivalente a la siguiente anotación
	@GetMapping("/clientes") //Al no tener ruta base para el controlador, cada método tiene que tener la ruta completa
	public String listar(Model model) {
		
		List<Cliente> listaClientes =  clienteService.listAll();
		model.addAttribute("listaClientes", listaClientes);
				
		return "clientes";// es el nombre de templates, lo redirigue all
	}

	@GetMapping("/clientes/crear")
	public String crear(Model model) {

		Cliente cliente = new Cliente();
		model.addAttribute("cliente", cliente);

		return "crear-cliente";
	}

	@PostMapping("/clientes/crear")
	public String submitCrear(@Valid @ModelAttribute("cliente") Cliente cliente, Errors errors) {

		if(errors.hasErrors()){
			return "crear-cliente";
		}
		clienteService.newCliente(cliente);

		return "redirect:/clientes";

	}

	@GetMapping("/clientes/{id}")
	public String detalle(Model model, @PathVariable Integer id ) {

		Cliente cliente = clienteService.one(id);
		List<Pedido> pedidos = pedidoService.listAll();

		ClienteDTO clienteDTO = clienteMapper.clienteAClienteDTO(cliente, pedidos);


		model.addAttribute("clienteDTO", clienteDTO);
		model.addAttribute("cliente", cliente);

		return "detalle-cliente";

	}

	@GetMapping("/clientes/editar/{id}")
	public String editar(Model model, @PathVariable Integer id) {

		Cliente cliente = clienteService.one(id);
		model.addAttribute("cliente", cliente);

		return "editar-cliente";

	}

	@PostMapping("/clientes/editar/{id}")
	public String submitEditar(@Valid @ModelAttribute("cliente") Cliente cliente, Errors errors) {

		if(errors.hasErrors()){
			return "editar-cliente";
		}
		clienteService.replaceCliente(cliente);

		return "redirect:/clientes";
	}

	@PostMapping("/clientes/borrar/{id}")
	public RedirectView submitBorrar(@PathVariable Integer id) {

		clienteService.deleteCliente(id);
		return new RedirectView("/clientes");

	}

}
