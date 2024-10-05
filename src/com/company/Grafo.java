package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grafo {

    public static class Aresta{
        private Endereco origem;
        private Endereco destino;
        private int dist;

        public Aresta(Endereco origem, Endereco destino, int dist) {
            this.origem = origem;
            this.destino = destino;
            this.dist = dist;
        }

        public Endereco getOrigem() {
            return origem;
        }

        public void setOrigem(Endereco origem) {
            this.origem = origem;
        }

        public Endereco getDestino() {
            return destino;
        }

        public void setDestino(Endereco destino) {
            this.destino = destino;
        }

        public int getDist() {
            return dist;
        }

        public void setDist(int dist) {
            this.dist = dist;
        }

        @Override
        public String toString() {
            return "Aresta:{ (origem) " + origem + "-> (destino) " + destino + ";     distancia= " + dist + " }";
        }
    }


    private Map<Endereco, Integer> estimativas = new HashMap<>();
    private List<Aresta> arestas= new ArrayList<>();

    public Grafo(Map<Endereco, Integer> estimativas, List<Aresta> arestas){
        this.estimativas= estimativas;
        this.arestas= arestas;
    }

    public Map<Endereco, Integer> getEstimativas() {
        return estimativas;
    }

    public void setEstimativas(Map<Endereco, Integer> estimativas) {
        this.estimativas = estimativas;
    }

    public List<Aresta> getArestas() {
        return arestas;
    }

    public void setArestas(List<Aresta> arestas) {
        this.arestas = arestas;
    }

    @Override
    public String toString() {
        String result = "";
        result+= "     -> Lista de Custos Estimados:";

        for(Map.Entry <Endereco, Integer> entry1: estimativas.entrySet()){
            result+= "\n           Nodo destino: " + entry1.getKey().toString() + "; Custo aproximado: " + entry1.getValue();
        }

        result+= "\n\nLista de Arestas:";
        for(Aresta a: this.arestas){
            result+= "\n   " + a.toString();
        }

        return result;
    }
}