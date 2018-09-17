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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.transformation.SortedList;

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
        if (!"(".equals(ExpRegular.substring(0, 1)) || !")".equals(ExpRegular.substring(ExpRegular.length()-1, ExpRegular.length()))) {
            ExpRegular = "(" + ExpRegular;
            ExpRegular = ExpRegular + ")";
        }
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
                OrdenOperaciones();
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
        return result;
    }
    
    public void generateTerms(){
        
        List laux = (List)colaPrioridadIndex.get(0);
        terminos.add(tokens.subList((int)laux.get(0)+1, (int)laux.get(1)));
        List terminosSortedbyLeastIndex = new LinkedList();
        for (Object object : colaPrioridadIndex) {
            List ls = (List)object;
            terminosSortedbyLeastIndex.add(ls);
        }
        SortByLeastIndex(terminosSortedbyLeastIndex, colaPrioridadIndex.size());
        
        //colaPrioridad.add(terminos);
        
        for (int i = 1; i < colaPrioridadIndex.size(); i++) {
            laux = (List)colaPrioridadIndex.get(i);
            int k = 0;
            while (k<i) {                    
            List laux2 = (List)terminosSortedbyLeastIndex.get(k);
            List laux3 = new LinkedList();
            if((int)laux2.get(0)>=(int)laux.get(0) && (int)laux2.get(0)<=(int)laux.get(1)){
                for (int j = (int)laux.get(0); j < (int)laux.get(1); j++) {
                    if ((int)laux2.get(0)==j) {
                        laux3.add("par_"+colaPrioridadIndex.indexOf(laux2));
                        //add("term_"+(k));
                        j = (int)laux2.get(1);
                        laux2 = (List)terminosSortedbyLeastIndex.get(k);
                        k++;
                    }else{
                         laux3.add(tokens.get(j));                      
                        //laux3.add(laux3.get(j));
                    }
                }
                terminos.add(laux3);
            }
            //colaPrioridad.add(laux3);
            k++;
           
            }
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
    
    public void OrdenOperaciones(){
        for (int i = 0; i < colaPrioridadIndex.size(); i++) {
            List laux = (List)colaPrioridadIndex.get(i);
            terminos.add(tokens.subList((int)laux.get(0)+1, (int)laux.get(1)));
        }
        List terminosSortedbyLeastIndex = new LinkedList();
        for (Object object : colaPrioridadIndex) {
            List ls = (List)object;
            terminosSortedbyLeastIndex.add(ls);
        }
        
        SortByLeastIndex(terminosSortedbyLeastIndex, colaPrioridadIndex.size());
        List teraux = new LinkedList();
            
        for (Object object : terminos) {
            List ls = (List)object;
            teraux.add(ls);
        }
        for (int i = 0; i < colaPrioridadIndex.size(); i++) {
            
            List laux = (List) colaPrioridadIndex.get(i);
            List teraux2 = (List) teraux.get(i);
            boolean sw = false;
            
           
            for (int j = 0; j < i; j++) {
                List laux2 = (List)colaPrioridadIndex.get(j);
                List listBuilder = new LinkedList();
                
                if((int)laux2.get(0)>(int)laux.get(0) && (int)laux2.get(0)<(int)laux.get(1)){
                    for (int k = 0; k < teraux2.size(); k++) {
                        if(k == ((int)laux2.get(0)-((int)laux.get(0)+1))){
                            listBuilder.add("parent_"+j);
                            sw = true;
                        
                        }else if(k >((int)laux2.get(0)-((int)laux.get(0)+1)) && k <=((int)laux2.get(1)-((int)laux.get(0)+1))){
                            listBuilder.add("");
                        }
                        else{
                            listBuilder.add(teraux2.get(k));
                        }
                    }
                    teraux2 = listBuilder;
                    
                }
            }
            eliminarElemento(teraux2, "");
            teraux.set(i, teraux2);
           
        } 
        terminos = teraux;
    }
    
    public void eliminarElemento(List ls,String s){
        Iterator<String> iter = ls.iterator();
        
        while (iter.hasNext()) {            
            String str = iter.next();
            if (s.equals(str)) {
                iter.remove();
            }
        }
    }
    /* Function to sort an array using insertion sort*/
    void SortByLeastIndex(final List ls, int n)
    {
    int i, j;
    List key;
    List lx = null;
    for (i = 1; i < n; i++)
       {
       key = (List)ls.get(i);
       j = i-1;
 
       /* Move elements of arr[0..i-1], that are
          greater than key, to one position ahead
          of their current position */
       lx = (List)ls.get(j);
       while (j >= 0 && (int)lx.get(0) < (int)key.get(0))
       {
           ls.set(j+1, lx);
           j = j-1;
           try{
               lx = (List)ls.get(j);
           }catch(Exception e){
               
           }
           
       }
       ls.set(j+1, key);
        
       }
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