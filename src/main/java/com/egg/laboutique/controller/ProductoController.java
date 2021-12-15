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
import org.springframework.web.multipart.MultipartFile;
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
    
    //Detalle de un producto
    @GetMapping("/detalleProducto/{id}")
    public ModelAndView verProducto(@PathVariable("id") Long id){
        ModelAndView mav = new ModelAndView("producto-tienda");
        Producto producto = pService.obtenerPorId(id);
        mav.addObject("producto", producto);
        return mav;
    }
    
    //Crear un producto
    @GetMapping("/crear")
    public ModelAndView crearProducto(){
        ModelAndView mav = new ModelAndView("nuevo-producto");//refactorizar nombre de html a formulario-producto
        mav.addObject("producto", new Producto());
        //mav.addObject("tipo", Tipo.Deseo); Lo modifico con la sesion
        //mav.addObject("categorias", catService.obtenerTodas());
        mav.addObject("action", "guardar");
        return mav;
    }
    
    //No tendria que pasar los atributos en el request? como paso el beneficiario o donante?
    @PostMapping("/guardar")//agregar id de usuario con datos de la sesión
    public RedirectView guardar(@RequestParam("titulo") String titulo, 
            @RequestParam("descripcion") String descripcion,
//            @RequestParam("tipo") Tipo tipo,
//            @RequestParam("estado") Estado estado,
//            @RequestParam("categoria") Categoria categoria,
            MultipartFile archivo){
        
        pService.crearProducto(titulo, descripcion, null, null, null, archivo);
        
        return new RedirectView("/listado");
    }
    @GetMapping("/editar/{id}")
    public ModelAndView editarProducto(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("nuevo-producto");//refactorizar nombre de html a formulario-producto
        mav.addObject("producto", pService.obtenerPorId(id));
        mav.addObject("title", "Editar Producto");
        mav.addObject("action", "modificar");
        return mav;
    }

    @PostMapping("/modificar")
    public RedirectView modificarProducto(@RequestParam Long id,@RequestParam String titulo,@RequestParam String descripcion, @RequestParam Tipo tipo, @RequestParam Estado estado,@RequestParam Categoria categoria,@RequestParam Foto foto, @RequestParam Usuario donante,@RequestParam Usuario beneficiario,@RequestParam LocalDateTime modificacion){
            pService.modificarProducto(id, titulo, descripcion, tipo, estado, categoria, foto, donante, beneficiario, modificacion);
        return new RedirectView("/listado");
    }
    
    //Trae todos los productos (Para admin)
    @GetMapping("/listado")
    public ModelAndView mostrarProductos(){
        ModelAndView mav = new ModelAndView("todos-los-productos");
        List<Producto> productos = pService.obtenerTodos();
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
}
