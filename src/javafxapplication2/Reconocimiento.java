/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author mzapataj
 */
public class Reconocimiento {
    Scanner sc = new Scanner(System.in);
    String ExpRegular;
    Stack stackParentesis = new Stack();
    Stack pilaPrioridad = new Stack();
    boolean isParentesis = false;
    boolean valid = true;
    
    public Reconocimiento(){
        ExpRegular = sc.nextLine();
        ArrayList tokens = new ArrayList();
        String tokenAnterior = "";
        for(String c : ExpRegular.split("")){
            tokens.add(reconocerToken(c,tokenAnterior));
            tokenAnterior = c;
        }
        if(!valid){
            System.out.println("No es valido");
        }else{
            Matcher m = Pattern.compile("\\(([^()]|(?R))*\\)").matcher(ExpRegular);
            System.out.println("Division de parentesis");
            while(m.find()) {
                System.out.println(m.group(1));
            }
        }
        
        System.out.println(tokens);
    }
    public String reconocerToken(String token,String tokenAnterior){
        switch(token){
            case "|":
                if(tokenAnterior.equals(token) ){
                    valid = false;
                }
                    return "op_|";
            case "*":
                return "op_*";
            case "+":
                return "op_+";
            case "?":
                return "op_?";
            case "ε":
                return "vacio";
            case "(": 
               stackParentesis.push(true);
                return "op_(";
            case ")":
                try{
                    stackParentesis.pop();
                }catch(Exception e){
                    valid = false;
                }
                return "op_)";
            case "\"":
                return "op_\"";
            default:
                return token;
        }
    }
    
    public void isExp(){
       
        if(!stackParentesis.isEmpty() && isParentesis){
            valid = false;
        }
    }
}