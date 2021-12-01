
package com.egg.laboutique.repository;

import com.egg.laboutique.entity.Categoria;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

    // ------------------------- para modificar ------------------------ 
    @Modifying
    @Query("UPDATE Categoria c SET c.nombre = :nombre WHERE c.id = :id")
    void modificar(@Param("id") Long id, @Param("nombre") String nombre);
    
    // ------------------------------- busquedas ------------------------------- 
    Optional<Categoria> findByNombre(String nombre);
}
