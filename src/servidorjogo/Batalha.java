package servidorjogo;

import java.util.Random;

public class Batalha {

    private String IDBatalha;
    private Usuario desafiante;
    private Usuario desafiado;
    private int numDesafiante;
    private int numDesafiado;
    private int valorAposta;
    private int situacaoDesafiado; //0-Aguardando 1-aceitou - 2-recusou.
    private boolean vizualizadoDesafiante;
    private boolean vizualizadoDesafiado;
    private boolean resultadoPronto;

    public Batalha(Usuario desafiante, Usuario desafiado, int valorAposta) {
        this.IDBatalha = desafiante.getNick() + "vs" + desafiado.getNick();
        this.desafiante = desafiante;
        this.desafiado = desafiado;
        this.situacaoDesafiado = 0;
        this.valorAposta = valorAposta;
        this.vizualizadoDesafiante = false;
        this.vizualizadoDesafiado = false;
        this.resultadoPronto = false;
    }

    public String getIDBatalha() {
        return IDBatalha;
    }

    public void setresultadoPronto(boolean result){
        
        this.resultadoPronto = result;
        
    }
    
    public boolean isResultadoPronto(){
        
        return this.resultadoPronto;
        
    }
    
    public void setIDBatalha(String IDBatalha) {
        this.IDBatalha = IDBatalha;
    }

    public int getSituacaoDesafiado() {
        return situacaoDesafiado;
    }

    public void setSituacaoDesafiado(int situacaoDesafiado) {
        this.situacaoDesafiado = situacaoDesafiado;
    }

    public boolean isVizualizadoDesafiante() {
        return vizualizadoDesafiante;
    }

    public void setVizualizadoDesafiante(boolean vizualizadoDesafiante) {
        this.vizualizadoDesafiante = vizualizadoDesafiante;
    }

    public boolean isVizualizadoDesafiado() {
        return vizualizadoDesafiado;
    }

    public void setVizualizadoDesafiado(boolean vizualizadoDesafiado) {
        this.vizualizadoDesafiado = vizualizadoDesafiado;
    }

    
    
    public int getSituacaoDesafido() {
        return situacaoDesafiado;
    }

    public void setSituacaoDesafido(int situacaoDesafio) {

        this.situacaoDesafiado = situacaoDesafio;

        if (situacaoDesafio == 1) {
            Random random = new Random();
            this.numDesafiado = random.nextInt(100);
            this.numDesafiante = random.nextInt(100);
            this.setresultadoPronto(true);
        }

    }

    public Usuario getDesafiante() {
        return desafiante;
    }

    public void setDesafiante(Usuario desafiante) {
        this.desafiante = desafiante;
    }

    public Usuario getDesafiado() {
        return desafiado;
    }

    public void setDesafiado(Usuario desafiado) {
        this.desafiado = desafiado;
    }

    public int getNumDesafiante() {
        return numDesafiante;
    }

    public void setNumDesafiante(int numDesafiante) {
        this.numDesafiante = numDesafiante;
    }

    public int getNumDesafiado() {
        return numDesafiado;
    }

    public void setNumDesafiado(int numDesafiado) {
        this.numDesafiado = numDesafiado;
    }

    public int getValorAposta() {
        return valorAposta;
    }

    public void setValorAposta(int valorAposta) {
        this.valorAposta = valorAposta;
    }

}
