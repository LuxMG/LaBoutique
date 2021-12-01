package com.egg.laboutique.service;

import com.egg.laboutique.entity.Categoria;
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
    public void crear(String nombre) {
        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoriaRepository.save(categoria);
    }

    @Transactional
    public void modificar(Long id, String nombre) {
        categoriaRepository.modificar(id, nombre);
    }

    @Transactional
    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
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
