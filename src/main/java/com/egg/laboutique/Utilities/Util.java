package com.egg.laboutique.Utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    
    //Método para validar solo cadenas de texto
    public static Boolean checkName(String name){
        Pattern regEx = Pattern.compile("^[a-zA-Z]+(( [a-zA-Z]+)*)$");
        Matcher check = regEx.matcher(name);
        return check.matches();
    }
    
    public static Boolean checkEmail(String email){
        Pattern regEx = Pattern.compile("(([^<>()\\[\\]\\\\.,;:\\s@”]+(\\.[^<>()\\[\\]\\\\.,;:\\s@”]+)*)|(“.+”))@((\\[[0–9]{1,3}\\.[0–9]{1,3}\\.[0–9]{1,3}\\.[0–9]{1,3}])|(([a-zA-Z\\-0–9]+\\.)+[a-zA-Z]{2,}))");
        Matcher check = regEx.matcher(email);
        return check.matches();
    }
    
    public static Boolean checkDNI(String dni){
        Pattern regEx = Pattern.compile("[1-9]([0-9]{6,7})");
        Matcher check = regEx.matcher(dni);
        return check.matches();
    }
    
    public static Boolean checkTelefono(String telefono){
        Pattern regEx = Pattern.compile("[0-9]*");
        Matcher check = regEx.matcher(telefono);
        return check.matches();
    }
}
