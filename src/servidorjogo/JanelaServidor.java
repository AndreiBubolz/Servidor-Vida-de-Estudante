/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorjogo;

import java.awt.Color;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andreibubolz
 */
public class JanelaServidor extends javax.swing.JFrame {
    private ServerSocket servidor;
    private final static int PORT = 12345;
    private static int numeroConectados;
    private boolean servidorOnline;
    private Thread threadServidor;
    
    private Color VERDE = new Color(0,153,0);
   
    public JanelaServidor() {
        
        initComponents();
        this.textoNumConectados.setVisible(false);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textoOnlineOffline = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        botaoIniciarPararServidor = new javax.swing.JButton();
        textoNumConectados = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);

        textoOnlineOffline.setFont(new java.awt.Font("Unispace", 1, 15)); // NOI18N
        textoOnlineOffline.setForeground(new java.awt.Color(255, 0, 0));
        textoOnlineOffline.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textoOnlineOffline.setText("Servidor Offline");

        botaoIniciarPararServidor.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        botaoIniciarPararServidor.setText("Iniciar Servidor");
        botaoIniciarPararServidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoIniciarPararServidorActionPerformed(evt);
            }
        });

        textoNumConectados.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        textoNumConectados.setForeground(new java.awt.Color(0, 0, 204));
        textoNumConectados.setText("0");
        textoNumConectados.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(botaoIniciarPararServidor, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(textoOnlineOffline)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(textoNumConectados, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(textoOnlineOffline, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(textoNumConectados))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botaoIniciarPararServidor, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botaoIniciarPararServidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoIniciarPararServidorActionPerformed
        if(this.botaoIniciarPararServidor.getText().equals("Iniciar Servidor")){
            IniciaServidor iniciaS = new IniciaServidor(this);
            this.textoOnlineOffline.setText("Servidor Online");
            this.threadServidor = new Thread(iniciaS);
            this.threadServidor.start();
        }
        else if(this.botaoIniciarPararServidor.getText().equals("Parar Servidor")){
            this.servidorOnline = false;
            this.textoOnlineOffline.setText("Servidor Offline");
            this.textoOnlineOffline.setForeground(Color.RED);
            this.numeroConectados = 0;
            this.textoNumConectados.setText(String.valueOf(this.numeroConectados));
            this.textoNumConectados.setVisible(false);
            this.botaoIniciarPararServidor.setText("Iniciar Servidor");
            try {
                this.servidor.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
    }//GEN-LAST:event_botaoIniciarPararServidorActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JanelaServidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JanelaServidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JanelaServidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JanelaServidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JanelaServidor().setVisible(true);
            }
        });
    }
    
    public class IniciaServidor implements Runnable{
        private JanelaServidor janelaS;
        private ServidorJogo novoTratamento ;
        
        public IniciaServidor(JanelaServidor janela){
         this.janelaS = janela;   
        }
        
        public void run() {
            janelaS.textoOnlineOffline.setText("Servidor Online");
            janelaS.textoOnlineOffline.setForeground(VERDE);
            janelaS.textoNumConectados.setText(String.valueOf(this.janelaS.numeroConectados));
            janelaS.textoNumConectados.setVisible(true);
            janelaS.botaoIniciarPararServidor.setText("Parar Servidor");
            
            servidorOnline = true;
            try {
                this.janelaS.servidor = new ServerSocket(PORT);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            while (servidorOnline) {
                try {
                    
                    Socket conexao = this.janelaS.servidor.accept();
                    novoTratamento = new ServidorJogo(conexao,janelaS);
                    
                    Thread t = new Thread(novoTratamento);
                    
                    t.start();

                    } catch (IOException ex) {
                        break;
                    }
            
            }
            try {
                janelaS.servidor.close();
                novoTratamento.fechaEntradaSaida();
                
            } catch (IOException ex) {
                //ex.printStackTrace();
            }
        }
    }

    public void decNumOnline(){
        
        if(numeroConectados>0)
            numeroConectados--;
        else
            numeroConectados = 0;
        
        this.textoNumConectados.setText(String.valueOf(numeroConectados));
        
    }
    
    public void incNumOnline(){
        
        numeroConectados++;
        this.textoNumConectados.setText(String.valueOf(numeroConectados));
        
    }
    
    public int getNumConectados(){
        return numeroConectados;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botaoIniciarPararServidor;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel textoNumConectados;
    private javax.swing.JLabel textoOnlineOffline;
    // End of variables declaration//GEN-END:variables
}
