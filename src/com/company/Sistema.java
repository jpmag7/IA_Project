package com.company;

import java.util.*;

public class Sistema {

    public static Grafo g;
    private static Endereco greenDistributionLocal;
    private static Map<String, Cliente> clientes= new HashMap<>();
    private static Map<String, Encomenda> encomenda= new HashMap<>();
    private static Map<String, Estafeta> estafetas= new HashMap<>();
    private static Map<String, Entrega> entregas= new HashMap<>();
    private static List<Endereco> destinos = new ArrayList<>();
    private static Map<String,List<Entrega>> circuitos = new HashMap<>();

    public Sistema(){
        estafetas.put("0",new Estafeta("0","Duarte",0,20));
        estafetas.put("1",new Estafeta("1","Pedro",5,20));
        estafetas.put("2",new Estafeta("2","Esquerdo",5,20));
        clientes.put("0",new Cliente("0", "Pedro"));
        clientes.put("1",new Cliente("1","Madeira"));

        constroiGrafo();
    }

    public Grafo getG() {
        return g;
    }

    public void setG(Grafo g) {
        this.g = g;
    }


    public void constroiGrafo(){
        Endereco e1= new Endereco("Canelas", "Travessa do Falcão");
        Endereco e2= new Endereco("Agrela", "Gomes Ferreira");
        Endereco e3= new Endereco("Caniço", "Caminho Velho dos Barreiros");
        Endereco e4= new Endereco("Quinchães", "Rua do Atrasado");
        Endereco e5= new Endereco("Green Distribution", "Rua do Caralho");
        Endereco e6= new Endereco("Cinfães", "Rua Calçada Calvário");
        Endereco e7= new Endereco("Resende", "Rua Doutor Pereira Dias");
        Endereco e8= new Endereco("Santana", "Rua Baltazar Dias");
        Endereco e9= new Endereco("Cervos", "Rua Baixo");
        Endereco e10= new Endereco("Espinho", "Rua da Idanha");

        destinos.add(e1);
        destinos.add(e2);
        destinos.add(e3);
        destinos.add(e4);
        destinos.add(e6);
        destinos.add(e7);
        destinos.add(e8);
        destinos.add(e9);
        destinos.add(e10);

        Map<Endereco, Integer> estimativas = new HashMap<>();

        estimativas.put(e1, 5);
        estimativas.put(e2, 5);
        estimativas.put(e3, 4);
        estimativas.put(e4, 4);
        estimativas.put(e5, 0);
        estimativas.put(e6, 8);
        estimativas.put(e7, 5);
        estimativas.put(e8, 6);
        estimativas.put(e9, 8);
        estimativas.put(e10, 11);

        List<Grafo.Aresta> aresta = new ArrayList<>();

        Grafo.Aresta e1e3= new Grafo.Aresta(e1, e3, 3);
        Grafo.Aresta e3e2= new Grafo.Aresta(e3, e2, 2);
        Grafo.Aresta e2e4= new Grafo.Aresta(e2, e4, 4);
        Grafo.Aresta e4e5= new Grafo.Aresta(e4, e5, 4);
        Grafo.Aresta e5e3= new Grafo.Aresta(e5, e3, 4);
        Grafo.Aresta e6e5= new Grafo.Aresta(e6, e5, 5);
        Grafo.Aresta e7e6= new Grafo.Aresta(e7, e6, 6);
        Grafo.Aresta e6e8= new Grafo.Aresta(e6, e8, 3);
        Grafo.Aresta e7e8= new Grafo.Aresta(e7, e8, 4);
        Grafo.Aresta e8e9= new Grafo.Aresta(e8, e9, 2);
        Grafo.Aresta e9e10= new Grafo.Aresta(e9, e10, 4);


        aresta.add(e1e3);
        aresta.add(e3e2);
        aresta.add(e2e4);
        aresta.add(e4e5);
        aresta.add(e5e3);
        aresta.add(e6e5);
        aresta.add(e7e6);
        aresta.add(e6e8);
        aresta.add(e7e8);
        aresta.add(e8e9);
        aresta.add(e9e10);

        g= new Grafo(estimativas, aresta);
        greenDistributionLocal = e5;
    }

