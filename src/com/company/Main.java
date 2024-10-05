package com.company;

import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
	    /*Sistema s= new Sistema();

        //System.out.println(s.getG().toString());

        Grafo g= s.getG();

        Set<Endereco> set= g.getVertices().keySet();
        Endereco destino= null;
        int i= 1;
        for(Endereco e: set){
            if(i== 5) {
                destino = e;
                break;
            }
            i++;
        }

        System.out.println(destino.toString());

        List<Endereco> caminho= s.depthFirstSearchIterative(destino);

        for(Endereco e: caminho) System.out.println("-> " + e.toString());
        */
        MenuUI menu = new MenuUI();
        menu.run();
    }
}
