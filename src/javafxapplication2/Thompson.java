/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jorge
 */
public class Thompson {
    Reconocimiento reconocimiento = new Reconocimiento();
    ArrayList<List> listaTransicion = new ArrayList();
    ArrayList<Graph> subgrafos = new ArrayList<>();
    
    Graph afn;
    Vertex currentVertex; 
    
    
    public Thompson(){
        afn = new Graph();
        currentVertex = afn.puntoPartida;
    }
    
    public void calculateAFN(){
        for ( Object obj : reconocimiento.terminos) {
            
            List term = (List) obj;
            int n = term.size();
            Graph subgraph = new Graph(afn.n);
            currentVertex = subgraph.puntoPartida;
            afn.n++;
            
            
            String op_ant = "";
            for (int i = 0; i < n; i++) {
                String s = (String)term.get(i);
                switch(s){
                    case("op_|"):
                        String ants = (String)term.get(i-1);
                        if (ants.length()==1) {
                            Union(subgraph);
                            op_ant = "op_|";
                        }else{
                            
                        }
                        
                    break;
                    case("op_*"):
                        CerraduraKleene(s);
                    break;
                    case("op_+"):
                        CerraduraPositiva(s);
                    break;
                    case("op_?"):
                        
                    break;
                    default:
                        if (op_ant.equals("op_|")) {
                            Edge edg = currentVertex.busquedaArista(null);
                            currentVertex = new Vertex(afn.n);
                            afn.n++;
                            edg.setDestino(currentVertex);
                            op_ant = "";
                        }
                        if (s.length() == 1) {
                            Concatenacion(s);
                        } else {
                            String c = s.substring(7);
                            int index = Integer.valueOf(c);
                            Concatenacion(subgrafos.get(index));
                        }
                }
            }
            subgraph.fin = currentVertex;
            subgrafos.add(subgraph);
        }
    }
    
    private void Union(Graph g){
        
        Vertex subinicio = afn.createVertex(afn.n);
        Vertex subfin = afn.createVertex(afn.n);
        
        Edge aristaAnt = currentVertex.antVertex.busquedaArista(currentVertex);
        Edge arista1 = new Edge("∈");
        Edge arista2 = new Edge("∈");
        
        aristaAnt.setDestino(subinicio);
        
        subinicio.aristas.add(arista1);
        subinicio.aristas.add(arista2);
        arista1.setDestino(currentVertex.antVertex);
        currentVertex = subinicio;
        g.puntoPartida = subinicio;
        
        
    }
    
    private void CerraduraPositiva(String s){
        
    }
    
    private void CerraduraKleene(String s){
        CerraduraPositiva(s);
    }
    
    private void Concatenacion(String s){
       Edge arista = new Edge(s);
       currentVertex.aristas.add(arista);
       Vertex antV = currentVertex;
       currentVertex = new Vertex(afn.n);
       afn.n++;
       arista.setDestino(currentVertex);
       currentVertex.antVertex = antV;
    }
    
    public void Concatenacion(Graph g){
        Edge edg = currentVertex.antVertex.busquedaArista(currentVertex);
        edg.setDestino(g.puntoPartida);
        afn.n--;
        currentVertex = g.fin;
    }
}
