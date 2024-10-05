package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Estafeta {
    private String id;
    private String nome;
    private float classificacao; //produtividade
    private int entregas= 20;
    private Map<Entrega,Integer> entregasFeitas;

    public Estafeta(String id, String nome, float classificacao, int entregas) {
        this.id = id;
        this.nome = nome;
        this.classificacao = classificacao;
        this.entregas = entregas;
        this.entregasFeitas= new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(float classificacao) {
        this.classificacao = classificacao;
    }

    public int getEntregas() {
        return entregas;
    }

    public void setEntregas(int entregas) {
        this.entregas = entregas;
    }

    public Map<Entrega,Integer> getEntregasFeitas() {
        return entregasFeitas;
    }

    public void setEntregasFeitas(Map<Entrega,Integer> entregasFeitas) {
        this.entregasFeitas = entregasFeitas;
    }

    @Override
    public String toString() {
        return "Estafeta{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", classificacao=" + classificacao +
                ", entregas=" + entregas +
                '}';
    }
}
