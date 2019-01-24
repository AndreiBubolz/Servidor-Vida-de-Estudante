
package servidorjogo;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


public class Usuario {
    private String nick;
    private String senha;
    private int XP;
    private int vitorias;
    private int derrotas;
    private int IDConexao;
    private int IDConexaoJogo;
    private int numSalaLogado;
    private InfoPartida informacaoPartida;
    private Queue<String> mensagens;
    private int posicaoSala;
    
    public Usuario(String nick) {
        this.nick = nick;
        this.senha = "";
        this.XP = 0;
        this.vitorias = 0;
        this.derrotas = 0;
        this.IDConexao = -1;
        this.numSalaLogado = -1;
        this.IDConexaoJogo = -1;
        this.informacaoPartida = new InfoPartida(100, 150, "No Corredor da Computação", 0);
        this.mensagens = new LinkedList();
        this.posicaoSala = -1;
    }
    
    public Usuario(String nick,String senha, int XP, int vitorias, int derrotas) {
        this.nick = nick;
        this.senha = senha;
        this.XP = XP;
        this.vitorias = vitorias;
        this.derrotas = derrotas;
        this.IDConexao = -1;
        this.numSalaLogado = -1;
        this.IDConexaoJogo = -1;
        this.informacaoPartida = new InfoPartida(100, 150, "No Corredor da Computação", 0);
        this.mensagens = new LinkedList();
        this.posicaoSala = -1;
    }
    
    public void resetInfoSala(){
        this.informacaoPartida = new InfoPartida(100, 150, "No Corredor da Computação", 0);
        this.informacaoPartida.setVitoria(false);
        this.informacaoPartida.setJogando(true);   
        //this.posicaoSala = -1;
    }
    
    
    public int getPosicaoSala() {
        return posicaoSala;
    }

    public void setPosicaoSala(int posicaoSala) {
        this.posicaoSala = posicaoSala;
    }
    
    public void setFimJogo(){
        
        this.informacaoPartida.setJogando(false);
        
    }
    
    public void setComecoJogo(){
        
        this.informacaoPartida.setJogando(true);
        
    }
    
    public InfoPartida getInfoPartida(){
        return this.informacaoPartida;
    }
    
    public boolean temMensagem(){
        return !this.mensagens.isEmpty();
    }
    public String getMensagem(){  
        return this.mensagens.poll(); 
    }
    
    public void setMensagem(String msg){
        this.mensagens.offer(msg);
    }
    
    public int getIDConexaoJogo() {
        return IDConexaoJogo;
    }

    public void setIDConexaoJogo(int IDConexaoJogo) {
        this.IDConexaoJogo = IDConexaoJogo;
    }
 
    
    public int getNumSala() {
        return numSalaLogado;
    }

    public void setNumSala(int numSala) {
        this.numSalaLogado = numSala;
    }
    
    public int getIDConexao() {
        return IDConexao;
    }

    public void setIDConexao(int IDConexao) {
        this.IDConexao = IDConexao;
    }

    
    public int getXP() {
        return XP;
    }

    public void setXP(int XP) {
        this.XP = XP;
    }

    public int getVitorias() {
        return vitorias;
    }

    public void incVitorias() {
        this.vitorias = vitorias+1;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public void incDerrotas() {
        this.derrotas = derrotas+1;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public boolean verificaSenha(String senhaDigitada){
        
        return senhaDigitada.equals(this.senha);
        
    }
    
}
