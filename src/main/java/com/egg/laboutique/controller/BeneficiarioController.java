
package com.egg.laboutique.controller;

import com.egg.laboutique.entity.Producto;
import com.egg.laboutique.service.ProductoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/beneficiario")
@PreAuthorize("hasRole('Beneficiario')")
public class BeneficiarioController {

    @Autowired
    private ProductoService pService;
    
    //Trae todos los productos donados a la tienda (vista Beneficiario)
    @GetMapping("/tienda")
    public ModelAndView mostrarProductosTienda(){
        ModelAndView mav = new ModelAndView("tienda");
        List<Producto> productos = pService.obtenerDonaciones();
        mav.addObject("productos", productos);
        return mav;
    }
    
    //Carrito de un usuario
    @GetMapping("/carrito/{id}")
    public ModelAndView mostrarCarrito(@PathVariable("id") Long id){
        ModelAndView mav = new ModelAndView("carrito");
        List<Producto> productos = pService.obtenerCarrito(id);
        mav.addObject("productos", productos);
        return mav;
    }
    
    //Vista deseos de un usuario
    @GetMapping("/deseos/{id}")
    public ModelAndView mostrarDeseosUsuario(@PathVariable("id") Long id){
        ModelAndView mav = new ModelAndView("deseos");
        List<Producto> productos = pService.obtenerListaDeseos(id);
        mav.addObject("productos", productos);
        return mav;
    }
    
    @PostMapping("/comprar/{id}")
    public RedirectView comprarProducto(@PathVariable("id") Long id){
        // cambiar estado del producto
        // setear al usuario que compro como beneficiario
        // mostrar los datos del usuario donante
        
        return new RedirectView("/beneficiario/tienda");
    }
}
