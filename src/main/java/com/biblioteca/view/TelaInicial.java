package com.biblioteca.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.biblioteca.model.Usuario;
import com.biblioteca.service.BibliotecaService;

public class TelaInicial extends JFrame {
   private static TelaInicial instance;
   private JTextField campoEmail;
   private JPasswordField campoSenha;
   private JButton botaoLogin;
   private JButton botaoCadastro;
   private BibliotecaService bibliotecaService;

   public TelaInicial() {
      try {
         bibliotecaService = new BibliotecaService();
         configurarTela();
         instance = this;
      } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Erro ao inicializar o sistema: " + e.getMessage());
         System.exit(1);
      }
   }

   public static TelaInicial getInstance() {
      if (instance == null) {
         instance = new TelaInicial();
      }
      return instance;
   }

   private void configurarTela() {
      setTitle("Biblioteca - Login");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(335, 290);
      setLocationRelativeTo(null);

      JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
      painelPrincipal.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

      // Adiciona título do sistema
      JLabel labelTitulo = new JLabel("Sistema Bibliotecário", JLabel.CENTER);
      labelTitulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
      JPanel painelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
      painelTitulo.add(labelTitulo);
      painelPrincipal.add(painelTitulo, BorderLayout.NORTH);

      JPanel painelLogin = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
      painelLogin.setLayout(new BoxLayout(painelLogin, BoxLayout.Y_AXIS));

      // Painel para email
      JPanel painelEmail = new JPanel(new FlowLayout(FlowLayout.LEFT));
      painelEmail.add(new JLabel("Email:"));
      campoEmail = new JTextField(20);
      campoEmail.setPreferredSize(new java.awt.Dimension(200, 30));
      painelEmail.add(campoEmail);
      painelLogin.add(painelEmail);

      // Painel para senha
      JPanel painelSenha = new JPanel(new FlowLayout(FlowLayout.LEFT));
      painelSenha.add(new JLabel("Senha:"));
      campoSenha = new JPasswordField(20);
      campoSenha.setPreferredSize(new java.awt.Dimension(200, 30));
      painelSenha.add(campoSenha);
      painelLogin.add(painelSenha);

      JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
      botaoLogin = new JButton("Login");
      botaoCadastro = new JButton("Cadastrar");

      botaoLogin.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            realizarLogin();
         }
      });

      botaoCadastro.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            abrirTelaCadastro();
         }
      });

      painelBotoes.add(botaoLogin);
      painelBotoes.add(botaoCadastro);

      painelPrincipal.add(painelLogin, BorderLayout.CENTER);
      painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

      add(painelPrincipal);
   }

   private void realizarLogin() {
      String email = campoEmail.getText();
      String senha = new String(campoSenha.getPassword());

      try {
         Usuario usuario = bibliotecaService.buscarUsuarioPorEmail(email);

         if (usuario != null && bibliotecaService.verificarSenha(senha, usuario.getSenha())) {
            if (usuario.isEspecial()) {
               new TelaUsuarioEspecial(usuario).setVisible(true);
            } else {
               new TelaUsuarioComum(usuario).setVisible(true);
            }
            this.dispose();
         } else {
            JOptionPane.showMessageDialog(this, "Email ou senha inválidos!");
         }
      } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Erro ao realizar login: " + e.getMessage());
      }
   }

   private void abrirTelaCadastro() {
      this.setVisible(false);
      TelaCadastroUsuario telaCadastro = new TelaCadastroUsuario(null);
      telaCadastro.setLocationRelativeTo(null);
      telaCadastro.setVisible(true);
   }

}