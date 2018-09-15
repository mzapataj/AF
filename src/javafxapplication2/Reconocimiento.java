/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
    ArrayList tokens = new ArrayList();
    Stack stackParentesis = new Stack();
    List colaPrioridadIndex = new LinkedList();
    List colaPrioridad = new LinkedList();
    Stack indexParIzq;
    Stack indexParDer;
    List<Object> terminos = new LinkedList<>();
    boolean isParentesis = false;
    boolean valid = true;
    BinaryTree arbolSintax;
    
    public Reconocimiento(){
        ExpRegular = sc.nextLine();
        String tokenAnterior = "";
        
        for(String c : ExpRegular.split("")){
            tokens.add(reconocerToken(c,tokenAnterior));
            tokenAnterior = c;
        }
        if(!valid){
            System.out.println("No es valido");
        }else{
            try{
                arbolSintax = new BinaryTree();
                indexParIzq = new Stack();
                indexParDer = new Stack();
                colaPrioridadIndex = getIndexOfParentheses();
                System.out.println("Division de parentesis");
                System.out.println(colaPrioridadIndex);
                generateTerms();
                System.out.println(terminos);
            }catch(Exception e){
                e.printStackTrace();
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
                return "ε";
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
    
    public List getIndexOfParentheses(){
        List<List> result = new LinkedList();
        
        int li = 0;
        int ls = tokens.size()-1;
        List aux = new LinkedList();
        while (li<=ls) {            
            if(tokens.get(li).equals("op_(")){
                indexParIzq.push(li);
            }
            if(tokens.get(li).equals("op_)")){
                aux = new LinkedList();
                aux.add(indexParIzq.pop());
                aux.add(li);
                result.add(aux);
            }
            li++;
        }
        aux = new LinkedList();
        aux.add(0);
        aux.add(tokens.size()-1);
        result.add(aux);
        return result;
    }
    public void generateTerms(){
        
        List laux = (List)colaPrioridadIndex.get(0);
        terminos.add(tokens.subList((int)laux.get(0)+1, (int)laux.get(1)));
        //colaPrioridad.add(terminos);
        
        for (int i = 1; i < colaPrioridadIndex.size(); i++) {
            laux = (List)colaPrioridadIndex.get(i);
            List laux2 = (List)colaPrioridadIndex.get(i-1);
            List laux3 = new LinkedList();
            
            for (int j = (int)laux.get(0); j < (int)laux.get(1); j++) {
                if ((int)laux2.get(0)==j) {
                    laux3.add("term_"+(i-1));
                    j = (int)laux2.get(1);
                }else{
                    laux3.add(tokens.get(j));
                }
            }
            terminos.add(laux3);
            //colaPrioridad.add(laux3);
        }
        /*for (int j = 0; j < 10; j++) {
            int indexSubList = findArray(tokens, (List)terminos.get(terminos.size()-1));
            List auxlist =(List)colaPrioridad.get(colaPrioridad.size()-1);
            colaPrioridad.remove(colaPrioridad.size()-1);
            List newlist = new LinkedList();
            for (int i = 0; i < auxlist.size(); i++) {
                if (i==indexSubList) {
                newlist.add("term_"+(terminos.size()-1));
                List templist= (List)terminos.get(terminos.size()-1);
                i=i+templist.size();
            }
                newlist.add(auxlist.get(i));
            }
              
            colaPrioridad.add(newlist);
        }
        */
    }
    void buildSintaxTree(){
        for (int i = indexParIzq.size()-1; i >= 0; i--) {
            terminos.add(tokens.subList((int)indexParIzq.get(i)+1, (int)indexParDer.get(i)));
            colaPrioridad.add(terminos.get(indexParIzq.size()-1-i));
        }
        colaPrioridad.add(tokens);
        
        int n = indexParIzq.size()-1;
        Node ptr = new Node(null);
        for (int i = 0; i < n; i++) {
            LinkedList aux = (LinkedList) colaPrioridad.get(i);
            for (int j = 0; j < aux.size(); j++) {
                String cadaux = (String) aux.get(j);
                /*if(cadaux.length() >1){
                    
                }
                */
                ptr = arbolSintax.root;
                arbolSintax.root = new Node(cadaux); 
                arbolSintax.root.left = ptr;
            }
        }
        
        
    }
    public static int findArray(List array, List subArray)
    {
        return Collections.indexOfSubList((List)array,(List)subArray);
    }

    public void isExp(){
       
        if(!stackParentesis.isEmpty() && isParentesis){
            valid = false;
        }
    }
}