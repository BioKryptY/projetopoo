package com.biblioteca.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.biblioteca.model.Usuario;
import com.biblioteca.service.BibliotecaService;

public class TelaEdicaoUsuario extends JFrame {
   private Usuario usuario;
   private Usuario usuarioLogado;
   private JTextField campoNome;
   private JTextField campoEmail;
   private JPasswordField campoSenha;
   private JTextField campoTelefone;
   private JCheckBox checkEspecial;
   private BibliotecaService bibliotecaService;

   public TelaEdicaoUsuario(Usuario usuario, Usuario usuarioLogado) {
      this.usuario = usuario;
      this.usuarioLogado = usuarioLogado;
      try {
         bibliotecaService = new BibliotecaService();
         configurarTela();
         carregarDados();
      } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Erro ao inicializar a tela: " + e.getMessage());
         System.exit(1);
      }
   }

   private void configurarTela() {
      setTitle("Biblioteca - Edição de Usuário");
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

      painelFormulario.add(new JLabel("Usuário Especial:"));
      checkEspecial = new JCheckBox();
      painelFormulario.add(checkEspecial);

      JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
      JButton botaoSalvar = new JButton("Salvar");
      JButton botaoCancelar = new JButton("Cancelar");

      botaoSalvar.addActionListener(x  -> salvarUsuario());
      botaoCancelar.addActionListener(x  -> cancelar());

      painelBotoes.add(botaoSalvar);
      painelBotoes.add(botaoCancelar);

      painelPrincipal.add(painelFormulario, BorderLayout.CENTER);
      painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

      add(painelPrincipal);
   }

   private void carregarDados() {
      campoNome.setText(usuario.getNome());
      campoEmail.setText(usuario.getEmail());
      campoTelefone.setText(usuario.getTelefone());
      checkEspecial.setSelected(usuario.isEspecial());
   }

   private void salvarUsuario() {
      String nome = campoNome.getText();
      String email = campoEmail.getText();
      String senha = new String(campoSenha.getPassword());
      String telefone = campoTelefone.getText();
      boolean especial = checkEspecial.isSelected();

      if (nome.isEmpty() || email.isEmpty()) {
         JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios!");
         return;
      }

      try {
         usuario.setNome(nome);
         usuario.setEmail(email);
         if (!senha.isEmpty()) {
            usuario.setSenha(senha);
         }
         usuario.setTelefone(telefone);
         usuario.setEspecial(especial);

         bibliotecaService.atualizarUsuario(usuario);
         JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!");
         voltar();
      } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Erro ao atualizar usuário: " + e.getMessage());
      }
   }

   private void cancelar() {
      voltar();
   }

   private void voltar() {
      new TelaUsuarioEspecial(usuarioLogado).setVisible(true);
      this.dispose();
   }
}