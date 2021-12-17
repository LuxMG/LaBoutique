
package com.egg.laboutique.entity;

import com.egg.laboutique.enums.Estado;
import com.egg.laboutique.enums.Tipo;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE producto SET alta = false WHERE id = ?")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private String titulo;
    
    @Column
    private String descripcion;
    
    @Column
    @Enumerated(EnumType.STRING)
    private Tipo tipo; // donacion/deseo
    
    @Column
    @Enumerated(EnumType.STRING)
    private Estado estado; // disponible/reservado/etc
    
    @OneToOne
    private Categoria categoria; // ropa/mueble/etc
    
    @OneToOne
    private Foto foto;
    
    @OneToOne
    private Usuario donante; // el que da
    
    @OneToOne
    private Usuario beneficiario; // el que recibe
    
    @Column
    private Boolean alta;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime creacion;

    @LastModifiedDate
    private LocalDateTime modificacion;

    @Override
    public String toString() {
        return "Producto{" + "id=" + id + ", titulo=" + titulo + ", descripcion=" + descripcion + ", alta=" + alta + '}';
    }
}