    public List<Endereco> algoritmoAEstrela(Endereco destino, Transporte trans){
        long startTime = System.nanoTime();
        List<Endereco> caminho= new ArrayList<>();
        List<Endereco> visitados= new ArrayList<>();
        List<Struct> faltaVisitar= new ArrayList<>();

        Endereco atual= destino;
        Struct pai = new Struct(0, g.getEstimativas().get(atual), null, atual);
        while(atual != greenDistributionLocal) {
            visitados.add(atual);
            Map<Endereco, Integer> adjacencia = adjacentes(atual);
            for (Map.Entry<Endereco, Integer> entry : adjacencia.entrySet()) {

                if (!visitados.contains(entry.getKey())) {
                    Struct s = new Struct(entry.getValue() + pai.getCustoReal(), g.getEstimativas().get(entry.getKey()), pai, entry.getKey());
                    Struct existente= null;
                    float heuristica= trans == null ? s.getCustoReal() + s.getCustoEstimado() :
                            s.getCustoReal()/trans.getVelocidadeMed() + s.getCustoEstimado()/trans.getVelocidadeMed();
                    int indice= 0;
                    for(Struct st: faltaVisitar){
                        if(st.getAtual() == s.getAtual()){
                            existente= st;
                            break;
                        }
                        indice++;
                    }
                    float oldHeuristica = 0;
                    if(existente != null) oldHeuristica = trans == null ? existente.getCustoEstimado()+ existente.getCustoReal() :
                            existente.getCustoEstimado()/trans.getVelocidadeMed() + existente.getCustoReal() / trans.getVelocidadeMed();
                    if(existente!= null && heuristica< oldHeuristica){
                        faltaVisitar.remove(indice);
                        faltaVisitar.add(s);
                    }
                    else if(existente== null)  faltaVisitar.add(s);
                }
            }

            float menor = -1;
            Struct menorzita = null;
            for (Struct s : faltaVisitar) {
                float heuristica = trans == null ? s.getCustoEstimado() + s.getCustoReal() :
                        s.getCustoEstimado()/trans.getVelocidadeMed() + s.getCustoReal()/trans.getVelocidadeMed();
                if (heuristica < menor || menor == -1) {
                    menor = heuristica;
                    menorzita = s;
                }
            }

            faltaVisitar.remove(menorzita);
            atual = menorzita.getAtual();
            pai= menorzita;
        }

        while(pai.getAtual() != destino){
            caminho.add(pai.getAtual());
            pai= pai.getPai();
        }

        caminho.add(destino);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Tempo de execução do algoritmo: " + duration + " nanossegundos!");
        return caminho;
    }



    public static Map<Endereco, Integer> adjacentes(Endereco v){

        Map<Endereco, Integer> adjacencia= new HashMap<>();
        for(Grafo.Aresta a: g.getArestas()){
            if(v.getFreguesia().equals(a.getOrigem().getFreguesia())) adjacencia.put(a.getDestino(), a.getDist());
            else if(v.getFreguesia().equals(a.getDestino().getFreguesia())) adjacencia.put(a.getOrigem(), a.getDist());
        }

        return adjacencia;
    }

    public List<Endereco> gulosa(Endereco destino, Transporte trans){
        long startTime = System.nanoTime();
        List<Endereco> caminho= new ArrayList<>();
        List<Endereco> visitados= new ArrayList<>();
        List<Struct> faltaVisitar= new ArrayList<>();

        Endereco atual= destino;
        Struct pai= new Struct(0, g.getEstimativas().get(atual), null, atual);
        while(atual!= greenDistributionLocal) {
            visitados.add(atual);
            Map<Endereco, Integer> adjacencia = adjacentes(atual);
            for (Map.Entry<Endereco, Integer> entry : adjacencia.entrySet()) {
                if (!visitados.contains(entry.getKey())) {
                    Struct s = new Struct(entry.getValue() + pai.getCustoReal(), g.getEstimativas().get(entry.getKey()), pai, entry.getKey());

                    Struct existente = null;
                    float heuristica = trans == null ? s.getCustoEstimado() : s.getCustoEstimado() / trans.getVelocidadeMed();
                    int indice= 0;

                    for(Struct st: faltaVisitar){
                        if(st.getAtual() == s.getAtual()){
                            existente= st;
                            break;
                        }
                        indice++;
                    }
                    float oldHeuristica = 0;
                    if(existente != null) oldHeuristica = trans == null ? existente.getCustoEstimado() : existente.getCustoEstimado() / trans.getVelocidadeMed();
                    if(existente != null && heuristica < oldHeuristica){
                        faltaVisitar.remove(indice);
                        faltaVisitar.add(s);
                    }
                    else if(existente == null)  faltaVisitar.add(s);
                }
            }

            float menor = -1;
            Struct menorzita = null;
            for (Struct s : faltaVisitar) {
                float heuristica = trans == null ? s.getCustoEstimado() : s.getCustoEstimado() / trans.getVelocidadeMed();
                if (heuristica < menor || menor == -1) {
                    menor = heuristica;
                    menorzita = s;
                }
            }
            faltaVisitar.remove(menorzita);
            atual = menorzita.getAtual();
            pai= menorzita;
        }

        while(pai.getAtual() != destino){
            caminho.add(pai.getAtual());
            pai= pai.getPai();
        }
        caminho.add(destino);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Tempo de execução do algoritmo: " + duration + " nanossegundos!");
        return caminho;
    }

