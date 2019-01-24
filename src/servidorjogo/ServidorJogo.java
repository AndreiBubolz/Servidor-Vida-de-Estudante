package servidorjogo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorJogo implements Runnable {

    private ObjectOutputStream saida;
    private ObjectInputStream entrada;
    private Socket usuario;
    private static HashMap<String, Usuario> listaUsuarios;
    private static HashMap<String, Usuario> listaLogados;
    private static HashMap<String, Batalha> listaBatalhas;
    private static HashMap<String, String> listaPalavroes;
    private static FileIO IO;
    private static ArrayList<Sala> listaSalas;
    private JanelaServidor janelaS;
    private static int contadorID;

    public ServidorJogo(Socket s, JanelaServidor jane) {
        this.usuario = s;
        janelaS = jane;

        IO = new FileIO();
        listaUsuarios = IO.carregaBancoDeJogadores();

        //Inicializa todos os conjuntos.
        if (listaSalas == null) {
            listaSalas = new ArrayList();
        }

        if (listaLogados == null) {
            listaLogados = new HashMap();
        }
        if (listaBatalhas == null) {
            listaBatalhas = new HashMap();
        }

        if (listaPalavroes == null) {
            listaPalavroes = new HashMap();
            carregaListaPalavroes();   //Carrega lista de palavroes.
        }

        if (listaSalas.isEmpty()) {//Inicializa as salas (12)
            listaSalas.add(new Sala("#" + String.valueOf(listaSalas.size() + 1)));
            listaSalas.add(new Sala("#" + String.valueOf(listaSalas.size() + 1)));
            listaSalas.add(new Sala("#" + String.valueOf(listaSalas.size() + 1)));
            listaSalas.add(new Sala("#" + String.valueOf(listaSalas.size() + 1)));
            listaSalas.add(new Sala("#" + String.valueOf(listaSalas.size() + 1)));
            listaSalas.add(new Sala("#" + String.valueOf(listaSalas.size() + 1)));
            listaSalas.add(new Sala("#" + String.valueOf(listaSalas.size() + 1)));
            listaSalas.add(new Sala("#" + String.valueOf(listaSalas.size() + 1)));
            listaSalas.add(new Sala("#" + String.valueOf(listaSalas.size() + 1)));
            listaSalas.add(new Sala("#" + String.valueOf(listaSalas.size() + 1)));
            listaSalas.add(new Sala("#" + String.valueOf(listaSalas.size() + 1)));
            listaSalas.add(new Sala("#" + String.valueOf(listaSalas.size() + 1)));
        }
    }

    public void run() {

        //Se entrou aqui, cliente obteve com sucesso conexão com o servidor.
        System.out.println("Conexão iniciada com o cliente " + usuario.getInetAddress().getHostAddress());
        String lidoNick, lidoSenha;
        Usuario user = null;
        Sala sala = null;

        try {

            /////STATIC = TODAS AS INSTÂNCIAS DO SERVIDOR ACESSAM A MESMA VARIÁVEL.
            saida = new ObjectOutputStream(usuario.getOutputStream());
            entrada = new ObjectInputStream(usuario.getInputStream());

            saida.writeObject(true); //Envia para cliente "sinal" de conexão bem sucessida
            saida.writeObject(++contadorID); //Envia um ID unico para representar o cliente atendido. (STATIC)
            int salvaContadorID = contadorID; // Salva o ID do cliente sendo atendido por essa instância do servidor. (LOCAL)
            saida.writeObject(this.janelaS.getNumConectados()); //Envia para cliente numero de clientes conectados. (STATIC)

            OUTER:
            while (true) {  //Loop principal que fica rodando o tempo todo até a desconexão.

                int codigoOperacao = (int) entrada.readObject(); // Códido de operação a ser executado
                // De acordo com esse código, é executada uma sequencia de comandos selecionado pelo Switch abaixo.

                switch (codigoOperacao) {
                    case 1:  // Login cliente
                        loginCliente();
                        break;
                    case 2:  //Cadastro cliente
                        cadastroCliente();
                        break;
                    case 3: // Termina conexao com cliente sem fazer logoff;
                        entrada.close(); // Fecha o canal de entrada
                        saida.close(); //Fecha o canal de saida.
                        System.out.println("Conexão terminada com o cliente " + usuario.getInetAddress().getHostAddress());
                        break OUTER;
                    case 4: // Termina conexao com cliente fazendo logoff do mesmo;
                        removeListaLogados((int) entrada.readObject()); //Remove da lista de logados.
                        janelaS.decNumOnline(); //Decrementa o numero de logados.
                        entrada.close(); //Fecha canal de entrada.
                        saida.close(); // Fecha canal de entrada
                        usuario.close();
                        System.out.println("Conexão terminada com o cliente " + usuario.getInetAddress().getHostAddress());
                        break OUTER;
                    case 5: //Conectar na sala.  // Colca usuaario em uma determinada sala.
                        conectaEmSala();
                        break;
                    case 6: //Atualiza lista de salas na janela.
                        atualizaSalasJanela();
                        break;
                    case 7: //Remove usuario de sala.
                        int ID = (int) entrada.readObject();  // Recebe ID unico do jogador a ser removido.
                        int x = removoDeSala(ID); // remove da sala.
                        saida.writeObject(x); //Envia para cliente se foi bem sucessido. -1= nao foi possivel remover, CC. Envia o numero da sala q foi removido.
                        break;
                    case 8: // Não usado.

                        break;
                    case 9: // Salva um ID unico de conexão de usuario, referente ao tratamento direto do jogo.. (nao mais da parte de login)
                        salvaIDConexaoJogo(salvaContadorID);
                        break;
                    case 10: // Verifica se algum adversário se juntou a sala. Se sim, avisa que já pode começar o jogo pro cliente que está esperando
                        // e também pro que conectou por ultimo
                        aguardaAdversario();
                        break;
                    case 11:    //Atualiza informação usuário na lista de logados, recebido do cliente já atualizado em tempo real.
                        atualizaInfoUsuario();
                        break;
                    case 12: //Envia informacao atualizada do adversário para o usuário,.
                        enviaInfoAtualizadaAdversario();
                        break;
                    case 13: //Envia msg para cliente.
                        enviaMensagemParaCliente();
                        break;
                    case 14: //Recebe msg de cliente.
                        recebeMensagemDeCliente();
                        break;
                    case 15: // Avisa servidor que jogador ganhou
                        registraVitoriaDeJogador();

                        break;
                    case 16: //Devolve pro cliente se há algum vencedor na partida.
                        verificaSeTemVitoriaDeJogador();
                        break;
                        
                    //////------------------- Inicio batalhas, dificil de explicar. Se nao quis botar no relatorio, botar algo bem por cima, OK.
                    case 17:  //Inicia partida numa sala.
                        iniciaNovaBatalha();
                        break;
                    case 18:  // Analisa no banco de dados se usuario recebeu alguma proposta de batalha.
                        verificaSeFoiDesafiado();
                        break;
                    case 19:    // Se tem proposta de batalha, recebe e responde o pedido.
                        respondePorDesafio();
                        break;
                    case 20:    // Verifica se há resultado da batalha, se sim, envia dados para mostrar na janela.
                        verificaResultadoBatalha();
                        break;
                    /////--------------------- Fim batalhas, dificil de explicar. Se nao quis botar no relatorio, botar algo bem por cima, OK.
                    
                    case 21: //Armazena infos no banco de dados.
                        salvaDados();
                        break;
                    default:
                        break;
                }
            }

        } catch (IOException | ClassNotFoundException ex) {
            try {
                entrada.close();
                saida.close();
                usuario.close();
                System.out.println("Conexão terminada com o cliente " + usuario.getInetAddress().getHostAddress());
            } catch (IOException ex1) {
                ex1.printStackTrace();

            }

        }
    }

    private void atualizaInfoUsuario() throws IOException, ClassNotFoundException {

        String nickUsuario = (String) entrada.readObject();
        int dinheiroAtual = (int) entrada.readObject();
        int vidaAtual = (int) entrada.readObject();                   //Recebe as informacoes do usuario pra atualizar.
        String localAtual = (String) entrada.readObject();
        int numChavesAtual = (int) entrada.readObject();

        Usuario usuarioAtt = listaLogados.get(nickUsuario);
        usuarioAtt.getInfoPartida().setVida(vidaAtual);
        usuarioAtt.getInfoPartida().setDinheiro(dinheiroAtual);
        usuarioAtt.getInfoPartida().setLocal(localAtual);
        usuarioAtt.getInfoPartida().setNumChaves(numChavesAtual);

        listaLogados.replace(nickUsuario, usuarioAtt);

    }

    private void enviaInfoAtualizadaAdversario() throws IOException, ClassNotFoundException {

        String nickAdversario = (String) entrada.readObject();  //Recebe nick adversario para coletar informacoes e enviar.
        Usuario adversario = listaLogados.get(nickAdversario);

        saida.writeObject(adversario.getXP());
        saida.writeObject(adversario.getVitorias());
        saida.writeObject(adversario.getDerrotas());
        saida.writeObject(adversario.getInfoPartida().getDinheiro());
        saida.writeObject(adversario.getInfoPartida().getVida());
        saida.writeObject(adversario.getInfoPartida().getLocal());
        saida.writeObject(adversario.getInfoPartida().getNumChaves());
        saida.writeObject(adversario.getPosicaoSala());

    }

    private void enviaMensagemParaCliente() throws IOException, ClassNotFoundException {

        String nickUsuarioVerificarMensagens = (String) entrada.readObject();  //Recebe nick do usuario que vai receber msgs.
        Usuario usuarioMsgs = listaLogados.get(nickUsuarioVerificarMensagens);
        String msg;

        boolean temMsg = usuarioMsgs.temMensagem();
        msg = usuarioMsgs.getMensagem();

        saida.writeObject(temMsg); //Envia se usuario possui alguma msg.

        while (temMsg) {
            saida.writeObject(msg); //Envia msg 

            temMsg = usuarioMsgs.temMensagem();
            msg = usuarioMsgs.getMensagem();

            saida.writeObject(temMsg); // Envia se ainda tem mensagem.
        }

    }

    private void recebeMensagemDeCliente() throws IOException, ClassNotFoundException {

        String nickMsgAdd = (String) entrada.readObject(); //Nick para associar a mensagem
        String msgAdd = (String) entrada.readObject(); // Mensagem recebida
        Usuario msgUsuarioAdd = listaLogados.get(nickMsgAdd);

        if (msgAdd != null) {
            msgAdd = contemPalavrao(msgAdd);
            msgUsuarioAdd.setMensagem(msgAdd);
        }

    }

    private void registraVitoriaDeJogador() throws IOException, ClassNotFoundException {

        String nickUserGanhou = (String) entrada.readObject(); //Recebe nick de jogador que ganhou.

        Usuario User = listaLogados.get(nickUserGanhou);

        User.getInfoPartida().setVitoria(true);
        User.setXP(User.getXP() + 1000);
        listaUsuarios.get(nickUserGanhou).setXP(listaUsuarios.get(nickUserGanhou).getXP() + 1000);
        User.incVitorias();
        listaUsuarios.get(nickUserGanhou).incVitorias();
        User.setFimJogo();
        // Adiciona todas infos necessarias.

        int nSala = User.getNumSala();

        listaSalas.get(nSala).setUsuario1(null);
        listaSalas.get(nSala).setUsuario2(null);

        //Retira jogadores da sala.
        listaLogados.replace(nickUserGanhou, User);

    }

    private void verificaSeTemVitoriaDeJogador() throws IOException, ClassNotFoundException {

        String nickUserAdvs = (String) entrada.readObject(); //recebe nick do adversario.
        String nickUserr = (String) entrada.readObject(); //Recebe nick do usuario atual.
        boolean desistiu = (boolean) entrada.readObject(); //Recebe se houve desistencia.
        Usuario aversario = listaLogados.get(nickUserAdvs); 
        Usuario user = listaLogados.get(nickUserr);

        if (aversario.getInfoPartida().isVitoria()) { // Se o adversario esta com registro de vitoria
            saida.writeObject(true); //Envia TRUE.

            if (!desistiu) { //Se o usuario atual nao perdeu por desistencia, adiciona 250xp.
                user.setXP(user.getXP() + 250);
                listaUsuarios.get(nickUserr).setXP(listaUsuarios.get(nickUserr).getXP() + 250);
            }
            
            user.incDerrotas();
            listaUsuarios.get(nickUserr).incDerrotas();
            user.setFimJogo();
        } else {// Se não perdeu, envia false.
            saida.writeObject(false);
        }

        if (user.getInfoPartida().isVitoria()) {
            saida.writeObject(true);
        } else {
            saida.writeObject(false);
        }

    }

    private void verificaResultadoBatalha() throws IOException, ClassNotFoundException {
        String nickVerifica = (String) entrada.readObject(); //Recebe nick para verificar.
        String desafiadoOuDesafiante = "";

        Batalha batalha = getBatalha(null, null, nickVerifica);

        if (batalha != null) {

            if (!batalha.isResultadoPronto()) {
                saida.writeObject(false); 
                return;
            }

            if (nickVerifica.equals(batalha.getDesafiado().getNick())) {
                desafiadoOuDesafiante = "desafiado";

                if (batalha.isVizualizadoDesafiado()) {
                    saida.writeObject(false); 

                    if (batalha.isVizualizadoDesafiado() && batalha.isVizualizadoDesafiante()) {
                        removeListaBatalhas(batalha);
                    }

                    return;
                }
            } else if (nickVerifica.equals(batalha.getDesafiante().getNick())) {
                desafiadoOuDesafiante = "desafiante";

                if (batalha.isVizualizadoDesafiante()) {
                    saida.writeObject(false);

                    if (batalha.isVizualizadoDesafiado() && batalha.isVizualizadoDesafiante()) {
                        removeListaBatalhas(batalha);
                    }

                    return;
                }
            }

            saida.writeObject(true);
            if (desafiadoOuDesafiante.equals("desafiado")) {
                saida.writeObject(batalha.getNumDesafiado());
                saida.writeObject(batalha.getNumDesafiante());
                batalha.setVizualizadoDesafiado(true);
            } else if (desafiadoOuDesafiante.equals("desafiante")) {
                saida.writeObject(batalha.getNumDesafiante());
                saida.writeObject(batalha.getNumDesafiado());
                batalha.setVizualizadoDesafiante(true);
            }
            saida.writeObject(batalha.getValorAposta());

        } else {
            saida.writeObject(false);
        }

    }

    private void respondePorDesafio() throws IOException, ClassNotFoundException {
        int resposta = (int) entrada.readObject();
        String nickDesafiado = (String) entrada.readObject();
        String nickDesafiante = (String) entrada.readObject();

        Batalha batalha = getBatalha(nickDesafiante, nickDesafiado, null);
        batalha.setSituacaoDesafido(resposta);

    }

    private void verificaSeFoiDesafiado() throws IOException, ClassNotFoundException {
        String nickVerificarDesafiado = (String) entrada.readObject();

        Batalha batalha = getBatalha(null, nickVerificarDesafiado, null);

        if (batalha != null) {
            if (batalha.isResultadoPronto()) {
                saida.writeObject(false);
                return;
            }

            saida.writeObject(true);
            saida.writeObject(batalha.getDesafiante().getNick());
            saida.writeObject(batalha.getValorAposta());
        } else {
            saida.writeObject(false);
        }

    }

    private void iniciaNovaBatalha() throws IOException, ClassNotFoundException {

        String nickDesafiante = (String) entrada.readObject();
        String nickDesafiado = (String) entrada.readObject();
        int valorAposta = (int) entrada.readObject();

        this.addListaBatalhas(nickDesafiante, nickDesafiado, valorAposta);

    }

    private boolean senhaValida(String senha) {
        return true;
    }

    private void adicionaUsuario(String nick, String senha) {
        listaUsuarios.put(nick, new Usuario(nick, senha, 0, 0, 0));
        this.salvaDados();
    }

    public void salvaDados() {
        IO.salvaBancoDeJogadores(listaUsuarios);
    }

    public void fechaEntradaSaida() {
        try {
            this.entrada.close();
            this.saida.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void atualizaSalasJanela() {
        try {
            saida.writeObject(listaSalas.size());   //Envia tamanho da lista de salas.
            saida.writeObject(this.janelaS.getNumConectados()); //Envia para cliente numero de clientes conectados.

            for (Sala salas : listaSalas) {
                saida.writeObject(salas.getID()); //Envia ID das salas
                if (salas.getUsuario1() != null) {
                    saida.writeObject(salas.getUsuario1().getNick()); //Envia nick do usuario 1 das salas.
                } else {
                    saida.writeObject(null); //Caso o usuario 1 da sala não estiver presente, envia null .
                }
                if (salas.getUsuario2() != null) {
                    saida.writeObject(salas.getUsuario2().getNick()); //Envia nick do usuario 2 das salas.
                } else {
                    saida.writeObject(null); //Caso o usuario 2 da sala não estiver presente, envia null .
                }
                saida.writeObject(salas.getEstado());
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }

    }

    private void colocaListaLogados(Usuario user) {

        listaLogados.put(user.getNick(), user);

    }

    private void removeListaLogados(int IDConexao) {
        for (Map.Entry<String, Usuario> lista : listaLogados.entrySet()) {
            if (lista.getValue().getIDConexao() == IDConexao) {
                removoDeSala(lista.getValue().getIDConexao());
                listaLogados.remove(lista.getKey());
                break;
            }
        }
    }

    private int removoDeSala(int ID) {
        int contador = 1;

        for (Sala sala : listaSalas) {
            if (sala.getUsuario1() != null) {
                if (sala.getUsuario1().getIDConexao() == ID) {
                    sala.setUsuario1(null);
                    return contador;
                }
            }
            if (sala.getUsuario2() != null) {
                if (sala.getUsuario2().getIDConexao() == ID) {
                    sala.setUsuario2(null);
                    return contador;
                }
            }
            contador++;
        }

        return -1;

    }

    private boolean estaEmSala(String user) {

        for (Sala sala : listaSalas) {
            if (sala.getUsuario1() != null) {
                if (sala.getUsuario1().getNick().equals(user)) {
                    return true;
                }
            }
            if (sala.getUsuario2() != null) {
                if (sala.getUsuario2().getNick().equals(user)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean estaListaLogados(String nick) {

        return listaLogados.containsKey(nick);

    }

    private void conectaEmSala() throws IOException, ClassNotFoundException {
        Usuario user;
        Sala sala;
        int numeroSala = (int) entrada.readObject(); /////////////////////////////// Recebe a linha da tabela selecionada, referente ao numero da sala.
        String nickLogar = (String) entrada.readObject();  /////////////////////////////// ENTRADA 3

        if (numeroSala == -1) {
            saida.writeObject(-1);  /////////////////////////////// Envia estado da sala.
            return;
        }
        if (estaEmSala(nickLogar)) {
            saida.writeObject(-2); // Usuario ja em sala. 
            return;
        }

        saida.writeObject(1); /////////////////////////////// SAIDA 1

        user = listaLogados.get(nickLogar);
        sala = listaSalas.get(numeroSala);
        user.resetInfoSala();
        user.setComecoJogo();
        user.setNumSala(numeroSala);

        if (sala.getUsuario1() == null && sala.getUsuario2() == null) {
            user.setPosicaoSala(1);
            sala.setUsuario1(user);
            saida.writeObject(1);
            listaSalas.set(numeroSala, sala);
            user.setNumSala(numeroSala);
        } else if (sala.getUsuario1() != null && sala.getUsuario2() == null) {
            user.setPosicaoSala(2);
            sala.setUsuario2(user);
            saida.writeObject(2);
            listaSalas.set(numeroSala, sala);
            user.setNumSala(numeroSala);
        } else if (sala.getUsuario1() == null && sala.getUsuario2() != null) {
            user.setPosicaoSala(1);
            sala.setUsuario1(user);
            saida.writeObject(1);
            listaSalas.set(numeroSala, sala);
            user.setNumSala(numeroSala);
        } else {
            saida.writeObject(-1);
        }

        listaLogados.replace(nickLogar, user);

    }

    private void cadastroCliente() throws IOException, ClassNotFoundException {

        String lidoNick = (String) entrada.readObject(); //Recebe do cliente o nick escrito.
        String lidoSenha = (String) entrada.readObject(); //Recebe do cliente a senha escrita.

        if (listaUsuarios.containsKey(lidoNick)) {
            saida.writeObject(2);                   //Envia para usuario codigo 2, dizendo que o usuario ja esta cadastrado.
        } else if (!senhaValida(lidoSenha)) {
            saida.writeObject(3);
        } else {
            saida.writeObject(1);

            String confirmaSenha = (String) entrada.readObject();

            if (confirmaSenha == null) {
                saida.writeObject(-1);
                return;
            }

            if (confirmaSenha == null) {
                return;
            } else if (!confirmaSenha.equals(lidoSenha)) {
                saida.writeObject(2);
            } else {
                saida.writeObject(1);
                this.adicionaUsuario(lidoNick, lidoSenha);
            }

        }

    }

    private void loginCliente() throws IOException, ClassNotFoundException {
        Usuario user;

        String lidoNick = (String) entrada.readObject(); //Recebe do cliente o nick escrito.
        String lidoSenha = (String) entrada.readObject(); //Recebe do cliente a senha escrita.
        if (listaUsuarios.containsKey(lidoNick)) {
            user = listaUsuarios.get(lidoNick);
            if (user.getSenha().equals(lidoSenha)) {

                if (estaListaLogados(lidoNick)) {
                    saida.writeObject(4);
                } else {
                    saida.writeObject(1);   //SUcesso no login.                             
                    this.janelaS.incNumOnline();

                    atualizaSalasJanela();
                    user.setIDConexao((int) entrada.readObject());
                    colocaListaLogados(user);
                }
            } else {
                saida.writeObject(2); //Senha Inválida
            }
        } else {
            saida.writeObject(3); //Usuario não Cadastrado
        }

    }

    private void aguardaAdversario() throws IOException, ClassNotFoundException {
        int IDJogo = (int) entrada.readObject();
        int numSala = -1;
        int posSala = -1;

        for (Map.Entry<String, Usuario> lista : listaLogados.entrySet()) {
            if (lista.getValue().getIDConexaoJogo() == IDJogo) {
                numSala = lista.getValue().getNumSala();
                break;
            }
        }

        if (numSala == -1) {
            saida.writeObject(null);
            return;
        } else {

            if (listaSalas.get(numSala).getUsuario1().getIDConexaoJogo() == IDJogo) {
                posSala = 1;
            } else if (listaSalas.get(numSala).getUsuario2().getIDConexaoJogo() == IDJogo) {
                posSala = 2;
            }

            if (posSala == 1) {
                if (listaSalas.get(numSala).getUsuario2() != null) {
                    saida.writeObject(listaSalas.get(numSala).getUsuario2().getNick());
                    return;
                }
            } else if (posSala == 2) {
                if (listaSalas.get(numSala).getUsuario1() != null) {
                    saida.writeObject(listaSalas.get(numSala).getUsuario1().getNick());
                    return;
                }
            }
        }

        saida.writeObject(null);

    }

    private void salvaIDConexaoJogo(int salvaContadorID) throws IOException, ClassNotFoundException {

        int IDConexao = (int) entrada.readObject();

        for (Sala salas : listaSalas) {

            if (salas.getUsuario1() != null) {
                if (salas.getUsuario1().getIDConexao() == IDConexao) {
                    salas.getUsuario1().setIDConexaoJogo(salvaContadorID);
                    break;
                }
            }
            if (salas.getUsuario2() != null) {
                if (salas.getUsuario2().getIDConexao() == IDConexao) {
                    salas.getUsuario2().setIDConexaoJogo(salvaContadorID);
                    break;
                }
            }
        }
    }

    public void addListaBatalhas(String desafiante, String desafiado, int valorAposta) {

        listaBatalhas.put(desafiante + "vs" + desafiado, new Batalha(listaLogados.get(desafiante), listaLogados.get(desafiado), valorAposta));
    }

    public void removeListaBatalhas(Batalha batalha) {

        listaBatalhas.remove(batalha.getIDBatalha());

    }

    public Batalha getBatalha(String desafiante, String desafiado, String qualquer) {

        if (qualquer != null) {

            for (Map.Entry<String, Batalha> lista : listaBatalhas.entrySet()) {
                if (lista.getValue().getDesafiado() != null) {
                    if (lista.getValue().getDesafiado().getNick().equals(qualquer)) {
                        return lista.getValue();
                    }
                }
                if (lista.getValue().getDesafiante() != null) {
                    if (lista.getValue().getDesafiante().getNick().equals(qualquer)) {
                        return lista.getValue();
                    }
                }
            }
            return null;
        }

        if (desafiado != null && desafiante != null) {
            return listaBatalhas.get(desafiante + "vs" + desafiado);
        } else {

            if (desafiante == null) {

                for (Map.Entry<String, Batalha> lista : listaBatalhas.entrySet()) {
                    if (lista.getValue().getDesafiado() != null) {
                        if (lista.getValue().getDesafiado().getNick().equals(desafiado)) {
                            return lista.getValue();
                        }
                    }
                }

                return null;
            } else if (desafiado == null) {

                for (Map.Entry<String, Batalha> lista : listaBatalhas.entrySet()) {
                    if (lista.getValue().getDesafiante() != null) {
                        if (lista.getValue().getDesafiante().getNick().equals(desafiante)) {
                            return lista.getValue();
                        }
                    }
                }
                return null;
            } else {
                return null;
            }

        }
    }

    private void carregaListaPalavroes() {

        try {
            BufferedReader buffRead = new BufferedReader(new FileReader("Palavras Proibidas.txt"));
            String linha;
            while ((linha = buffRead.readLine()) != null) {
                listaPalavroes.put(linha, linha);
            }

            buffRead.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Nao deu pra ler palaavras proibidas");
        } catch (IOException ex) {
            System.out.println("Nao deu pra ler palaavras proibidas");
        }

    }

    private String contemPalavrao(String frase) {
        int numLetras;
        String asteriscos;
        char cs;
        frase = frase.toLowerCase();
        for (Map.Entry<String, String> lista : listaPalavroes.entrySet()) {
            String palavrao = lista.getValue();
            palavrao = palavrao.toLowerCase();

            if (frase.contains(palavrao)) {
                cs = palavrao.charAt(0);

                asteriscos = "";

                numLetras = palavrao.length() - 1;

                for (int x = 0; x < numLetras; x++) {
                    asteriscos = asteriscos.concat("*");
                }

                frase = frase.replace(palavrao, cs + asteriscos);

                return frase;
            }

        }

        return frase;
    }

}
