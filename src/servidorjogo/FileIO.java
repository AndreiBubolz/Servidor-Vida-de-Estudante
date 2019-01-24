
package servidorjogo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class FileIO {
    
    public HashMap<String,Usuario> carregaBancoDeJogadores(){
        HashMap<String,Usuario> listaUsuarios = new HashMap();
        String linha;
        String[] linhaDividida;
        BufferedReader buffRead;
        try {
            buffRead = new BufferedReader(new FileReader("BancoDeDados.txt"));
        
            while( (linha=buffRead.readLine()) != null ){

                linhaDividida = linha.split(";");

                Usuario novoUsuario = new Usuario(linhaDividida[0], linhaDividida[1], Integer.valueOf(linhaDividida[2]), Integer.valueOf(linhaDividida[3]), Integer.valueOf(linhaDividida[4]));

                listaUsuarios.put(novoUsuario.getNick(), novoUsuario);
            }
            buffRead.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Erro ao ler Banco de Dados.");
        } catch (IOException ex) {
            System.out.println("Erro ao ler Banco de Dados");
        }
        
        return listaUsuarios;
        
    }
    
    public void salvaBancoDeJogadores(HashMap<String,Usuario> listaUsuarios){
        
        System.out.println("->>>>>>>>>>>>>"+ listaUsuarios.size());
        File file = new File("BancoDeDados.txt");
        file.delete();
        
        try {
            BufferedWriter buffWrite = new BufferedWriter(new FileWriter("BancoDeDados.txt"));
            
            for(Map.Entry<String,Usuario> lista: listaUsuarios.entrySet()){
                Usuario usu = lista.getValue();
                buffWrite.write(usu.getNick()+";"+usu.getSenha()+";"+String.valueOf(usu.getXP())+";"+String.valueOf(usu.getVitorias())+";"+String.valueOf(usu.getDerrotas()));
                buffWrite.newLine(); 
            }
            buffWrite.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Erro ao salvar Banco de Dados.");
        } catch (IOException ex) {
            System.out.println("Erro ao salvar Banco de Dados");
        }
    
    }
}
