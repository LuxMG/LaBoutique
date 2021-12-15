/*
 */
package com.egg.laboutique.repository;

import com.egg.laboutique.entity.Categoria;
import com.egg.laboutique.entity.Foto;
import com.egg.laboutique.entity.Producto;
import com.egg.laboutique.entity.Usuario;
import com.egg.laboutique.enums.Estado;
import com.egg.laboutique.enums.Tipo;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Mailen
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    //Long id, String titulo, String descripcion, Tipo tipo, Estado estado, Categoria categoria, Foto foto, Usuario donante, Usuario beneficiario, Boolean alta, LocalDateTime creacion, LocalDateTime modificacion
   
    //Para crear y modificar
    @Modifying
    @Query("UPDATE Producto p SET p.titulo = :titulo, "
            + "p.descripcion = :descripcion, p.tipo = :tipo, "
            + "p.estado = :estado, p.categoria = :categoria, "
            + "p.foto = :foto, "
            + "p.donante = :donante, p.beneficiario = :beneficiario "
            + "WHERE p.id = :id")
    void modificar(@Param("id") Long id,
            @Param("titulo") String titulo,
            @Param("descripcion") String descripcion,
            @Param("tipo") Tipo tipo, @Param("estado") Estado estado,
            @Param("categoria") Categoria categoria,
            @Param("foto") Foto foto, 
            @Param("donante") Usuario donante,
            @Param("beneficiario") Usuario beneficiario);
    
    //Trae todos los productos ordenados por tipo(Donacion/Deseo)
    @Query("SELECT p FROM Producto p WHERE p.alta = true ORDER BY p.tipo")
    List<Producto> obtenerTodos(); 
    
    //Trae los productos de tipo Donacion
    @Query("SELECT p FROM Producto p WHERE p.tipo = 'Donacion' AND p.alta = true")
    List<Producto> obtenerDonaciones(); 
    
    //Trae los productos de tipo Deseo
    @Query("SELECT p FROM Producto p WHERE p.tipo = 'Deseo' AND p.alta = true")
    List<Producto> obtenerDeseos(); 
    
    //Trae la lista de productos donados/ofrecidos por un usuario
    @Query("SELECT p FROM Producto p WHERE p.donante.id = :idDonante AND p.alta = true ORDER BY p.estado")
    List<Producto> obtenerPorDonante(@Param("idDonante") Long idDonante); 
    
    //Trae el carrito de un beneficiario
    @Query("SELECT p FROM Producto p WHERE p.beneficiario.id = :idBeneficiario AND p.alta = true AND p.tipo = 'Donacion'")
    List<Producto> obtenerCarrito(@Param("idBeneficiario") Long idBeneficiario);
    
    //Trae la lista de deseos de un beneficiario
    @Query("SELECT p FROM Producto p WHERE p.beneficiario.id = :idBeneficiario AND p.alta = true AND p.tipo = 'Deseo'")
    List<Producto> obtenerListaDeseos(@Param("idBeneficiario") Long idBeneficiario);
    
    //Habilita un producto
    @Modifying
    @Query("UPDATE Producto p SET p.alta = true WHERE p.id = :id")
    void habilitar(@Param("id") Long id);
    
    //Buscador Donación
    @Query("SELECT p FROM Producto p WHERE p.titulo LIKE '%:busqueda%' AND p.alta = true AND p.tipo = 'Donacion'")
    List<Producto> buscarDonacion(@Param("busqueda") String busqueda);

    //Buscador Deseo
    @Query("SELECT p FROM Producto p WHERE p.titulo LIKE '%:busqueda%' AND p.alta = true AND p.tipo = 'Deseo'")
    List<Producto> buscarDeseo(@Param("busqueda") String busqueda);
}
