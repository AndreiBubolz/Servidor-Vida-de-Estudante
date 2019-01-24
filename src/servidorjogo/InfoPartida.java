
package servidorjogo;

import java.util.Random;


public class InfoPartida {
    private int vida;
    private int dinheiro;
    private String local;
    private int numChaves;
    private boolean jogando;
    private boolean vitoria;

    
    public InfoPartida(int v,int d,String l,int n) {
        this.vida = v;
        this.dinheiro = d;
        this.local = l;
        this.numChaves = n;
        
    }

    public boolean isVitoria() {
        return vitoria;
    }

    public void setVitoria(boolean vitoria) {
        this.vitoria = vitoria;
    }

    
    
    public boolean isJogando() {
        return jogando;
    }

    public void setJogando(boolean jogando) {
        this.jogando = jogando;
    }
    
    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getDinheiro() {
        return dinheiro;
    }

    public void setDinheiro(int dinheiro) {
        this.dinheiro = dinheiro;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public int getNumChaves() {
        return numChaves;
    }

    public void setNumChaves(int numChaves) {
        this.numChaves = numChaves;
    }
    
    
    
}