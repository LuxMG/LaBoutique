package com.egg.laboutique.controller;

import com.egg.laboutique.entity.Foto;
import com.egg.laboutique.exception.ServiceException;
import com.egg.laboutique.service.FotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/foto")
public class FotoController {

    @Autowired
    private FotoService fotoService;
    
    @PostMapping
    public Foto guardar(@RequestParam("file") MultipartFile archivo) throws ServiceException{
        return fotoService.guardar(archivo);
    }
    
    @PutMapping
    public Foto actualizar(Long id, MultipartFile archivo) throws ServiceException{
        return fotoService.actualizar(id,archivo);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> obtenerPorId(@PathVariable("id") Long id){
        System.out.println("----###----");
        System.out.println(id);
        System.out.println("----###----");
        byte[] foto = fotoService.obtenerPorId(id).getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        
        return new ResponseEntity<>(foto,headers,HttpStatus.OK);
    }
}
