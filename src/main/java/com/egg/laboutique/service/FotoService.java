package com.egg.laboutique.service;

import com.egg.laboutique.entity.Foto;
import com.egg.laboutique.exception.ServiceException;
import com.egg.laboutique.repository.FotoRepository;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoService {
    
    @Autowired
    private FotoRepository fotoRepository;
    
    // Metodo para guardar Foto
    public Foto guardar(MultipartFile archivo) throws ServiceException{
        if(archivo==null){
            throw new ServiceException("Archivo nulo");
        }
        try{
            Foto foto = new Foto();
            foto.setMime(archivo.getContentType());
            foto.setNombre(archivo.getName());
            foto.setContenido(archivo.getBytes());
            
            return fotoRepository.save(foto);
        }catch (IOException e){
            System.out.println(e.getMessage());
            throw new ServiceException("Error al cargar la foto");
        }
    }
    
    // Metodo para actualizar Foto
    public Foto actualizar(Long idFoto,MultipartFile archivo) throws ServiceException{
        if(archivo==null){
            throw new ServiceException("Archivo nulo");
        }
        if(idFoto==null){
            throw new ServiceException("IdFoto nulo");
        }
        
        try{
            
            Foto foto = new Foto();
            Optional<Foto> respuesta = fotoRepository.findById(idFoto);
            if(respuesta.isPresent()){
                foto = respuesta.get();
            }
            foto.setMime(archivo.getContentType());
            foto.setNombre(archivo.getName());
            foto.setContenido(archivo.getBytes());
            
            return fotoRepository.save(foto);
        }catch (IOException e){
            System.out.println(e.getMessage());
            throw new ServiceException("Error al cargar la foto");
        }
    }

    public Foto obtenerPorId(Long idFoto) {
        Optional<Foto> respuesta = fotoRepository.findById(idFoto);
        Foto foto;    
        if(respuesta.isPresent()){
                foto = respuesta.get();
                return foto;
            }else{
            return null;
        }
    }
}
