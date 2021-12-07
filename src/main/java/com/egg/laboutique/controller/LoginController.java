package com.egg.laboutique.controller;

import java.security.Principal;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping
public class LoginController {
    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error,
            @RequestParam(required = false) String logout, Principal principal) {

        ModelAndView modelAndView = new ModelAndView("login");

        if (error != null) {
            modelAndView.addObject("error", "Correo o contraseña inválida");
        }

        if (logout != null) {
            modelAndView.addObject("logout", "Ha salido correctamente de la plataforma");
        }

        if (principal != null) {
            modelAndView.setViewName("redirect:/");
        }

        return modelAndView;
    }
    
    @GetMapping("/signup")
    public ModelAndView signup(HttpServletRequest request, Principal principal) {
        ModelAndView modelAndView = new ModelAndView("signup");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            modelAndView.addObject("exito", flashMap.get("exito"));
            modelAndView.addObject("error", flashMap.get("error"));
//            modelAndView.addObject("nombre", flashMap.get("nombre"));
//            modelAndView.addObject("apellido", flashMap.get("apellido"));
//            modelAndView.addObject("correo", flashMap.get("correo"));
//            modelAndView.addObject("clave", flashMap.get("clave"));

        }
        
        if(principal!=null){
            modelAndView.setViewName("redirect:/");
        }
        
        return modelAndView;
    }
//    @PostMapping("/registro")
//    public RedirectView signup(@RequestParam String nombre,@RequestParam String apellido,
//            @RequestParam String correo,@RequestParam String clave,RedirectAttributes attributtes){
//        RedirectView redirectView = new RedirectView("/login");
//        
//        try{
//            usuarioService.crear(nombre,apellido,correo,clave);
//            attributtes.addFlashAttribute("exito", "El registro se ha realizado satisfactoriamente");
//        }catch(Exception e){
//           attributtes.addFlashAttribute("error", e.getMessage()); 
//           attributtes.addFlashAttribute("nombre", nombre); 
//           attributtes.addFlashAttribute("apellido", apellido); 
//           attributtes.addFlashAttribute("correo", correo); 
//           attributtes.addFlashAttribute("clave", clave); 
//           
//           redirectView.setUrl("/signup");
//        }
//        
//        return redirectView;
//    }
}
