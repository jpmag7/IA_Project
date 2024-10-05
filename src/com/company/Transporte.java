package com.company;

public class Transporte {
    private int tipo;
    private float velocidadeMed;
    public static final int BICICLETA= 0;
    public static final int CARRO= 1;
    public static final int MOTO= 2;
    public static final float VEL_BICICLETA= 10;
    public static final float MAX_PESO_BICICLETA= 5;
    public static final float VEL_CARRO= 25;
    public static final float MAX_PESO_CARRO= 100;
    public static final float VEL_MOTO= 35;
    public static final float MAX_PESO_MOTO= 20;

    public Transporte(int tipo, float velocidadeMed) {
        this.tipo = tipo;
        this.velocidadeMed = velocidadeMed;
    }

    public static float pesos(int transporte) {
        switch (transporte){
            case BICICLETA: return MAX_PESO_BICICLETA;
            case MOTO: return MAX_PESO_MOTO;
            case CARRO: return MAX_PESO_CARRO;
            default:return -1;
        }
    }

    public static float velTrans(int transporte) {
        switch (transporte){
            case BICICLETA: return VEL_BICICLETA;
            case MOTO: return VEL_MOTO;
            case CARRO: return VEL_CARRO;
            default:return -1;
        }
    }

    public static float pesosPerdidos(int transporte) {
        switch (transporte){
            case BICICLETA: return 0.7f;
            case MOTO: return 0.5f;
            case CARRO: return 0.1f;
            default:return -1;
        }
    }

    public String getNome() {
        switch (tipo){
            case BICICLETA: return "Bicicleta";
            case MOTO: return "Mota";
            case CARRO: return "Carro";
            default:return "Não Definido";
        }
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public float getVelocidadeMed() {
        return velocidadeMed;
    }

    public void setVelocidadeMed(float velocidadeMed) {
        this.velocidadeMed = velocidadeMed;
    }


    @Override
    public String toString() {
        return "Transporte{" +
                "tipo=" + tipo +
                ", velocidadeMed=" + velocidadeMed +
                '}';
    }
}
