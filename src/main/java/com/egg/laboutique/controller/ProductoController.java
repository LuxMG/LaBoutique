package com.egg.laboutique.controller;

import com.egg.laboutique.entity.Categoria;
import com.egg.laboutique.entity.Foto;
import com.egg.laboutique.entity.Producto;
import com.egg.laboutique.entity.Usuario;
import com.egg.laboutique.enums.Estado;
import com.egg.laboutique.enums.Rol;
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
import org.springframework.web.bind.annotation.RequestBody;
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
            Usuario usuario = usuarioService.buscarPorEmail(session.getAttribute("email").toString());
            switch (session.getAttribute("rol").toString()) {
                case "Beneficiario":
                    producto.setTipo(Tipo.Deseo);
                    producto.setBeneficiario(usuario);
                    producto.setDonante(null);
                    break;
                case "Donante":
                    producto.setTipo(Tipo.Donacion);
                    producto.setDonante(usuario);
                    producto.setBeneficiario(null);
                    break;
            }
            mav.addObject("producto", producto);
            mav.addObject("categorias", catService.buscarTodas());
            mav.addObject("action", "guardar");

        } catch (Exception ex) {
            Logger.getLogger(ProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam MultipartFile archivo, @ModelAttribute Producto producto, HttpSession session) {
        String url = "";
        try {
            Usuario usuario = usuarioService.buscarPorEmail(session.getAttribute("email").toString());
            producto.setFoto(fotoService.guardar(archivo));
            pService.crearProducto(producto);
            
            if (usuario.getRol() == Rol.Donante) {
                url = "/donante/donaciones/" + usuario.getId();
            }
            if (usuario.getRol() == Rol.Beneficiario) {
                url = "/beneficiario/deseos/" + usuario.getId();
            }
        } catch (ServiceException ex) {
            Logger.getLogger(ProductoController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new RedirectView(url);
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarProducto(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("nuevo-producto");//refactorizar nombre de html a formulario-producto
        mav.addObject("producto", pService.obtenerPorId(id));
        mav.addObject("title", "Editar Producto");
        mav.addObject("categorias", catService.buscarTodas());
        mav.addObject("action", "modificar");
        return mav;
    }

    @PostMapping("/modificar")
    public RedirectView modificarProducto(
            @RequestParam MultipartFile archivo,
            @ModelAttribute Producto producto,
            HttpSession session) {

        String url = "";
        try {
            //validarProducto()
            Usuario usuario = usuarioService.buscarPorEmail(session.getAttribute("email").toString());
            //producto.setFoto(fotoService.actualizar(producto.getFoto().getId(), archivo));
            pService.modificarProducto(producto);
            if (usuario.getRol() == Rol.Donante) {
                url = "/donante/donaciones/" + usuario.getId();
            }
            if (usuario.getRol() == Rol.Beneficiario) {
                url = "/beneficiario/deseos/" + usuario.getId();
            }
        } catch (Exception ex) {
            Logger.getLogger(ProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new RedirectView(url);
    }

    //Trae todos los productos (Para admin)
    @GetMapping("/listado")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView mostrarProductos() {
        ModelAndView mav = new ModelAndView("producto-listado");
        List<Producto> productos = pService.obtenerTodos();
        mav.addObject("productos", productos);
        return mav;
    }

    //Deshabilitar desde admin o donante
    @GetMapping("/eliminar/{productoId}")
    public RedirectView eliminar(@PathVariable("productoId") Long id) {
        pService.eliminar(id);
        return new RedirectView("/donante/donaciones/" + id); //Si fuera admin deberia retornar el listado
    }

    @PostMapping("/comprar")
    @PreAuthorize("hasRole('Beneficiario')")
    public RedirectView comprar(@RequestParam("producto") String productoId, HttpSession session) {
        Producto producto = pService.obtenerPorId(Long.parseLong(productoId));
        try {
            Usuario usuario = usuarioService.buscarPorEmail(session.getAttribute("email").toString());
            producto.setBeneficiario(usuario);
            producto.setEstado(Estado.Reservado);
            pService.modificarProducto(producto);
        } catch (Exception ex) {
            Logger.getLogger(ProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new RedirectView("/usuario/datos/" + producto.getDonante().getId());
    }

    @PostMapping("/donar") //Asocia el donante al deseo y cambia el estado
    @PreAuthorize("hasRole('Donante')")
    public RedirectView donar(@RequestParam("producto") String productoId, HttpSession session) {
        Producto producto = pService.obtenerPorId(Long.parseLong(productoId));
        try {
            Usuario usuario = usuarioService.buscarPorEmail(session.getAttribute("email").toString());
            producto.setDonante(usuario);
            producto.setEstado(Estado.Tomado);
            pService.modificarProducto(producto);
        } catch (Exception ex) {
            Logger.getLogger(ProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new RedirectView("/usuario/datos/" + producto.getBeneficiario().getId());
    }

    @GetMapping("/entregado/{productoID}")
    public RedirectView entregado(@PathVariable("productoID") Long productoId, HttpSession session) {
        Producto producto = pService.obtenerPorId(productoId);
        producto.setEstado(Estado.Entregado);
        pService.modificarProducto(producto);

        return new RedirectView("/donante/donaciones/" + producto.getDonante().getId());
    }

    @GetMapping("/cancelar-compra/{idProducto}")
    public RedirectView cancelarCompra(@PathVariable("idProducto") Long idProducto) {
        Producto producto = pService.obtenerPorId(idProducto);
        producto.setEstado(Estado.Disponible);
        producto.setBeneficiario(null);
        pService.modificarProducto(producto);
        return new RedirectView("/beneficiario/tienda");
    }
    //Busquedas
    //filtros por categoria
}
