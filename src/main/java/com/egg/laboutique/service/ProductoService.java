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

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repo;

    @Autowired
    private FotoService fotoService;

    @Transactional
    public void crearProducto(String titulo, String descripcion, Tipo tipo, Estado estado, Categoria categoria, Foto foto, Usuario donante, Usuario beneficiario, Boolean alta, LocalDateTime modificacion) throws Exception {
       
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
        validarProducto(producto);
        repo.save(producto);
    }

    @Transactional
    public void crearProducto(Producto producto) throws ServiceException {
        
        validarProducto(producto);
        producto.setAlta(true);
        repo.save(producto);
    }

    @Transactional(readOnly = true)
    public Producto obtenerPorId(Long id) {
        Optional<Producto> productoOptional = repo.findById(id);
        return productoOptional.orElse(null);
    }

    @Transactional
    public void modificarProducto(Long id, String titulo, String descripcion, Tipo tipo, Estado estado, Categoria categoria, Foto foto, Usuario donante, Usuario beneficiario, LocalDateTime modificacion) throws Exception {
        
        repo.modificar(id, titulo, descripcion, tipo, estado, categoria, foto, donante, beneficiario);
    }

    //Trae todos los productos que estan dados de alta
    @Transactional(readOnly = true)
    public List<Producto> obtenerTodos() {
        return repo.obtenerTodos();
    }

    //Trae todos los deseos dados de alta
    @Transactional(readOnly = true)
    public List<Producto> obtenerDeseos() {
        return repo.obtenerDeseos();
    }

    //Trae todas las donaciones dadas de alta 
    @Transactional(readOnly = true)
    public List<Producto> obtenerDonaciones() {
        return repo.obtenerDonaciones();
    }

    //Trae los productos que esten en el carrito de un beneficiario
    @Transactional(readOnly = true)
    public List<Producto> obtenerCarrito(Long idBeneficiario) {
        return repo.obtenerCarrito(idBeneficiario);
    }

    //Trae la lista de deseos de un beneficiario
    @Transactional(readOnly = true)
    public List<Producto> obtenerListaDeseos(Long idBeneficiario) {
        return repo.obtenerListaDeseos(idBeneficiario);
    }

    //Trae los productos dados de alta por un donante
    @Transactional(readOnly = true)
    public List<Producto> obtenerPorDonante(Long idDonante) {
        return repo.obtenerPorDonante(idDonante);
    }

    //Da de baja un producto
    @Transactional
    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    //Da de alta un producto
    @Transactional
    public void habilitar(Long id) {
        repo.habilitar(id);
    }

    //Busca por titulo entre las donaciones (Barra de busqueda del beneficiario)
    @Transactional
    public void buscarDonacion(String busqueda) {
        repo.buscarDonacion(busqueda);
    }

    //Busca por titulo entre los deseos (Barra de busqueda del donante)
    @Transactional
    public void buscarDeseo(String busqueda) {
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

    @Transactional
    public void modificarProducto(Producto producto) throws Exception {
        
        validarProducto(producto);
        repo.modificar(
                producto.getId(),
                producto.getTitulo(),
                producto.getDescripcion(),
                producto.getTipo(),
                producto.getEstado(),
                producto.getCategoria(),
                producto.getFoto(),
                producto.getDonante(),
                producto.getBeneficiario()
        );
    }

    @Transactional
    public void modificarProducto(MultipartFile archivo, Producto producto) throws ServiceException {

        validarProducto(producto);
        Foto foto = fotoService.actualizar(producto.getFoto().getId(), archivo);
        repo.modificar(
                producto.getId(),
                producto.getTitulo(),
                producto.getDescripcion(),
                producto.getTipo(),
                producto.getEstado(),
                producto.getCategoria(),
                foto,
                producto.getDonante(),
                producto.getBeneficiario()
        );
    }
    
     private void validarProducto(Producto producto) throws ServiceException {

        if (producto.getTitulo().equals("") || producto.getTitulo() == null) {
            throw new ServiceException("El título se encuentra vacío.");
        }

        if (producto.getDescripcion().equals("") ||producto.getDescripcion()  == null) {
            throw new ServiceException("La descripción se encuentra vacía.");
        }
        
        if (producto.getCategoria() == null) {
            throw new ServiceException("La categoría se encuentra vacía.");
        }
    }
}
