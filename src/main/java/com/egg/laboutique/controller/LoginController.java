package com.egg.laboutique.controller;

import com.egg.laboutique.entity.Usuario;
import com.egg.laboutique.enums.Rol;
import com.egg.laboutique.service.UsuarioService;
import java.security.Principal;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping
public class LoginController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error,
            @RequestParam(required = false) String logout, Principal principal) {

        ModelAndView modelAndView = new ModelAndView("login");

        if (error != null) {
            modelAndView.addObject("error", "Correo o contraseña inválida");
        }

        if (logout != null) {
            modelAndView.addObject("logout", "Ha salido correctamente de la plataforma");
        }

        if (principal != null) {
            modelAndView.setViewName("redirect:/");
        }

        return modelAndView;
    }

    @GetMapping("/registrodonante")
    public ModelAndView signupDonante(HttpServletRequest request, Principal principal) {

        ModelAndView modelAndView = new ModelAndView("registro");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        modelAndView.addObject("usuario", new Usuario());
        modelAndView.addObject("rol", Rol.Donante);
        if (flashMap != null) {

            modelAndView.addObject("exito", flashMap.get("exito"));
            modelAndView.addObject("error", flashMap.get("error"));
            modelAndView.addObject("nombre", flashMap.get("nombre"));
            modelAndView.addObject("apellido", flashMap.get("apellido"));
            modelAndView.addObject("email", flashMap.get("email"));
            modelAndView.addObject("clave", flashMap.get("clave"));

        }

        if (principal != null) {
            modelAndView.setViewName("redirect:/");
        }

        return modelAndView;
    }
    
    @GetMapping("/registrobeneficiario")
    public ModelAndView signupBeneficiario(HttpServletRequest request, Principal principal) {

        ModelAndView modelAndView = new ModelAndView("registro");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        modelAndView.addObject("usuario", new Usuario());
        modelAndView.addObject("rol", Rol.Beneficiario );
        if (flashMap != null) {

            modelAndView.addObject("exito", flashMap.get("exito"));
            modelAndView.addObject("error", flashMap.get("error"));
            modelAndView.addObject("nombre", flashMap.get("nombre"));
            modelAndView.addObject("apellido", flashMap.get("apellido"));
            modelAndView.addObject("email", flashMap.get("email"));
            modelAndView.addObject("clave", flashMap.get("clave"));

        }

        if (principal != null) {
            modelAndView.setViewName("redirect:/");
        }

        return modelAndView;
    }
    
    @PostMapping("/registro")
    public RedirectView signup(@ModelAttribute Usuario usuario, RedirectAttributes attributtes) {
        RedirectView redirectView = new RedirectView("/producto/productosTienda");

        try {
            usuarioService.crearUsuario(usuario);
            attributtes.addFlashAttribute("exito", "El registro se ha realizado satisfactoriamente");
        } catch (Exception e) {
            attributtes.addFlashAttribute("error", e.getMessage());
            attributtes.addFlashAttribute("nombre", usuario.getNombre());
            attributtes.addFlashAttribute("dni", usuario.getDni());
            attributtes.addFlashAttribute("email", usuario.getEmail());
            attributtes.addFlashAttribute("clave", usuario.getClave());

            redirectView.setUrl("/signup");
        }

        return redirectView;
    }
}
