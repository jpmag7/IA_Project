package com.company;

public class Endereco{
    private String rua;
    private String freguesia;


    public Endereco(String freguesia, String rua){
        this.freguesia = freguesia;
        this.rua = rua;
    }

    public String getRua(){return rua;}

    public String getFreguesia(){return freguesia;}

    public void setRua(String rua){this.rua = rua;}

    public void setFreguesia(String freguesia){this.freguesia = freguesia;}

    @Override
    public String toString() {
        return "Rua: " + this.rua + "; Freguesia: " + this.freguesia;
    }
}
