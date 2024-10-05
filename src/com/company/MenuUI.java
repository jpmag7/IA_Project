package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class MenuUI {
    private Scanner sc;
    private Sistema sistema;
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_YELLOW = "\u001B[33m";

    // Construtor vazio
    public MenuUI() {
        this.sc = new Scanner(System.in);
        this.sistema = new Sistema();
    }


    // Executa o menu principal e invoca o método correspondente à opção seleccionada.
    public void run() {
        System.out.println("Bem vindo ao Sistema de Encomendas!");
        this.menu();
        System.out.println("Até breve...");
    }


    // <------------------- AUTENTICAÇÃO --------------------->

    // Menu de autenticação
    private void menu(){
        Menu menu = new Menu(new String[]{
                "Mostrar Grafo",
                "Criar uma Encomenda",
                "Ver uma Encomenda",
                "Fazer Entrega",
                "Ver uma Entrega",
                "Gerar um Caminho de uma Encomenda",
                "Gerar Circuitos",
                "Circuitos com Maior Número de Entregas",
                "Comparar Circuitos",
        });

        // Registar pré-condições das transições
        menu.setPreCondition(3, ()->this.sistema.haEncomendas());
        menu.setPreCondition(4, ()->this.sistema.encomendaValida());
        menu.setPreCondition(5, ()->this.sistema.haEntregas());
        menu.setPreCondition(6, ()->this.sistema.haEncomendas());
        menu.setPreCondition(8, ()->this.sistema.haCircuitos());
        menu.setPreCondition(9, ()->this.sistema.haCircuitos());


        // Registar os handlers das transições
        menu.setHandler(1, ()->mostrarGrafo());
        menu.setHandler(2, ()->criarEncomenda());
        menu.setHandler(3, ()->verEncomenda());
        menu.setHandler(4, ()->fazerEntrega());
        menu.setHandler(5, ()->verEntrega());
        menu.setHandler(6, ()->geraCaminho());
        menu.setHandler(7, ()->gerarCircuitos());
        menu.setHandler(8, ()->circuitosMaisUsados());
        menu.setHandler(9, ()->compararCircuitos());
        menu.run();
    }


    // Funções dos handlers
    public void mostrarGrafo() {
        System.out.println(sistema.getG().toString());
    }

    public void criarEncomenda() {
        String id = System.currentTimeMillis() + "";
        System.out.println("Introduza o peso da encomenda: ");
        float peso = getFloat();
        System.out.println("Introduza o volume da encomenda: ");
        float volume = getFloat();
        System.out.println("Introduza o id do cliente: ");
        String idCliente = getLine();
        if(! sistema.getClientes().containsKey(idCliente)) {
            System.out.println("O id do cliente não existe, cria um cliente novo: ");
            System.out.println("Introduza o nome do cliente: ");
            String nomeCliente = getLine();
            Cliente cliente = new Cliente(idCliente, nomeCliente);
            sistema.getClientes().put(idCliente, cliente);
        }
        System.out.println("Introduza o prazo da encomenda: ");
        long prazo = getLong();
        System.out.println("Introduza o preço da encomenda: ");
        float preco = getFloat();
        // ALTERAR
        int count = 0;
        for(Endereco e : sistema.getDestinos()) {
            System.out.println(count + " -> Freguesia :" + e.getFreguesia() + " Rua: " + e.getRua());
            count++;
        }
        System.out.println("Selecione a localidade do cliente: ");
        int localidade = getInt();
        if(localidade < 0 || localidade >= sistema.getDestinos().size()) {
            System.out.println("Localidade inválida!!!");
            return;
        }



        Estafeta estafeta = null;
        Endereco endereco = sistema.getDestinos().get(localidade);
        Cliente cliente = sistema.getClientes().get(idCliente);
        Encomenda encomenda = new Encomenda(id,peso,volume,cliente,estafeta,preco,prazo,endereco);
        sistema.getEncomenda().put(id,encomenda);
        encomenda.setTransporteMaisAdequado();
        System.out.println("Encomenda criada!!!");
    }

    public void verEncomenda() {
        System.out.println("Selecione a encomenda: ");
        int count = 0;
        List<Encomenda> lista = sistema.getEncomenda().values().stream().collect(Collectors.toList());
        for(Encomenda encomenda : sistema.getEncomenda().values()) {
            System.out.println(count++ + " -> Id da encomenda: " + encomenda.getIdEncomenda() + " Nome do cliente: " + encomenda.getCliente().getNome());
        }
        int opcao = getInt();
        if(opcao < 0 || opcao >= lista.size()) {
            System.out.println("Encomenda inválida!!!");
            return;
        }
        Encomenda encomenda = lista.get(opcao);
        System.out.println("Id da encomenda: " + encomenda.getIdEncomenda());
        System.out.println("Peso da encomenda: " + encomenda.getPeso());
        System.out.println("Volume da encomenda: " + encomenda.getVolume());
        if(encomenda.getTrans() == null) System.out.println("Transporte da encomenda: Ainda não definido!");
        else System.out.println("Transporte da encomenda: " + encomenda.getTrans().getNome());
        System.out.println("Nome do cliente da encomenda: " + encomenda.getCliente().getNome());
        //System.out.println("Nome do estafeta da encomenda: " + encomenda.getEstafeta().getNome());
        System.out.println("Preco da encomenda: " + encomenda.getPreco() + "€");
        System.out.println("Prazo da encomenda: " + encomenda.getPrazo() + " dias");
        System.out.println("Destino da encomenda: " + encomenda.getEndereco().getRua());
        System.out.println("Caminho a ser percorrido da encomenda: ");
        if(encomenda.getCaminho().isEmpty()) System.out.println("Distância a ser percorrida para a entrega da encomenda: Ainda não definido!");
        else {
            for(Endereco e : encomenda.getCaminho()) { System.out.print("-> "+ e.getRua()); }
            System.out.println("Distância a ser percorrida para a entrega da encomenda: " + encomenda.getDistancia());
        }
    }

    public void fazerEntrega() {
        System.out.println("Seleciona a encomenda a realizar: ");
        int count = 0;
        List<Encomenda> lista = sistema.getEncomenda().values().stream().collect(Collectors.toList());
        for(Encomenda encomenda : sistema.getEncomenda().values()) {
            if(!encomenda.getCaminho().isEmpty())
                System.out.println(count++ + " -> Id da encomenda: " + encomenda.getIdEncomenda() + " Nome do cliente: " + encomenda.getCliente().getNome());
        }
        int opcao = getInt();
        if(opcao < 0 || opcao >= lista.size()) {
            System.out.println("Encomenda inválida!!!");
            return;
        }
        Encomenda encomenda = lista.get(opcao);

        for(Estafeta e : sistema.getEstafetas().values()) System.out.println("Id: " + e.getId() + " Nome: " + e.getNome());
        System.out.println("Introduza o id do estafeta responsável pela entrega: ");
        String idEstafeta = getLine();
        if(!sistema.getEstafetas().containsKey(idEstafeta)) {
            System.out.println("O id introduzido não é válido!!!");
            return;
        }

        encomenda.setEstafeta(sistema.getEstafetas().get(idEstafeta));

        float tempoEntrega = encomenda.getDistancia()*encomenda.getTrans().getVelocidadeMed();
        Entrega entrega = new Entrega(encomenda.getIdEncomenda(),encomenda.getCaminho(),encomenda,tempoEntrega,encomenda.getDistancia());

        if(tempoEntrega <= encomenda.getPrazo()) encomenda.getEstafeta().setEntregas(encomenda.getEstafeta().getEntregas()+1);
        else encomenda.getEstafeta().setEntregas(encomenda.getEstafeta().getEntregas()-1);

        System.out.println("Tempo de entrega -> " + tempoEntrega + " Prazo estabelecido pelo cliente -> " + encomenda.getPrazo());
        System.out.println("Introduza uma classificação ao estafeta (0-10): ");
        int classificacao = getInt();
        if(classificacao>=0 && classificacao <=10) encomenda.getEstafeta().getEntregasFeitas().put(entrega,classificacao);
        else {
            System.out.println("Classificação Inválida!!!");
            return;
        }

        int counter = 0;
        int total = 0;
        for(Map.Entry<Entrega, Integer> entry : encomenda.getEstafeta().getEntregasFeitas().entrySet()) {
            counter++;
            total += entry.getValue();
        }
        encomenda.getEstafeta().setClassificacao(total/counter);

        String circuito = "";
        for(Endereco endereco : encomenda.getCaminho()) {
            circuito += endereco.getFreguesia();
        }

        if(!sistema.getCircuitos().containsKey(circuito)) {
            sistema.getCircuitos().put(circuito,new ArrayList<>());
        }

        sistema.getEncomenda().remove(encomenda.getIdEncomenda());
        sistema.getEntregas().put(encomenda.getIdEncomenda(),entrega);
        sistema.getCircuitos().get(circuito).add(entrega);


        float peso = encomenda.getPeso();
        List<Encomenda> levadas = new ArrayList<>();
        // Levar encomendas para freguesias que estejam no circuito
        List<Endereco> caminho = encomenda.getCaminho();
        for(Endereco end : caminho){
            for(Encomenda enc : sistema.getEncomenda().values())
                if(caminho.contains(enc.getEndereco()) &&
                        calculaDistancia(caminho.subList(0, 1 + caminho.indexOf(enc.getEndereco())))/recalculaVelMedia(encomenda.getTrans().getTipo(), peso + enc.getPeso()) < enc.getPrazo() &&
                        enc.getPeso() + peso <= Transporte.pesos(encomenda.getTrans().getTipo()) &&
                        verificaLista(levadas, encomenda, caminho, peso + enc.getPeso()) &&
                        calculaDistancia(caminho)/recalculaVelMedia(encomenda.getTrans().getTipo(), peso) < encomenda.getPrazo()){
                    levadas.add(enc);
                    peso += enc.getPeso();
                    enc.setEstafeta(encomenda.getEstafeta());
                    enc.setTrans(encomenda.getTrans());
                    enc.setCaminho(caminho.subList(0, 1 + caminho.indexOf(enc.getEndereco())));
                    enc.calculaDistancia();
                    tempoEntrega = enc.getDistancia() * encomenda.getTrans().getVelocidadeMed();
                    entrega = new Entrega(enc.getIdEncomenda(), enc.getCaminho(), enc, tempoEntrega, enc.getDistancia());
                    sistema.getEncomenda().remove(enc.getIdEncomenda());
                    sistema.getEntregas().put(enc.getIdEncomenda(),entrega);
                    sistema.getCircuitos().get(circuito).add(entrega);
                }
        }
    }

    private boolean verificaLista(List<Encomenda> lista, Encomenda encomenda, List<Endereco> caminho, float peso){
        for(Encomenda enc : lista){
            if(!(calculaDistancia(caminho.subList(0, 1 + caminho.indexOf(enc.getEndereco())))/recalculaVelMedia(encomenda.getTrans().getTipo(), peso) < enc.getPrazo())) return false;
        }
        return true;
    }

    private float recalculaVelMedia(int transporte, float peso){
        return Transporte.velTrans(transporte) - peso * Transporte.pesosPerdidos(transporte);
    }

    private int calculaDistancia(List<Endereco> caminho) {
        int distancia = 0;
        for(int i = 0; i < caminho.size() - 1; i++)
            distancia += Encomenda.calcDist(caminho.get(i), caminho.get(i + 1));
        return distancia;
    }

    public void verEntrega() {
        System.out.println("Selecione a entrega que deseja ver: ");
        int count = 0;
        List<Entrega> lista = sistema.getEntregas().values().stream().collect(Collectors.toList());
        for(Entrega entrega : sistema.getEntregas().values()) {
            System.out.println(count++ + " -> Id da entrega: " + entrega.getId() + " Nome do cliente: " + entrega.getEncomenda().getCliente().getNome());
        }
        int opcao = getInt();
        if(opcao < 0 || opcao >= lista.size()) {
            System.out.println("Entrega inválida!!!");
            return;
        }

        Entrega entrega = lista.get(opcao);
        System.out.println("Id da entrega: " + entrega.getId());
        System.out.println("Caminho percorrido da entrega: ");
        for(Endereco e : entrega.getCaminho()) {
            System.out.print("-> "+ e.getRua());
        }
        System.out.println("\nId da entrega associada: " + entrega.getEncomenda().getIdEncomenda());
        System.out.println("Tempo de entrega: " + entrega.getTempoEntrega());
        System.out.println("Distancia percorrida: " + + entrega.getDistancia());

    }

    public void geraCaminho() {
        System.out.println("Selecione a encomenda: ");
        int count = 0;
        List<Encomenda> lista = sistema.getEncomenda().values().stream().collect(Collectors.toList());
        for(Encomenda encomenda : sistema.getEncomenda().values()) {
            System.out.println(count++ + " -> Id da encomenda: " + encomenda.getIdEncomenda() + " Nome do cliente: " + encomenda.getCliente().getNome());
        }
        int opcao = getInt();
        if(opcao < 0 || opcao >= lista.size()) {
            System.out.println("Encomenda inválida!!!");
            return;
        }
        System.out.println("Selecione o algoritmo que deseja para gerar o caminho da encomend: a");
        System.out.println("0 -> Depth-First");
        System.out.println("1 -> Breadth-First");
        System.out.println("2 -> Iterative Depth-First");
        System.out.println("3 -> Gulosa (Mais rápida)");
        System.out.println("4 -> Gulosa (Mais ecológica)");
        System.out.println("5 -> A* (Mais rápida)");
        System.out.println("6 -> A* (Mais ecológica)");
        int alg = getInt();

        Endereco endereco = lista.get(opcao).getEndereco();
        Encomenda encomenda = lista.get(opcao);
        List<Endereco> caminho = null;
        switch (alg) {
            case 0 : caminho = sistema.depthFirstSearch(endereco); break;
            case 1 : caminho = sistema.breathFirstSearch(endereco); break;
            case 2 : caminho = sistema.depthFirstSearchIterative(endereco); break;
            case 3 : caminho = sistema.gulosa(endereco, null); break;
            case 4 : caminho = sistema.gulosa(endereco, encomenda.getTrans()); break;
            case 5 : caminho = sistema.algoritmoAEstrela(endereco, null); break;
            case 6 : caminho = sistema.algoritmoAEstrela(endereco, encomenda.getTrans()); break;
            default:
                System.out.println("Numero inserido inválido!!!");
                return ;
        }
        encomenda.setCaminho(caminho);
        System.out.println("O caminho calculdo foi: ");
        for(Endereco e : encomenda.getCaminho()) {
            System.out.print("-> "+ e.getRua());
        }
        System.out.print("\n");
        encomenda.calculaDistancia();
    }

    public void gerarCircuitos() {
        int flag = 0;
        List<Endereco> lista = new ArrayList<>();
        while(flag == 0) {
            int count = 1;
            System.out.println("Selecione a localidade que pretende gerir circuitos: ");
            for(Endereco endereco : sistema.getDestinos()) {
                System.out.println(count++ + " -> Freguesia: " + endereco.getFreguesia());
            }
            System.out.println("0 -> ACABAR DE GERAR CIRCUITO");
            int opcao = getInt();
            if(opcao < 0 || opcao > sistema.getDestinos().size()) {
                System.out.println("Destino inválida!!!");
                return;
            }
            if(lista.contains(sistema.getDestinos().get(opcao))) {
                System.out.println("Destino já adicionado!!!");
            }
            else if(opcao == 0) {
                flag = 1;
            } else {
                lista.add(sistema.getDestinos().get(opcao));
            }
        }
        List<List<List<Endereco>>> novo = sistema.geraCircuitos(lista);
        int i = 0;
        for(Endereco endereco : lista) {
            System.out.println("Os caminhos possiveis para " + endereco.getFreguesia());
            int counter = 1;
            for(List<Endereco> adj : novo.get(i)) {
                System.out.println(" Caminho " + counter++ );
                for(Endereco ok : adj) {
                    System.out.println("      " + ok.toString());
                }
            }
            i++;
        }
    }

    public void circuitosMaisUsados() {
        System.out.println("Circuitos com mais entregas com maior volume: ");
        List<Map.Entry<List<Endereco>,Integer>> circuitosVolume = new ArrayList<>();
        for(List<Entrega> entregas : sistema.getCircuitos().values()) {
            int volume = 0;
            for(Entrega ent : entregas) {
                volume += ent.getEncomenda().getVolume();
            }
            circuitosVolume.add(Map.entry(entregas.get(0).getCaminho(),volume));
        }
        Comparator<Map.Entry<List<Endereco>,Integer>> aux = (Map.Entry<List<Endereco>,Integer> b1, Map.Entry<List<Endereco>,Integer> b2)
                -> (int) (b2.getValue() - b1.getValue());
        Collections.sort(circuitosVolume,aux);

        for(Map.Entry<List<Endereco>,Integer> novo : circuitosVolume) {
            System.out.println("-> " + novo.getKey().toString());
            System.out.println("     Volume: " + novo.getValue());
        }

        System.out.println("Circuitos com mais entregas com maior peso: ");
        List<Map.Entry<List<Endereco>,Integer>> circuitosPeso = new ArrayList<>();
        for(List<Entrega> entregas : sistema.getCircuitos().values()) {
            int peso = 0;
            for(Entrega ent : entregas) {
                peso += ent.getEncomenda().getPeso();
            }
            circuitosPeso.add(Map.entry(entregas.get(0).getCaminho(),peso));
        }

        Collections.sort(circuitosPeso,aux);

        for(Map.Entry<List<Endereco>,Integer> novo : circuitosPeso) {
            System.out.println("-> " + novo.getKey().toString());
            System.out.println("     Peso: " + novo.getValue());
        }
    }

    public void compararCircuitos() {
        if(sistema.getCircuitos().size() <= 1) {
            System.out.println("Apenas tem 1 circuito!");
            return;
        }
        System.out.println("Selecione dois circuitos: ");
        int count1 = 0;
        List<String> lista1 = sistema.getCircuitos().keySet().stream().collect(Collectors.toList());
        for(String circuito : sistema.getCircuitos().keySet()) {
            System.out.println(count1++ + " -> Circuito: " + circuito);
        }
        int opcao1 = getInt();
        if(opcao1 < 0 || opcao1 >= lista1.size()) {
            System.out.println("Circuito inválido!!!");
            return;
        }
        String circuito1 = lista1.get(opcao1);
        System.out.println("Selecione dois circuitos: ");
        int count2 = 0;
        List<String> lista2 = sistema.getCircuitos().keySet().stream().collect(Collectors.toList());
        for(String circuito : sistema.getCircuitos().keySet()) {
            System.out.println(count2++ + " -> Circuito: " + circuito);
        }
        int opcao2 = getInt();
        if(opcao2 < 0 || opcao2 >= lista2.size()) {
            System.out.println("Circuito inválido!!!");
            return;
        }
        String circuito2 = lista2.get(opcao2);

        if(circuito1.equals(circuito2)) {
            System.out.println("Selecionou 2 vezes o mesmo circuito!!!");
            return;
        }

        int tempo1 = 0;
        for(Entrega entrega : sistema.getCircuitos().get(circuito1)) {
            tempo1 += entrega.getEncomenda().getPrazo() - entrega.getTempoEntrega();
        }
        int tempo2 = 0;
        for(Entrega entrega : sistema.getCircuitos().get(circuito2)) {
            tempo2 += entrega.getEncomenda().getPrazo() - entrega.getTempoEntrega();
        }

        int distancia1 = 0;
        for(Entrega entrega : sistema.getCircuitos().get(circuito2)) {
            distancia1 += entrega.getEncomenda().getDistancia();
        }
        int distancia2 = 0;
        for(Entrega entrega : sistema.getCircuitos().get(circuito2)) {
            distancia2 += entrega.getEncomenda().getDistancia();
        }

        int fatorProdutividade1 = tempo1 + distancia1;
        int fatorProdutividade2 = tempo2 + distancia2;

        if(fatorProdutividade1 >= fatorProdutividade2) {
            System.out.println("O circuito com maior fator de produtividade foi: " + sistema.getCircuitos().get(circuito1).get(0).getCaminho().toString());
            System.out.println("-> Tempo de entrega: "+ tempo1);
            System.out.println("-> Distância percorrida: "+ distancia1);
            System.out.println("O circuito com menor fator de produtividade foi: " + sistema.getCircuitos().get(circuito2).get(0).getCaminho().toString());
            System.out.println("-> Tempo de entrega: "+ tempo2);
            System.out.println("-> Distância percorrida: "+ distancia2);
        } else {
            System.out.println("O circuito com maior fator de produtividade foi: " + sistema.getCircuitos().get(circuito2).get(0).getCaminho().toString());
            System.out.println("-> Tempo de entrega: "+ tempo2);
            System.out.println("-> Distância percorrida: "+ distancia2);
            System.out.println("O circuito com menor fator de produtividade foi: " + sistema.getCircuitos().get(circuito1).get(0).getCaminho().toString());
            System.out.println("-> Tempo de entrega: "+ tempo1);
            System.out.println("-> Distância percorrida: "+ distancia1);
        }
    }




    private String getLine(){
        return sc.nextLine();
    }

    private int getInt(){
        try {
            int num = sc.nextInt();
            sc.nextLine();
            return num;
        }catch (Exception e) {
            sc.nextLine();
            return -1;
        }
    }

    private float getFloat(){
        try {
            float num = sc.nextFloat();
            sc.nextLine();
            return num;
        }catch (Exception e) {
            sc.nextLine();
            return -1;
        }
    }

    private long getLong(){
        try {
            long num = sc.nextLong();
            sc.nextLine();
            return num;
        }catch (Exception e) {
            sc.nextLine();
            return -1;
        }
    }
}