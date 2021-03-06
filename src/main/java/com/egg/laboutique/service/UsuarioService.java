package com.egg.laboutique.service;

import com.egg.laboutique.entity.Usuario;
import com.egg.laboutique.enums.Rol;
import com.egg.laboutique.repository.UsuarioRepository;
import java.util.Collections;
import java.util.List;
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

    /**
     * crear usuario
     *
     * @param usuario
     * @throws java.lang.Exception
     */
    @Transactional
    public void crearUsuario(Usuario usuario) throws Exception {

        //hago todas las validaciones
//        if (usuario.getNombre().equals("")){
//            throw new Exception("El nombre se encuentra vacío"); 
//        }
//          if (usuario.getDni().equals("")){
//            throw new Exception("El DNI se encuentra vacío" );     
//        }
//        if (usuario.getEmail().equals("")){
//            throw new Exception("El e-mail se encuentra vacío"); 
//        }
//        if (usuario.getTelefono().equals("")){
//            throw new Exception("El telefono se encuentra vacío" );     
//        }
//        if (usuario.getBarrio().equals("")){
//            throw new Exception("El barrio se encuentra vacío" );     
//        }  
        //Usuario usuario1 = new Usuario();
        //usuario1.setNombre(usuario.getNombre());
        //.setDni(usuario.getDni());
        //usuario1.setEmail(usuario.getEmail());
        //usuario1.setTelefono(usuario.getTelefono());
        //usuario1.setBarrio(usuario.getBarrio());
        //usuario1.setRol(usuario.getRol());
        //usuario1.setTelefono("1122223333");
        //usuario1.setBarrio("Barrio");
        //usuario1.setRol(Rol.ADMIN);
        usuario.setClave(encoder.encode(usuario.getClave()));

        System.out.println("******" + usuario.getNombre() + "********");
        System.out.println("******" + usuario.getClave() + "********");
        System.out.println("******" + usuario.getRol() + "********");

        usuarioRepository.save(usuario);

    }

    //modificar usuario
    @Transactional
    public void modificarUsuario(Long id, Usuario usuario) throws Exception {
        //hago todas las validaciones
        //si no existe el usuario, lanzo una excepcion 
        if (!usuarioRepository.existsUsuarioById(id)) {
            throw new Exception("No existe un usuario con este id " + id);
        }
        if (usuario.getNombre().equals("")) {
            throw new Exception("El nombre se encuentra vacío");
        }
        if (usuario.getDni().equals("")) {
            throw new Exception("El DNI se encuentra vacío");
        }
        if (usuario.getEmail().equals("")) {
            throw new Exception("El e-mail se encuentra vacío");
        }
        if (usuario.getTelefono().equals("")) {
            throw new Exception("El telefono se encuentra vacío");
        }
        if (usuario.getBarrio().equals("")) {
            throw new Exception("El barrio se encuentra vacío");
        }

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
        System.out.println("---entra a buscar por email");
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

}
