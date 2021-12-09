package com.egg.laboutique.controller;

import com.egg.laboutique.entity.Categoria;
import com.egg.laboutique.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // ----------------------------- para mostrar ------------------------------ 
    @GetMapping
    public ModelAndView mostrarTodas() {
        ModelAndView mav = new ModelAndView("categorias");
        mav.addObject("categorias", categoriaService.buscarTodas());
        return mav;
    }

    // ------------------------------ para crear ------------------------------- 
    @GetMapping("/crear")
    public ModelAndView crearCategoria() {
        ModelAndView mav = new ModelAndView("categoria-formulario");
        mav.addObject("categoria", new Categoria());
        mav.addObject("title", "Crear Categoria");
        mav.addObject("action", "guardar");
        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(@ModelAttribute Categoria categoria) throws Exception {
        try {
            System.out.println("se esta guardando una categoria: " + categoria.getNombre());
            categoriaService.crear(categoria.getNombre());
        }catch(Exception e) {
            return new RedirectView("/usuarios/crear");
        }
        
        return new RedirectView("/categorias");
    }

    // ------------------------------ para editar ------------------------------ 
    @GetMapping("/editar/{id}")
    public ModelAndView editarCategoria(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("categoria-formulario");
        mav.addObject("categoria", categoriaService.buscarPorId(id));
        mav.addObject("title", "Editar Categoria");
        mav.addObject("action", "modificar");
        return mav;
    }

    @PostMapping("/modificar")
    public RedirectView modificar(@ModelAttribute Categoria categoria) {
        categoriaService.modificar(categoria.getId(), categoria.getNombre()                           );
        return new RedirectView("/categorias");
    }

    // ------------------------------ para borrar ------------------------------ 
    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return new RedirectView("/categorias");
    }
    
}
