package com.company;

import java.util.List;

public class Entrega {
    private String id;
    private List<Endereco> caminho;
    private Encomenda encomenda;
    private float tempoEntrega;
    private int distancia;

    public Entrega(String id, List<Endereco> caminho, Encomenda encomenda, float tempoEntrega, int distancia) {
        this.id = id;
        this.caminho = caminho;
        this.encomenda = encomenda;
        this.tempoEntrega = tempoEntrega;
        this.distancia = distancia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Endereco> getCaminho() {
        return caminho;
    }

    public void setCaminho(List<Endereco> caminho) {
        this.caminho = caminho;
    }

    public Encomenda getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(Encomenda encomenda) {
        this.encomenda = encomenda;
    }

    public float getTempoEntrega() {
        return tempoEntrega;
    }

    public void setTempoEntrega(float tempoEntrega) {
        this.tempoEntrega = tempoEntrega;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    @Override
    public String toString() {
        return "Entrega{" +
                "id='" + id + '\'' +
                ", caminho=" + caminho +
                ", encomenda=" + encomenda +
                ", tempoEntrega=" + tempoEntrega +
                ", distancia=" + distancia +
                '}';
    }
}
