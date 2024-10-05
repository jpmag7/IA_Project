package com.company;

import java.util.ArrayList;
import java.util.List;

public class Encomenda{
    private String idEncomenda;
    private float peso;
    private float volume;
    private Transporte trans;
    private Cliente cliente;
    private Estafeta estafeta;
    private float preco;
    private float prazo;
    private Endereco endereco;
    private List<Endereco> caminho;
    private int distancia;


    public Encomenda(String idEncomenda, float peso, float volume, Cliente cliente, Estafeta estafeta, float preco, float prazo, Endereco endereco) {
        this.idEncomenda = idEncomenda;
        this.peso = peso;
        this.volume = volume;
        // this.trans = trans;
        this.cliente = cliente;
        this.estafeta = estafeta;
        this.preco = preco;
        this.prazo = prazo;
        this.endereco= endereco;
        this.caminho= new ArrayList<>();
        this.distancia= -1;
    }

    public void setTransporteMaisAdequado() {
        float velBicicleta = this.peso <= Transporte.MAX_PESO_BICICLETA ? Transporte.VEL_BICICLETA : -1;
        float velCarro = this.peso <= Transporte.MAX_PESO_CARRO ? Transporte.VEL_CARRO : -1;
        float velMota = this.peso <= Transporte.MAX_PESO_MOTO ? Transporte.VEL_MOTO : -1;

        for(int p = 1; velBicicleta >= 0 && p < peso; p++) velBicicleta -= 0.7f;
        for(int p = 1; velCarro >= 0 && p < peso; p++) velCarro -= 0.1f;
        for(int p = 1; velMota >= 0 && p < peso; p++) velMota -= 0.5f;

        float tempBicicleta = velBicicleta > 0 ? velBicicleta : -1;
        float tempCarro = velCarro > 0 ? velCarro : -1;
        float tempMota = velMota > 0 ? velMota : -1;

        if(tempBicicleta >= 0 && tempBicicleta < prazo)
            this.trans = new Transporte(Transporte.BICICLETA, velBicicleta);
        else if(tempCarro >= 0 && tempCarro < prazo)
            this.trans = new Transporte(Transporte.CARRO, velCarro);
        else this.trans = new Transporte(Transporte.MOTO, velMota);
    }

    public void setVelocidadeMedia(){
        double fator= 0;
        //BICICLETA
        if(this.trans.getTipo()== 0){
            fator= this.peso * 0.7;
            this.trans.setVelocidadeMed((float) (Transporte.BICICLETA- fator));
        }
        //CARRO
        else if(this.trans.getTipo()== 1){
            fator= this.peso * 0.1;
            this.trans.setVelocidadeMed((float) (Transporte.CARRO- fator));
        }
        //MOTO
        else{
            fator= this.peso * 0.5;
            this.trans.setVelocidadeMed((float) (Transporte.MOTO- fator));}
    }

    public void calculaDistancia(){
        distancia = 0;
        for(int i = 0; i < caminho.size() - 1; i++)
            distancia += calcDist(caminho.get(i), caminho.get(i + 1));
    }

    public static int calcDist(Endereco e1, Endereco e2){
        Grafo g = Sistema.g;
        for(Grafo.Aresta a : g.getArestas()){
            if((a.getOrigem().getRua().equals(e1.getRua()) && a.getDestino().getRua().equals(e2.getRua())) ||
                    (a.getDestino().getRua().equals(e1.getRua()) && a.getOrigem().getRua().equals(e2.getRua())))
                return a.getDist();
        }
        return 0;
    }

    public String getIdEncomenda() {
        return idEncomenda;
    }

    public void setIdEncomenda(String idEncomenda) {
        this.idEncomenda = idEncomenda;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public Transporte getTrans() {
        return trans;
    }

    public void setTrans(Transporte trans) {
        this.trans = trans;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Estafeta getEstafeta() {
        return estafeta;
    }

    public void setEstafeta(Estafeta estafeta) {
        this.estafeta = estafeta;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public float getPrazo() {
        return prazo;
    }

    public void setPrazo(float prazo) {
        this.prazo = prazo;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<Endereco> getCaminho() {
        return caminho;
    }

    public void setCaminho(List<Endereco> caminho) {
        this.caminho = caminho;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    @Override
    public String toString() {
        return "Encomenda{" +
                "idEncomenda=" + idEncomenda +
                ", peso=" + peso +
                ", volume=" + volume +
                ", trans=" + trans +
                ", cliente=" + cliente +
                ", estafeta=" + estafeta +
                ", preco=" + preco +
                ", prazo=" + prazo +
                ", endereco=" + endereco +
                '}';
    }
}