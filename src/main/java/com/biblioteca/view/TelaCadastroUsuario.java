package com.biblioteca.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Calendar;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.biblioteca.model.Usuario;
import com.biblioteca.model.UsuarioComum;
import com.biblioteca.model.UsuarioEspecial;
import com.biblioteca.service.BibliotecaService;

public class TelaCadastroUsuario extends JFrame {
   private JTextField campoNome;
   private JTextField campoEmail;
   private JPasswordField campoSenha;
   private JTextField campoTelefone;
   private JPasswordField campoSenhaMaster;
   private JCheckBox checkEspecial;
   private BibliotecaService bibliotecaService;
   private Usuario usuarioLogado;

   public TelaCadastroUsuario(Usuario usuarioLogado) {
      this.usuarioLogado = usuarioLogado;
      try {
         bibliotecaService = new BibliotecaService();
         configurarTela();
      } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Erro ao inicializar a tela: " + e.getMessage());
         System.exit(1);
      }
   }

   private void configurarTela() {
      setTitle("Biblioteca - Cadastro de Usuário");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(400, 300);
      setLocationRelativeTo(null);

      JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
      painelPrincipal.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

      JPanel painelFormulario = new JPanel(new GridLayout(5, 2, 5, 5));

      painelFormulario.add(new JLabel("Nome:"));
      campoNome = new JTextField();
      painelFormulario.add(campoNome);

      painelFormulario.add(new JLabel("Email:"));
      campoEmail = new JTextField();
      painelFormulario.add(campoEmail);

      painelFormulario.add(new JLabel("Senha:"));
      campoSenha = new JPasswordField();
      painelFormulario.add(campoSenha);

      painelFormulario.add(new JLabel("Telefone:"));
      campoTelefone = new JTextField();
      painelFormulario.add(campoTelefone);

      if (usuarioLogado == null) {
         painelFormulario.add(new JLabel("Senha Master:"));
         campoSenhaMaster = new JPasswordField();
         painelFormulario.add(campoSenhaMaster);
         ((GridLayout) painelFormulario.getLayout()).setRows(6);
      }

      painelFormulario.add(new JLabel("Usuário Especial:"));
      checkEspecial = new JCheckBox();
      painelFormulario.add(checkEspecial);

      JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
      JButton botaoSalvar = new JButton("Salvar");
      JButton botaoCancelar = new JButton("Cancelar");

      botaoSalvar.addActionListener(x -> salvarUsuario());
      botaoCancelar.addActionListener(x -> cancelar());

      painelBotoes.add(botaoSalvar);
      painelBotoes.add(botaoCancelar);

      painelPrincipal.add(painelFormulario, BorderLayout.CENTER);
      painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

      add(painelPrincipal);
   }

   private void salvarUsuario() {
      String nome = campoNome.getText();
      String email = campoEmail.getText();
      String senha = new String(campoSenha.getPassword());
      String telefone = campoTelefone.getText();
      boolean especial = checkEspecial.isSelected();

      if (usuarioLogado == null) {
         String senhaMaster = new String(campoSenhaMaster.getPassword());
         if (!senhaMaster.equals("puc123456")) {
            JOptionPane.showMessageDialog(this, "Senha master incorreta!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
         }
      }

      if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
         JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
         return;
      }

      try {
         Usuario usuario;
         if (especial) {
            usuario = new UsuarioEspecial();
            String codigo = "PUC_" + System.currentTimeMillis() % 10000;
            ((UsuarioEspecial) usuario).setCodigo(codigo);
         } else {
            usuario = new UsuarioComum();
            String anoAtual = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            String semestre = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) / 6 + 1);
            String random = String.valueOf(new Random().nextInt(999999));
            String matricula = anoAtual + semestre + random;
            ((UsuarioComum) usuario).setMatricula(matricula);
         }

         usuario.setNome(nome);
         usuario.setEmail(email);
         usuario.setSenha(senha);
         usuario.setTelefone(telefone);
         usuario.setEspecial(especial);

         bibliotecaService.cadastrarUsuario(usuario);
         JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
         voltar();
      } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário: " + e.getMessage());
      }
   }

   private void cancelar() {
      voltar();
   }

   private void voltar() {
      if (usuarioLogado == null) {
         new TelaInicial().setVisible(true);
      } else {
         new TelaUsuarioEspecial(usuarioLogado).setVisible(true);
      }
      this.dispose();
   }
}