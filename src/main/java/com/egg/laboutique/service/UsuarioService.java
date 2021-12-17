package com.egg.laboutique.service;

import com.egg.laboutique.Utilities.Util;
import com.egg.laboutique.entity.Categoria;
import com.egg.laboutique.entity.Usuario;
import com.egg.laboutique.exception.ServiceException;
import com.egg.laboutique.repository.UsuarioRepository;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional
    public void crearUsuario(Usuario usuario) throws Exception {
        //hago todas las validaciones
        validarUsuario(usuario);
        if(usuarioRepository.existsByEmail(usuario.getEmail())){
            throw new ServiceException("Ya existe un usuario con ese EMAIL");
        }
        if(usuarioRepository.existsByDni(usuario.getDni())){
            throw new ServiceException("Ya existe un usuario con ese DNI");
        }
        usuario.setClave(encoder.encode(usuario.getClave()));
        
        usuarioRepository.save(usuario);
    }

    //modificar usuario
    @Transactional
    public void modificarUsuario(Long id, Usuario usuario) throws Exception {
        //si no existe el usuario, lanzo una excepcion 
        if (!usuarioRepository.existsUsuarioById(id)) {
            throw new Exception("No existe un usuario con este id " + id);
        }
        //hago todas las validaciones
        validarUsuario(usuario);
        
        if(usuarioRepository.existeOtroUsuarioConMismoEmail(id, usuario.getEmail())!=null){
            throw new ServiceException("Ya existe otro usuario con ese EMAIL");
        }
        if(usuarioRepository.existeOtroUsuarioConMismoDNI(id, usuario.getDni())!=null){
            throw new ServiceException("Ya existe otro usuario con ese DNI");
        }
        
        //se pisan los valores viejos con los valores nuevos si es que hubo cambios
        Usuario usuario2 = usuarioRepository.findById(usuario.getId()).get();
        usuario2.setNombre(usuario.getNombre());
        usuario2.setDni(usuario.getDni());
        usuario2.setEmail(usuario.getEmail());
        usuario2.setTelefono(usuario.getTelefono());
        usuario2.setBarrio(usuario.getBarrio());
        usuario2.setRol(usuario.getRol());

        usuarioRepository.save(usuario2);
    }

    //dar de baja un usuario
    @Transactional
    public void darDeBajaUsuario(Long id) throws Exception {

        if (!usuarioRepository.existsUsuarioById(id)) {
            throw new Exception("No existe un usuario con este id " + id);
        }
        usuarioRepository.darBaja(id);

    }

    @Transactional
    public Usuario buscarPorId(Long id) throws Exception {

        Optional<Usuario> optional = usuarioRepository.findById(id);
        return optional.orElse(null);

    }

    @Transactional
    public Usuario buscarPorEmail(String email) throws Exception {

        if (email.equals("") || email.isEmpty()) {
            throw new Exception("El email no puede estar vacío");
        }
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public List<Usuario> buscarPorNombre(String nombre) throws Exception {
        
        if (nombre.equals("")) {
            throw new Exception("El nombre no puede estar vacío");
        }
        return usuarioRepository.getByNombre(nombre);
    }

    @Transactional
    public void darAlta(Long id) throws Exception {

        if (!usuarioRepository.existsUsuarioById(id)) {
            throw new Exception("No existe un usuario con este id " + id);
        }
        usuarioRepository.darAlta(id);

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No existe un usuario asociado al correo ingresado"));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attributes.getRequest().getSession(true);

        session.setAttribute("id", usuario.getId());
        session.setAttribute("nombre", usuario.getNombre());
        session.setAttribute("email", usuario.getEmail());
        session.setAttribute("rol", usuario.getRol());

        return new User(usuario.getEmail(), usuario.getClave(), Collections.singletonList(authority));
    }

    private void validarUsuario(Usuario usuario) throws ServiceException {

        if (usuario.getNombre().equals("") || usuario.getNombre() == null) {
            throw new ServiceException("El nombre se encuentra vacío");
        }

        if (!Util.checkName(usuario.getNombre())) {
            throw new ServiceException("Nombre no válido, utilice solo letras");
        }

        if (usuario.getDni().equals("") || usuario.getDni() == null) {
            throw new ServiceException("El DNI se encuentra vacío");
        }
        if (!Util.checkDNI(usuario.getDni())) {
            throw new ServiceException("DNI no válido, utilice solo numeros");
        }
        
        if (usuario.getEmail().equals("") || usuario.getEmail() == null) {
            throw new ServiceException("El e-mail se encuentra vacío");
        }
        if (!Util.checkEmail(usuario.getEmail())) {
            throw new ServiceException("Email no válido");
        }
        
        if (usuario.getTelefono().equals("") || usuario.getTelefono() == null) {
            throw new ServiceException("El telefono se encuentra vacío");
        }
        if (!Util.checkTelefono(usuario.getTelefono())) {
            throw new ServiceException("Telefono no válido, utilice solo números");
        }

        if (usuario.getBarrio().equals("") || usuario.getBarrio() == null) {
            throw new ServiceException("El barrio se encuentra vacío");
        }
    }
    
    // ------------------------------- busquedas ------------------------------- 
    @Transactional
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }
    
}
