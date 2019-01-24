
package servidorjogo;

public class Sala{
    private String ID;
    private Usuario usuario1;
    private Usuario usuario2;
    private int estado;
    
    public Sala(String ID){
       this.ID = ID; 
       this.estado = 0; 
    }
    
    public Sala(String ID,Usuario us1,Usuario us2,int est) {
        this.usuario1 = us1;
        this.usuario2 = us2;
        this.ID = ID; 
        this.estado = est;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Usuario getUsuario1() {
        return usuario1;
    }

    public void setUsuario1(Usuario usuario1) {
        this.usuario1 = usuario1;
    }

    public Usuario getUsuario2() {
        return usuario2;
    }

    public void setUsuario2(Usuario usuario2) {
        this.usuario2 = usuario2;
    }
    //0= Aguardando preencher jogadores
    //1= Aguardando confirmacao dos 2 jogadores
    //2= Aguardando confirmacao do jogador 1.
    //3= Aguardando confirmacao do jogador 2.
    //4= Jogo acontecendo.
    public void setEstado(int est){
        this.estado = est;
    }
    
    public int getEstado(){
        return this.estado;
    }
}
