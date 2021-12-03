/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.laboutique.controller;

import com.egg.laboutique.entity.Producto;
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
    
    @GetMapping("/crear")
    public ModelAndView crearProducto(){
        ModelAndView mav = new ModelAndView("nuevo-producto");
        mav.addObject("producto", new Producto());
        //mav.addObject("tipo", Tipo.Deseo); Lo modifico con la sesion
        //Pasar atributos
        mav.addObject("action", "guardar");
        return mav;
    }
    
    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam Producto producto){
       // pService.crearProducto(producto.titulo, producto.descripcion, producto.tipo, producto.estado, producto.categoria, producto.foto, producto.donante, producto.beneficiario, producto.alta, producto.getModificacion());
        
        //Pasar objeto
        return new RedirectView("tienda");
    }
    
    @GetMapping("/modificar/{id}")
    public ModelAndView modificarProducto(@PathVariable("id") Long id){
        ModelAndView mav = new ModelAndView("nuevo-producto");
        mav.addObject("producto", pService.obtenerPorId(id));
        mav.addObject("action", "modificar");
        return mav;
    }
    
    //Trae todos los productos (Para admin)
    @GetMapping("/productos")
    public ModelAndView mostrarProductos(){
        ModelAndView mav = new ModelAndView("todos-los-productos");
        List<Producto> productos = pService.obtenerTodos();
        mav.addObject("productos", productos);
        return mav;
    }
    
    //Deshabilitar desde admin
    //Busquedas
    
}