    public List<Endereco> depthFirstSearchIterative(Endereco v){
        long startTime = System.nanoTime();
        int profundidade= 0;
        List<Endereco> visitados= new ArrayList<>();
        List<Endereco> caminho= new ArrayList<>();
        while(!depthFirstSearchIterativeAux(greenDistributionLocal, v, visitados, caminho, profundidade)){
            visitados.clear();
            caminho.clear();
            profundidade++;
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Tempo de execução do algoritmo: " + duration + " nanossegundos!");
        return caminho;
    }

    public static boolean depthFirstSearchIterativeAux(Endereco origem, Endereco v, List<Endereco> visitados, List<Endereco> caminho, int profundidade){

        caminho.add(origem);
        visitados.add(origem);
        if(origem == v) return true;
        if(profundidade == 0) return false;

        Map<Endereco, Integer> adjacencia= adjacentes(origem);

        for(Map.Entry <Endereco, Integer> entry: adjacencia.entrySet()){
                if(!visitados.contains(entry.getKey())) {
                    if(depthFirstSearchIterativeAux(entry.getKey(), v, visitados, caminho, --profundidade)) return true;
                }
        }

        return false;
    }




    public List<Endereco> depthFirstSearch(Endereco v){
        long startTime = System.nanoTime();
        List<Endereco> visitados= new ArrayList<>();
        List<Endereco> caminho= new ArrayList<>();
        depthFirstSearchAux(greenDistributionLocal, v, visitados, caminho);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Tempo de execução do algoritmo: " + duration + " nanossegundos!");
        return caminho;
    }

    public static boolean depthFirstSearchAux(Endereco origem, Endereco v, List<Endereco> visitados, List<Endereco> caminho){
        caminho.add(origem);
        visitados.add(origem);
        if(origem== v) return true;

        Map<Endereco, Integer> adjacencia= adjacentes(origem);

        for(Map.Entry <Endereco, Integer> entry: adjacencia.entrySet()){
            if(!visitados.contains(entry.getKey())) {
                if(depthFirstSearchAux(entry.getKey(), v, visitados, caminho)) return true;
            }
        }
        return false;
    }


    public static List<Endereco> breathFirstSearch(Endereco v) {
        long startTime = System.nanoTime();
        List<Endereco> caminho = new ArrayList<>();
        Endereco atual = greenDistributionLocal;


        if(v == atual) {
            caminho.add(atual);
            return caminho;
        }

        Map<Endereco,Endereco> caminhoAux = new HashMap<>();
        List<Endereco> adjacentes = new ArrayList<>();
        int count = 0;

        while(v != atual) {
            Map<Endereco,Integer> adj = adjacentes(atual);
            for(Map.Entry<Endereco, Integer> entry : adj.entrySet()) {
                if(! adjacentes.contains(entry.getKey())) {
                    adjacentes.add(entry.getKey());
                    caminhoAux.put(entry.getKey(),atual);
                }
            }
            atual = adjacentes.get(count);
            count++;
        }

        while(atual != greenDistributionLocal) {
            caminho.add(atual);
            atual = caminhoAux.get(atual);
        }
        caminho.add(atual);
        Collections.reverse(caminho);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Tempo de execução do algoritmo: " + duration + " nanossegundos!");
        return caminho;
    }

    public static List<List<List<Endereco>>> geraCircuitos(List<Endereco> list){
        List<List<List<Endereco>>> retList = new ArrayList<>();
        for(Endereco e : list) retList.add(geraCircuito(e));
        return retList;
    }

    private static List<List<Endereco>> geraCircuito(Endereco d){
        List<Endereco> visitados = new ArrayList<>();
        List<Endereco> caminho = new ArrayList<>();
        List<List<Endereco>> circuito = new ArrayList<>();

        circuitoDepthFirst(greenDistributionLocal, d, visitados, caminho, circuito);

        return circuito;
    }

    private static void circuitoDepthFirst(Endereco origem, Endereco d, List<Endereco> visitados, List<Endereco> caminho, List<List<Endereco>> caminhos){
        caminho.add(origem);
        Map<Endereco, Integer> adjacencia = adjacentes(origem);
        if(origem == d){
            caminhos.add(new ArrayList<>(caminho));
            caminho.remove(caminho.size() - 1);
            return;
        }else visitados.add(origem);

        for(Map.Entry <Endereco, Integer> entry: adjacencia.entrySet()){
            if(!visitados.contains(entry.getKey())) {
                circuitoDepthFirst(entry.getKey(), d, visitados, caminho, caminhos);
            }
        }
        caminho.remove(caminho.size() - 1);
        visitados.remove(visitados.indexOf(origem));
    }

    public static Endereco getGreenDistributionLocal() {
        return greenDistributionLocal;
    }

    public static void setGreenDistributionLocal(Endereco greenDistributionLocal) {
        Sistema.greenDistributionLocal = greenDistributionLocal;
    }

    public static Map<String, Cliente> getClientes() {
        return clientes;
    }

    public static void setClientes(Map<String, Cliente> clientes) {
        Sistema.clientes = clientes;
    }

    public static Map<String, Encomenda> getEncomenda() {
        return encomenda;
    }

    public static void setEncomenda(Map<String, Encomenda> encomenda) {
        Sistema.encomenda = encomenda;
    }

    public static Map<String, Estafeta> getEstafetas() {
        return estafetas;
    }

    public static void setEstafetas(Map<String, Estafeta> estafetas) {
        Sistema.estafetas = estafetas;
    }

    public static Map<String, Entrega> getEntregas() {
        return entregas;
    }

    public static void setEntregas(Map<String, Entrega> entregas) {
        Sistema.entregas = entregas;
    }

    public static List<Endereco> getDestinos() {
        return destinos;
    }

    public static void setDestinos(List<Endereco> destinos) {
        Sistema.destinos = destinos;
    }

    public static Map<String, List<Entrega>> getCircuitos() {
        return circuitos;
    }

    public static void setCircuitos(Map<String, List<Entrega>> circuitos) {
        Sistema.circuitos = circuitos;
    }

    public boolean haEncomendas(){
        return !getEncomenda().isEmpty();
    }

    public boolean haEntregas(){
        return !getEntregas().isEmpty();
    }

    public boolean encomendaValida() {
        if(getEncomenda().isEmpty()) return false;
        int count = 0;
        for(Encomenda encomenda : encomenda.values()) {
            if(!encomenda.getCaminho().isEmpty()) count++;
        }
        if(count != 0) return true;
        else return false;
    }
    public boolean haCircuitos() { return !circuitos.isEmpty(); }


    public class Struct{
        private int custoReal;
        private int custoEstimado;
        private Struct pai;
        private Endereco atual;

        public Struct(int custoReal, int custoEstimado, Struct pai, Endereco atual) {
            this.custoReal = custoReal;
            this.custoEstimado = custoEstimado;
            this.pai = pai;
            this.atual = atual;
        }

        public int getCustoReal() {
            return custoReal;
        }

        public void setCustoReal(int custoReal) {
            this.custoReal = custoReal;
        }

        public int getCustoEstimado() {
            return custoEstimado;
        }

        public void setCustoEstimado(int custoEstimado) {
            this.custoEstimado = custoEstimado;
        }

        public Struct getPai() {
            return pai;
        }

        public void setPai(Struct pai) {
            this.pai = pai;
        }

        public Endereco getAtual() {
            return atual;
        }

        public void setAtual(Endereco atual) {
            this.atual = atual;
        }


    }



}
