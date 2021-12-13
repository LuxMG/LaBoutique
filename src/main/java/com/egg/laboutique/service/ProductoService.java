/*
 */
package com.egg.laboutique.service;

import com.egg.laboutique.entity.Categoria;
import com.egg.laboutique.entity.Foto;
import com.egg.laboutique.entity.Producto;
import com.egg.laboutique.entity.Usuario;
import com.egg.laboutique.enums.Estado;
import com.egg.laboutique.enums.Tipo;
import com.egg.laboutique.exception.ServiceException;
import com.egg.laboutique.repository.ProductoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Mailen
 */

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository repo;
    
    @Autowired
    private FotoService fotoService;
    
    @Transactional
    public void crearProducto(String titulo, String descripcion, Tipo tipo, Estado estado, Categoria categoria, Foto foto, Usuario donante, Usuario beneficiario, Boolean alta, LocalDateTime modificacion){
        Producto producto = new Producto();
        producto.setTitulo(titulo);
        producto.setDescripcion(descripcion);
        producto.setTipo(tipo);
        producto.setEstado(estado);
        producto.setCategoria(categoria);
        producto.setFoto(foto);
        producto.setBeneficiario(beneficiario);
        producto.setDonante(donante);
        producto.setAlta(true);
        producto.setModificacion(modificacion);
        repo.save(producto);
    }
    
    @Transactional(readOnly = true)
    public Producto obtenerPorId(Long id){
        Optional<Producto> productoOptional = repo.findById(id);
        return productoOptional.orElse(null);
    }
    
    @Transactional
    public void modificarProducto(Long id, String titulo,String descripcion,Tipo tipo,Estado estado,Categoria categoria,Foto foto,Usuario donante,Usuario beneficiario,LocalDateTime modificacion){
        repo.modificar(id, titulo, descripcion, tipo, estado, categoria, foto, donante, beneficiario, modificacion);
    }
    
    //Trae todos los productos que estan dados de alta
    @Transactional(readOnly = true)
    public List<Producto> obtenerTodos(){
        return repo.obtenerTodos();
    }
    
    //Trae todos los deseos dados de alta
    @Transactional(readOnly = true)
    public List<Producto> obtenerDeseos(){
        return repo.obtenerDeseos();
    }
    
    
    //Trae todas las donaciones dadas de alta 
    @Transactional(readOnly = true)
    public List<Producto> obtenerDonaciones(){
        return repo.obtenerDonaciones();
    }
    
    //Trae los productos que esten en el carrito de un beneficiario
    @Transactional(readOnly = true)
    public List<Producto> obtenerCarrito(Long idBeneficiario){
        return repo.obtenerCarrito(idBeneficiario);
    }
    
    //Trae la lista de deseos de un beneficiario
    @Transactional(readOnly = true)
    public List<Producto> obtenerListaDeseos(Long idBeneficiario){
        return repo.obtenerListaDeseos(idBeneficiario);
    }
    
    //Trae los productos dados de alta por un donante
    @Transactional(readOnly = true)
    public List<Producto> obtenerPorDonante(Long idDonante){
        return repo.obtenerPorDonante(idDonante);
    }
    
    //Da de baja un producto
    @Transactional
    public void eliminar(Long id){
        repo.deleteById(id);
    }
    
    //Da de alta un producto
    @Transactional
    public void habilitar(Long id){
        repo.habilitar(id);
    }
    
    //Busca por titulo entre las donaciones (Barra de busqueda del beneficiario)
    @Transactional
    public void buscarDonacion(String busqueda){
        repo.buscarDonacion(busqueda);
    }
    
    //Busca por titulo entre los deseos (Barra de busqueda del donante)
    @Transactional
    public void buscarDeseo(String busqueda){
        repo.buscarDeseo(busqueda);
    }
    
    @Transactional
    public void crearProducto(String titulo, String descripcion, Tipo tipo, Estado estado, Categoria categoria, MultipartFile archivo) {
        
        try {
            
            Producto producto = new Producto();
            producto.setTitulo(titulo);
            producto.setDescripcion(descripcion);
            producto.setTipo(tipo);
            producto.setEstado(estado);
            producto.setCategoria(categoria);
            producto.setFoto(fotoService.guardar(archivo));
            
            repo.save(producto);
            
        } catch (ServiceException ex) {
            Logger.getLogger(ProductoService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
