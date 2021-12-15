package com.egg.laboutique.repository;

import com.egg.laboutique.entity.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //Alta, baja y modificacion sería lo esencial.
    //alta, modificar y baja de un usuario entero (se resuelve con el repository.save en el Service)  
    //traer todos los usuarios (se resuelve con findAll en el Service)
    //aca reviso que el usuario exista de acuerdo al id
    Boolean existsUsuarioById(Long id);

    //traer usuario por nombre
    @Query(value = "SELECT * from usuario as u WHERE u.nombre = :nombre", nativeQuery = true)
    List<Usuario> getByNombre(@Param("nombre") String nombre);
    //@Query(value = "SELECT * FROM libro WHERE libro.id = :id AND libro.alta = true ", nativeQuery = true)
    //Libro findByIdAndAlta(@Param("id") int id);

    //traer usuario por correo electronico
    @Query(value = "SELECT * from Usuario u WHERE u.email = :email", nativeQuery = true)
    Usuario getByEmail(@Param("email") String email);
    

    //modificacion - le cambio el estado a usuario como "alta"
    @Modifying
    @Query(value = "UPDATE usuario as u SET u.alta= true WHERE id= :id", nativeQuery = true)
    void darAlta(@Param("id") Long id);

    //modificacion - le cambio el estado a usuario como "baja"
    @Modifying
    @Query(value = "UPDATE usuario as u SET u.alta= false WHERE id= :id", nativeQuery = true)
    void darBaja(@Param("id") Long id);

    // Retorna usuario por email
    Optional<Usuario> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    boolean existsByDni(String dni);
    
    @Query(value = "SELECT * from usuario WHERE usuario.email = :email AND usuario.id != :id",nativeQuery = true)
    Usuario existeOtroUsuarioConMismoEmail(@Param("id") Long id,@Param("email") String email);
    
    @Query(value = "SELECT * from usuario WHERE usuario.dni = :dni AND usuario.id != :id",nativeQuery = true)
    Usuario existeOtroUsuarioConMismoDNI(@Param("id") Long id,@Param("dni") String dni);
}
