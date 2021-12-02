package com.egg.laboutique.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Foto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private String nombre;
    
    @Column
    private String mime; // tipo de archivo / extension
    
    @Lob // indica que el archivo es pesado
    @Basic(fetch = FetchType.LAZY) //Carga perezosa
    private byte[] contenido;
    
}
