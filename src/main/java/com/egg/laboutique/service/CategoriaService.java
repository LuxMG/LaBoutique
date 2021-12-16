package com.egg.laboutique.service;

import com.egg.laboutique.entity.Categoria;
import com.egg.laboutique.exception.ServiceException;
import com.egg.laboutique.repository.CategoriaRepository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // ------------------------- alta-baja-modificacion ------------------------ 
    @Transactional
    public void crear(String nombre) throws ServiceException {
        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setAlta(true);
        
        validarCreacion(categoria);
        categoriaRepository.save(categoria);
    }
    
    @Transactional
    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }

    @Transactional
    public void modificar(Long id, String nombre) throws ServiceException {
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNombre(nombre);
        categoria.setAlta(true);
        
        validarModificacion(categoria);
        categoriaRepository.modificar(categoria.getId(), categoria.getNombre());
    }
    
    // ------------------------------ validaciones ----------------------------- 
    public void validarCreacion(Categoria categoria) throws ServiceException{
        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new ServiceException("Ya existe una categoria con ese nombre");
        }
    }
    
    public void validarModificacion(Categoria categoria) throws ServiceException{
        if (!categoriaRepository.existsById(categoria.getId())) {
            throw new ServiceException("No existe una categoria con el id " + categoria.getId());
        }
        //si el nombre ya esta usado por otra categoria
        Categoria aux = categoriaRepository.findByNombre(categoria.getNombre()).orElse(null);
        if(aux != null && aux.getId().equals(categoria.getId())){
            throw new ServiceException("Ya existe otra categoria con ese nombre");
        }
    }

    // ------------------------------- busquedas ------------------------------- 
    @Transactional(readOnly = true)
    public List<Categoria> buscarTodas() {
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }
    
    @Transactional(readOnly = true)
    public Categoria buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre).orElse(null);
    }
    
}
