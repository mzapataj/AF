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
    List colaPrioridad = new LinkedList();
    ArrayList<Object> indexParIzq;
    ArrayList<Object> indexParDer;
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
                indexParIzq = new ArrayList<>();
                indexParDer = new ArrayList<>();
                getSubstringInParentheses();
                System.out.println("Division de parentesis");
                System.out.println(indexParDer);
                System.out.println(indexParIzq);
                for (int i = indexParDer.size()-1; i >= 0; i--) {
                    terminos.add(tokens.subList((int)indexParDer.get(i)+1, (int)indexParIzq.get(i)));
                    colaPrioridad.add(tokens.subList((int)indexParDer.get(i)+1, (int)indexParIzq.get(i)));
                }
              
              colaPrioridad.add(tokens);
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
                System.out.println(colaPrioridad);
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
    public void getSubstringInParentheses(){
        int li = 0;
        int ls = tokens.size()-1;
        
        while (li<=ls) {            
            if(tokens.get(li).equals("op_(")){
                indexParDer.add(li);
            }
            if(tokens.get(ls).equals("op_)")){
               indexParIzq.add(ls);
            }
            li++;
            ls--;
        }
    }
    public void generateTerms(){
        for (int j = 0; j < 10; j++) {
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
        
    }
    void buildSintaxTree(){
        for (int i = indexParDer.size()-1; i >= 0; i--) {
            terminos.add(tokens.subList((int)indexParDer.get(i)+1, (int)indexParIzq.get(i)));
            colaPrioridad.add(terminos.get(indexParDer.size()-1-i));
        }
        colaPrioridad.add(tokens);
        
        int n = indexParDer.size()-1;
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