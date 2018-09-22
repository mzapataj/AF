/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Jorge
 */
public class Thompson {
    Reconocimiento reconocimiento = new Reconocimiento();
    ArrayList<List> listaTransicion = new ArrayList();
    ArrayList<Graph> subgrafos = new ArrayList<>();
    
    ArrayList<List> matrizTransicion = new ArrayList<>();
    
    Graph afn;
    Vertex currentVertex; 
    Vertex antCurrentVertex; 
    
    
    public Thompson(){
        afn = new Graph();
        currentVertex = afn.puntoPartida;
    }
    /*
    public void calculateAFN(){
         for ( Object obj : reconocimiento.terminos) {
            
            List term = (List) obj;
            int n = term.size();
           
            for (int i = 0; i < n; i++) {
                String s = (String)term.get(i);
                switch(s){
                    case("op_|"):
                   
                    break;
                    case("op_*"):
                        
                    break;
                    case("op_+"):
                    break;
                    case("op_?"):
                        
                    break;
                    default:
                        concat(s);
                }
            }
        }
    }
    public void union(){
        
    }
    public void concat(String s){
              
    }
    */
   
     
    
    
    
    public void calculateAFN(){
        
        String ants = "";
        for ( Object obj : reconocimiento.terminos) {
            
            List term = (List) obj;
            int n = term.size();
            Graph subgraph = new Graph(afn.n);
            currentVertex = subgraph.puntoPartida;
            afn.n++;
            
            String op_ant = "";
            for (int i = 0; i < n; i++) {
                String s = (String)term.get(i);
                if (!s.equals(ants) || !s.substring(0, 6).equals("parent")) {
                    switch (s) {
                        case ("op_|"):
                            Union(subgraph, term, i);
                            op_ant = "op_|";
                            i++;

                            break;
                        case ("op_*"):
                            CerraduraKleene(s);
                            break;
                        case ("op_+"):
                            CerraduraPositiva(s);
                            break;
                        case ("op_?"):

                            break;
                        default:
                            if (op_ant.equals("op_|")) {
                                /*    currentVertex.aristas.get(1).setDestino(currentVertex);
                            
                            op_ant = "";
                                 */
                            }
                            Concatenacion(s);
                    }
                }
                
                ants = s;
            }
            subgraph.fin = currentVertex;
            subgrafos.add(subgraph);
        }
    }
    
  /*
    a | b
    */
    private void Union(Graph g, List term, int i){
        //a
        Vertex subinicio = afn.createVertex(afn.n,1);
        Vertex subfin = afn.createVertex(afn.n,1);

        Edge aristaInicio1 = new Edge("∈");
        Edge aristaInicio2 = new Edge("∈");
        Edge aristaFin1 = new Edge("∈");
        Edge aristaFin2 = new Edge("∈");
        
        
        subinicio.aristas.add(aristaInicio1);
        subinicio.aristas.add(aristaInicio2);
        
        currentVertex.aristas.add(aristaFin1);
        aristaFin1.setDestino(subfin);
        
        
        if (antCurrentVertex.type == 0) {
        
            //b
            String b = (String) term.get(i + 1);
            
            if (antCurrentVertex==g.puntoPartida) {
                g.puntoPartida = subinicio;
            }else{
                for (Vertex elem : antCurrentVertex.antVertex){
                    
                    for(Edge edg : elem.aristas){
                        if (!"∈".equals(elem.aristas.get(elem.aristas.indexOf(edg)).value)) {
                            edg.setDestino(subinicio);
                        }
                    }
                }
            }
            
            aristaInicio1.setDestino(antCurrentVertex);
            
            if (b.length() == 1) {
                currentVertex = afn.createVertex(afn.n, 0);
                aristaInicio2.setDestino(currentVertex);
                Concatenacion(b);
                currentVertex.aristas.add(aristaFin2);
                aristaFin2.setDestino(subfin);
            }else{
                String c = b.substring(7);
                int index = Integer.valueOf(c);
                Graph parent_index = subgrafos.get(index);
                antCurrentVertex = parent_index.puntoPartida;
                currentVertex = parent_index.fin; 
                aristaInicio2.setDestino(antCurrentVertex);
                currentVertex.aristas.add(aristaFin2);
                aristaFin2.setDestino(subfin);
            }
            currentVertex = subfin;
            antCurrentVertex = subinicio;
                
        }
        
    }
    
    private void CerraduraPositiva(String s){
        
    }
    
    private void CerraduraKleene(String s){
        CerraduraPositiva(s);
    }
    
    private void Concatenacion(String s){
        if (s.length() == 1) {
            Edge arista = new Edge(s);
            currentVertex.aristas.add(arista);
            Vertex antV = currentVertex;
            currentVertex = new Vertex(afn.n, 0);
            afn.n++;
            arista.setDestino(currentVertex);
            currentVertex.antVertex.add(antV);
            antCurrentVertex = antV;
        }else{
            String c = s.substring(7);
            int index = Integer.valueOf(c);
            Graph g = subgrafos.get(index);
            List aristAnt = new LinkedList();
            for (Vertex v : currentVertex.antVertex) {
                v.busquedaArista(currentVertex).setDestino(g.puntoPartida);
            }
            afn.n--;
            Vertex antCVaux = antCurrentVertex;
            antCurrentVertex = g.puntoPartida;
            
            antCurrentVertex.antVertex.clear();
            antCurrentVertex.antVertex.add(antCVaux);
            
            
            currentVertex = g.fin;                
        }
    }
}
