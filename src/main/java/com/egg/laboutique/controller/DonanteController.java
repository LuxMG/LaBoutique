
package com.egg.laboutique.controller;

import com.egg.laboutique.entity.Producto;
import com.egg.laboutique.service.ProductoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/donante")
public class DonanteController {

    @Autowired
    private ProductoService pService;
    
    //Trae todos los deseos de todos los usuarios a la tienda (vista Donante)
    @GetMapping("/tienda")
    public ModelAndView mostrarProductosDeseos(){
        ModelAndView mav = new ModelAndView("tienda");
        List<Producto> productos = pService.obtenerDeseos();
        mav.addObject("productos", productos);
        return mav;
    }
    
    //Vista de donaciones que subio un usuario
    @GetMapping("/donaciones/{id}")
    public ModelAndView mostrarDonacionesUsuario(@PathVariable("id") Long id){
        ModelAndView mav = new ModelAndView("donaciones");
        List<Producto> productos = pService.obtenerPorDonante(id);
        mav.addObject("productos", productos);
        return mav;
    }
    
    
}
