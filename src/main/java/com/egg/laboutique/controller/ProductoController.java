package com.egg.laboutique.controller;

import com.egg.laboutique.entity.Categoria;
import com.egg.laboutique.entity.Foto;
import com.egg.laboutique.entity.Producto;
import com.egg.laboutique.entity.Usuario;
import com.egg.laboutique.enums.Estado;
import com.egg.laboutique.enums.Tipo;
import com.egg.laboutique.exception.ServiceException;
import com.egg.laboutique.service.CategoriaService;
import com.egg.laboutique.service.FotoService;
import com.egg.laboutique.service.ProductoService;
import com.egg.laboutique.service.UsuarioService;
import java.time.LocalDateTime;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService pService;

    @Autowired
    private CategoriaService catService;

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private FotoService fotoService;
    
    
    //Detalle de un producto
    @GetMapping("/detalleProducto/{id}")
    public ModelAndView verProducto(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("producto-tienda");
        Producto producto = pService.obtenerPorId(id);
        mav.addObject("producto", producto);
        return mav;
    }

    //Crear un producto
    @GetMapping("/crear")
    public ModelAndView crearProducto(HttpSession session) {
        ModelAndView mav = new ModelAndView("nuevo-producto");
        
        try {
            //refactorizar nombre de html a formulario-producto
            Producto producto = new Producto();
            producto.setEstado(Estado.Disponible);
            System.out.println("------------------ANTES DEL SERVICE-----------------");
            Usuario usuario = usuarioService.buscarPorEmail(session.getAttribute("email").toString());
            System.out.println("--------------"+usuario.getNombre()+"--------------");
            switch (session.getAttribute("rol").toString()) {
                case "Beneficiario":
                    producto.setTipo(Tipo.Deseo);
                    producto.setBeneficiario(usuario);
                    producto.setDonante(null);
                    System.out.println("--------------"+producto.getTitulo()+"--------------");
                    break;
                case "Donante":
                    producto.setTipo(Tipo.Donacion);
                    producto.setDonante(usuario);
                    producto.setBeneficiario(null);
                    break;
            }
            System.out.println("----------------------DESPUES DEL SWITCH---------------------");
            mav.addObject("producto", producto);
            mav.addObject("categorias", catService.buscarTodas());
            mav.addObject("action", "guardar");
            
        } catch (Exception ex) {
            Logger.getLogger(ProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam MultipartFile archivo, @ModelAttribute Producto producto) {
        try {
            producto.setFoto(fotoService.guardar(archivo));
            pService.crearProducto(producto);
            
        } catch (ServiceException ex) {
            Logger.getLogger(ProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    public RedirectView modificarProducto(@RequestParam Long id, @RequestParam String titulo, @RequestParam String descripcion, @RequestParam Tipo tipo, @RequestParam Estado estado, @RequestParam Categoria categoria, @RequestParam Foto foto, @RequestParam Usuario donante, @RequestParam Usuario beneficiario, @RequestParam LocalDateTime modificacion) {
        pService.modificarProducto(id, titulo, descripcion, tipo, estado, categoria, foto, donante, beneficiario, modificacion);
        return new RedirectView("/listado");
    }

    //Trae todos los productos (Para admin)
    @GetMapping("/listado")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView mostrarProductos(){
        ModelAndView mav = new ModelAndView("producto-listado");
        List<Producto> productos = pService.obtenerTodos();
        mav.addObject("productos", productos);
        return mav;
    }

    //Deshabilitar desde admin
    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable Long id) {
        pService.eliminar(id);
        return new RedirectView("/listado");
    }

    //Cambiar estados de productos
    //Busquedas
    //filtros por categoria
    //Asociar 2 usuarios, agregando el segundo usuario a la BD y trayendo los datos.
}
