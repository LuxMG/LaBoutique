package com.egg.laboutique.controller;

import com.egg.laboutique.entity.Usuario;
import com.egg.laboutique.service.UsuarioService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService uService;

    @GetMapping("/crear")
    public ModelAndView crearUsuario() {
        ModelAndView mav = new ModelAndView("datos"); //llama al html que se llame datos
        mav.addObject("usuario", new Usuario());
        mav.addObject("action", "guardar");
        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardarUsuario(@ModelAttribute Usuario usuario) {
        RedirectView mav = new RedirectView("/usuario/crear"); //le digo a qué url quiero ir 
        try {
            System.out.println("******" + usuario.getNombre() + "****");
            uService.crearUsuario(usuario);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return mav;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarProducto(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("registro");
        try {
            Usuario usuario = uService.buscarPorId(id);
            mav.addObject("usuario", usuario);
            mav.addObject("rol", usuario.getRol());
            mav.addObject("title", "Editar Usuario");
            mav.addObject("action", "modificar");

        } catch (Exception ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mav;
        
    }

    @PostMapping("/modificar")
    public RedirectView modificar(@RequestBody Usuario usuario, RedirectAttributes attributtes) {
        RedirectView redirectView = new RedirectView("/producto/productosTienda");
        try {
            uService.modificarUsuario(usuario.getId(), usuario);
            attributtes.addFlashAttribute("exito", "El registro se ha realizado satisfactoriamente");
        } catch (Exception e) {
            attributtes.addFlashAttribute("error", e.getMessage());
            attributtes.addFlashAttribute("nombre", usuario.getNombre());
            attributtes.addFlashAttribute("dni", usuario.getDni());
            attributtes.addFlashAttribute("email", usuario.getEmail());
            attributtes.addFlashAttribute("clave", usuario.getClave());
            switch (usuario.getRol()) {
                case Beneficiario:
                    redirectView.setUrl("/registrobeneficiario");
                    break;
                case Donante:
                    redirectView.setUrl("/registrodonante");
                    break;
                default:
                    redirectView.setUrl("/");
                    break;
            }
        }
        return redirectView;
    }

    @PostMapping("/darDeBajaUsuario")
    public RedirectView darDeBaja(@RequestBody Long id) {
        try {
            uService.darDeBajaUsuario(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new RedirectView("/usuario");
        }
        return new RedirectView("/usuario");
    }

}
