
package com.egg.laboutique.controller;

import com.egg.laboutique.entity.Producto;
import com.egg.laboutique.entity.Usuario;
import com.egg.laboutique.enums.Estado;
import com.egg.laboutique.service.ProductoService;
import com.egg.laboutique.service.UsuarioService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/beneficiario")
@PreAuthorize("hasRole('Beneficiario')")
public class BeneficiarioController {

    @Autowired
    private ProductoService pService;
    
    @Autowired
    private UsuarioService usuarioService;
    
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
    
    //Logica cuando se compra un producto
    @PostMapping("/comprar/{id}")
    public RedirectView comprar(@RequestParam("producto") String productoId,HttpSession session) {
        Producto producto = pService.obtenerPorId(Long.parseLong(productoId));
        try {
            Usuario usuario = usuarioService.buscarPorEmail(session.getAttribute("email").toString());
            producto.setBeneficiario(usuario);
            producto.setEstado(Estado.Reservado);
            pService.modificarProducto(producto);
        } catch (Exception ex) {
            Logger.getLogger(ProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new RedirectView("beneficiario/tienda/");
    }
}
