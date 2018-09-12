/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author mzapataj
 */
public class Reconocimiento {
    Scanner sc = new Scanner(System.in);
    String ExpRegular;
    Stack stackParentesis = new Stack();
    boolean isParentesis = false;
    boolean valid = true;
    public Reconocimiento(){
        ExpRegular = sc.nextLine();
        ArrayList tokens = new ArrayList();
        System.out.println(ExpRegular);
        
        for(String c : ExpRegular.split("")){
            tokens.add(reconocerToken(c));
        }
        if(valid){
            System.out.println("No es valido");
        }
        
        System.out.println(tokens);
    }
    public String reconocerToken(String token){
        switch(token){
            case "|":
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
                }catch(NullPointerException e){
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
            valid = true;
        }
        
    }
}