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
 * @author mzapataj
 */

/* Class containing left and right child of current 
   node and key value*/
class Node 
{ 
    int key;
    ArrayList<Node> aristas = new ArrayList<>();
    Node left, right; 
  
    public Node(int item) 
    { 
        key = item; 
        //left = right = null; 
    } 
}
class Graph{
    ArrayList<Vertex> vertices;
    Vertex puntoPartida;
    Vertex fin;
    ArrayList<Edge> aristas;
    int n;
    int m;

    public void setFin(Vertex fin) {
        this.fin = fin;
    }
    
    public Graph(){
        vertices = new ArrayList<>();
        aristas = new ArrayList<>();
        puntoPartida = new Vertex(0);
    }
    
    public Graph(int inicio){
        vertices = new ArrayList<>();
        aristas = new ArrayList<>();
        puntoPartida = createVertex(inicio);
    }
    public Vertex createVertex(int index){
        Vertex v = new Vertex(index);
        vertices.add(v);
        n++;
        return v;
    }
}

    class Vertex
    {
        int value;
        ArrayList<Edge> aristas;
        List<Vertex> antVertex;
        
        public Vertex(int index){
            value = index;
            aristas = new ArrayList<>();
            antVertex = new LinkedList<>();
        }
        
        public Edge busquedaArista(Vertex v) {
            for (Edge arist : aristas) {
                if (v != null) {
                    if (arist.getDestino().equals(v)) {
                        return arist;
                    }
                } else {
                    if (v == arist.destino) {
                        return arist;
                    }
                }
            }
            return null;
        }
    }


class Edge
{
    String value;
    Vertex destino;
    public Edge(String s){
        value = s;
        destino = null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Vertex getDestino() {
        return destino;
    }

    public void setDestino(Vertex destino) {
        this.destino = destino;
    }
}



  
// A Java program to introduce Binary Tree

//class BinaryTree 
//{ 
//    // Root of Binary Tree 
//    
//    Node root; 
//  
//    // Constructors 
//    BinaryTree(String key) 
//    { 
//        root = new Node(key); 
//    } 
//  
//    BinaryTree() 
//    { 
//        root = null; 
//    } 
//  
//    public static void main(String[] args) 
//    { 
//        BinaryTree tree = new BinaryTree(); 
//  
//        /*create root*/
//        tree.root = new Node("1"); 
//  
//        /* following is the tree after above statement 
//  
//              1 
//            /   \ 
//          null  null     */
//  
//        tree.root.left = new Node("2"); 
//        tree.root.right = new Node("3"); 
//  
//        /* 2 and 3 become left and right children of 1 
//               1 
//             /   \ 
//            2      3 
//          /    \    /  \ 
//        null null null null  */
//  
//  
//        tree.root.left.left = new Node("4"); 
//        /* 4 becomes left child of 2 
//                    1 
//                /       \ 
//               2          3 
//             /   \       /  \ 
//            4    null  null  null 
//           /   \ 
//          null null 
//         */
//       
//    }
//} 
