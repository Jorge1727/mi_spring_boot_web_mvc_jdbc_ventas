package org.iesvdm.controlador;

import java.util.List;

import jakarta.validation.Valid;
import org.iesvdm.dto.ComercialDTO;
import org.iesvdm.modelo.*;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.service.ClienteService;
import org.iesvdm.service.ComercialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
//Se puede fijar ruta base de las peticiones de este controlador.
//Los mappings de los métodos tendrían este valor /comerciales como
//prefijo.
//@RequestMapping("/comerciales")
public class ComercialController {
    @Autowired
    private ComercialService comercialService;
    @Autowired
    private ClienteService clienteService;

    //Se utiliza inyección automática por constructor del framework Spring.
    //Por tanto, se puede omitir la anotación Autowired

    //@RequestMapping(value = "/comerciales", method = RequestMethod.GET)
    //equivalente a la siguiente anotación
    @GetMapping("/comerciales") //Al no tener ruta base para el controlador, cada método tiene que tener la ruta completa
    public String listar(Model model) {

        List<Comercial> listaComerciales =  comercialService.listAll();
        model.addAttribute("listaComerciales", listaComerciales);

        return "comerciales";

    }


    @GetMapping("/comerciales/crear")
    public String crear(Model model) {

        Comercial comercial = new Comercial();
        model.addAttribute("comercial", comercial);

        return "crear-comercial";
    }

    @PostMapping("/comerciales/crear")
    public String submitCrear(@Valid @ModelAttribute("comercial") Comercial comercial, Errors errors) {

        if(errors.hasErrors()){
            return "crear-comercial";
        }
        comercialService.newComercial(comercial);

        //Otra manera de redirijir si tenemos String en lugar de RedirectView
        return "redirect:/comerciales";

    }

    @GetMapping("/comerciales/{id}")
    public String detalle(Model model, @PathVariable Integer id ) {

        Comercial comercial = comercialService.one(id);
        ComercialDTO comercialDTO = comercialService.estadisticasComercial(id);
        List<Pedido> listaPedidos = comercialService.pedidosComercial(id);

        model.addAttribute("comercial", comercial);
        model.addAttribute("comercialDTO", comercialDTO);
        model.addAttribute("listaPedidos", listaPedidos);

        return "detalle-comercial";

    }

    @GetMapping("/comerciales/editar/{id}")
    public String editar(Model model, @PathVariable Integer id) {

        Comercial comercial = comercialService.one(id);
        model.addAttribute("comercial", comercial);

        return "editar-comercial";

    }

    @PostMapping("/comerciales/editar/{id}")
    public String submitEditar(@Valid @ModelAttribute("comercial") Comercial comercial, Errors errors) {

        if(errors.hasErrors()){
            return "editar-comercial";
        }
        comercialService.replaceComercial(comercial);

        return "redirect:/comerciales";
    }

    @PostMapping("/comerciales/borrar/{id}")
    public RedirectView submitBorrar(@PathVariable Integer id) {

        comercialService.deleteComercial(id);

        return new RedirectView("/comerciales");
    }

}
