
package com.egg.laboutique.controller;

import com.egg.laboutique.entity.Usuario;
import com.egg.laboutique.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    
     @Autowired
    private UsuarioService uService;
    
     @GetMapping("/crear")
    public ModelAndView crearUsuario() {
        ModelAndView mav= new ModelAndView("datos"); //llama al html que se llame datos
        mav.addObject("action", "guardar");
        return mav;
    }
    
         @PostMapping("/guardar")
    public RedirectView guardarUsuario(@RequestBody Usuario usuario ) {
        RedirectView mav= new RedirectView("/crear"); //le digo a qué url quiero ir 
        try {
            System.out.println("****" + usuario.getNombre() + "******");
        uService.crearUsuario(usuario);} catch(Exception e){
            System.out.println(e.getMessage());
        }
        return mav;
    }
    
    
}
