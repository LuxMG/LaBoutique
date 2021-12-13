package com.egg.laboutique.controller;

import com.egg.laboutique.entity.Usuario;
import com.egg.laboutique.enums.Rol;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
public class PrincipalController {

    @GetMapping("/")
    public ModelAndView inicio(HttpSession session) {
        
        ModelAndView mav = new ModelAndView("index");
        System.out.println("Rol: " + session.getAttribute("rol"));
        return mav;
    }
    
    @GetMapping("/tienda")
    public RedirectView redireccionar(HttpSession session){
        String rol = session.getAttribute("rol").toString();
        
        if(rol.equals("Beneficiario"))
            return new RedirectView("/beneficiario/tienda");
        if(rol.equals("Donante"))
            return new RedirectView("/donante/tienda");
        if(rol.equals("ADMIN"))
            return new RedirectView("/producto/listado");
            
        return new RedirectView("/");
    }
    
}
