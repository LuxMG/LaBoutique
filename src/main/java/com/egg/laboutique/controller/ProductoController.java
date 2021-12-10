/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.laboutique.controller;

import com.egg.laboutique.entity.Categoria;
import com.egg.laboutique.entity.Foto;
import com.egg.laboutique.entity.Producto;
import com.egg.laboutique.entity.Usuario;
import com.egg.laboutique.enums.Estado;
import com.egg.laboutique.enums.Tipo;
import com.egg.laboutique.service.ProductoService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author Mailen
 */

@Controller
@RequestMapping("/producto")
public class ProductoController {
    @Autowired
    private ProductoService pService;
    
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
    
    //Vista de donaciones que subio un usuario
    @GetMapping("/donaciones/{id}")
    public ModelAndView mostrarDonacionesUsuario(@PathVariable("id") Long id){
        ModelAndView mav = new ModelAndView("donaciones");
        List<Producto> productos = pService.obtenerPorDonante(id);
        mav.addObject("productos", productos);
        return mav;
    }
    
    //Detalle de un producto
    @GetMapping("/detalleProducto")
    public ModelAndView verProducto(@PathVariable("id") Long id){
        ModelAndView mav = new ModelAndView("producto-tienda");
        Producto producto = pService.obtenerPorId(id);
        mav.addObject("producto", producto);
        return mav;
    }
    
    //Crear un producto
    @GetMapping("/crear")
    public ModelAndView crearProducto(){
        ModelAndView mav = new ModelAndView("nuevo-producto");
        mav.addObject("producto", new Producto());
        //mav.addObject("tipo", Tipo.Deseo); Lo modifico con la sesion
        //mav.addObject("categorias", catService.obtenerTodas());
        mav.addObject("action", "guardar");
        return mav;
    }
    
    //No tendria que pasar los atributos en el request? como paso el beneficiario o donante?
    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam Producto producto){
        pService.crearProducto(producto.getTitulo(), producto.getDescripcion(), producto.getTipo(), producto.getEstado(), producto.getCategoria(), producto.getFoto(), producto.getDonante(), producto.getBeneficiario(), producto.getAlta(), producto.getModificacion());
        return new RedirectView("/tienda");
    }
    
    @GetMapping("/modificar")
    public RedirectView modificarProducto(@RequestParam Long id,@RequestParam String titulo,@RequestParam String descripcion, @RequestParam Tipo tipo, @RequestParam Estado estado,@RequestParam Categoria categoria,@RequestParam Foto foto, @RequestParam Usuario donante,@RequestParam Usuario beneficiario,@RequestParam LocalDateTime modificacion){
            pService.modificarProducto(id, titulo, descripcion, tipo, estado, categoria, foto, donante, beneficiario, modificacion);
        return new RedirectView("/tienda");
    }
    
    //Trae todos los productos (Para admin)
    @GetMapping("/productos")
    public ModelAndView mostrarProductos(){
        ModelAndView mav = new ModelAndView("todos-los-productos");
        List<Producto> productos = pService.obtenerTodos();
        mav.addObject("productos", productos);
        return mav;
    }
    
    //Trae todos los productos donados a la tienda (vista Beneficiario)
    @GetMapping("/productosTienda")
    public ModelAndView mostrarProductosTienda(){
        ModelAndView mav = new ModelAndView("tienda");
        List<Producto> productos = pService.obtenerDonaciones();
        mav.addObject("productos", productos);
        return mav;
    }
    
     //Trae todos los deseos de todos los usuarios a la tienda (vista Benefactor)
    @GetMapping("/productosDeseos")
    public ModelAndView mostrarProductosDeseos(){
        ModelAndView mav = new ModelAndView("tienda");
        List<Producto> productos = pService.obtenerDeseos();
        mav.addObject("productos", productos);
        return mav;
    }
    
    //Deshabilitar desde admin
    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable Long id){
        pService.eliminar(id);
        return new RedirectView("todos-los-productos");
    }
    
    //Cambiar estados de productos
    //Busquedas
    //filtros por categoria
    //Asociar 2 usuarios, agregando el segundo usuario a la BD y trayendo los datos.
    /*
        String cel = "543329333126";
        String url;
        url = "https://api.whatsapp.com/send/?phone="+cel+"&text=Hola%21+Te+contacto+desde+la+boutique&app_absent=0";
        mav.addObject("url",url);
    
    */
}
