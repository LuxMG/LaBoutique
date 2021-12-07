
package com.egg.laboutique.entity;

import com.egg.laboutique.enums.Rol;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete; 
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NoArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE usuario SET alta = false WHERE id = ?") 
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private String nombre;
    
    @Column
    private String dni;
    
    @Column
    private String email;
    
    @Column
    private String telefono;
    
    @Column
    private String barrio;
    
    @Column
    @Enumerated(EnumType.STRING)
    private Rol rol;
    
    @Column
    private Boolean alta=true;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime creacion;

    @LastModifiedDate
    private LocalDateTime modificacion;

    
    
    
}
