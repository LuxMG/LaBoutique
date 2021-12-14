package com.egg.laboutique.controller;

import com.egg.laboutique.entity.Producto;
import com.egg.laboutique.entity.Usuario;
import com.egg.laboutique.enums.Rol;
import com.egg.laboutique.service.UsuarioService;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
        ModelAndView mav = new ModelAndView("usuario-datos"); //llama al html
        mav.addObject("usuario", new Usuario());
        mav.addObject("action", "guardar");
        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(@ModelAttribute Usuario usuario) {
        RedirectView mav = new RedirectView("/usuario/crear"); //le digo a qué url quiero ir 
        try {
            uService.crearUsuario(usuario);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return mav;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarUsuario(@PathVariable Long id, HttpSession session) {
        ModelAndView mav = new ModelAndView("registro");
        try {
            String emailUsuario = session.getAttribute("email").toString();
            Usuario usuarioSession = uService.buscarPorEmail(emailUsuario);
            Usuario usuario = uService.buscarPorId(id);
            System.out.println("---------");
            System.out.println(usuarioSession.getId());
            System.out.println(usuario.getId());
            System.out.println("---------");
            if(!Objects.equals(usuarioSession.getId(), usuario.getId())){
                throw new Exception("No tiene permiso para modificar este usuario");
            }
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

    @PostMapping("/eliminar")
    public RedirectView darDeBaja(@RequestBody Long id) {
        try {
            uService.darDeBajaUsuario(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new RedirectView("/usuario");
    }

    //Trae todos los usuarios (Para admin)
    @GetMapping("/listado")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView mostrarUsuarios(){
        ModelAndView mav = new ModelAndView("usuario-listado");
        List<Usuario> usuarios = uService.buscarTodos();
        mav.addObject("usuarios", usuarios);
        return mav;
    }
    
}
