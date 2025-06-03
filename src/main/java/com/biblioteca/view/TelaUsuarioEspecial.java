package com.biblioteca.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Item;
import com.biblioteca.model.Usuario;
import com.biblioteca.service.BibliotecaService;

public class TelaUsuarioEspecial extends JFrame {
   private Usuario usuario;
   private BibliotecaService bibliotecaService;
   private JTable tabelaItens;
   private JTable tabelaUsuarios;
   private JTable tabelaEmprestimos;
   private JTextField campoBusca;
   private DefaultTableModel modeloItens;
   private DefaultTableModel modeloUsuarios;
   private DefaultTableModel modeloEmprestimos;

   public TelaUsuarioEspecial(Usuario usuario) {
      this.usuario = usuario;
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
      setTitle("Biblioteca - Administração");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(1000, 800);
      setLocationRelativeTo(null);

      JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
      painelPrincipal.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

      // Painel superior que contém a label do usuário e a busca
      JPanel painelSuperior = new JPanel(new BorderLayout());

      // Adiciona label com nome do usuário
      JLabel labelUsuario = new JLabel("Olá, " + usuario.getNome());
      labelUsuario.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
      JPanel painelUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT));
      painelUsuario.add(labelUsuario);
      painelSuperior.add(painelUsuario, BorderLayout.NORTH);

      JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
      campoBusca = new JTextField(20);
      JButton botaoBuscar = new JButton("Buscar");
      botaoBuscar.addActionListener(x -> buscarItens());
      painelBusca.add(new JLabel("Buscar por título:"));
      painelBusca.add(campoBusca);
      painelBusca.add(botaoBuscar);
      painelSuperior.add(painelBusca, BorderLayout.CENTER);

      painelPrincipal.add(painelSuperior, BorderLayout.NORTH);

      String[] colunasItens = { "ID", "Título", "Tipo", "Disponível", "Localização" };
      modeloItens = new DefaultTableModel(colunasItens, 0);
      tabelaItens = new JTable(modeloItens);
      JScrollPane scrollItens = new JScrollPane(tabelaItens);

      String[] colunasUsuarios = { "ID", "Nome", "Email", "Tipo" };
      modeloUsuarios = new DefaultTableModel(colunasUsuarios, 0);
      tabelaUsuarios = new JTable(modeloUsuarios);
      JScrollPane scrollUsuarios = new JScrollPane(tabelaUsuarios);

      String[] colunasEmprestimos = { "ID", "Usuário", "Itens", "Data Empréstimo", "Data Prevista Devolução" };
      modeloEmprestimos = new DefaultTableModel(colunasEmprestimos, 0);
      tabelaEmprestimos = new JTable(modeloEmprestimos);
      JScrollPane scrollEmprestimos = new JScrollPane(tabelaEmprestimos);

      JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
      JButton botaoCadastrarItem = new JButton("Cadastrar Item");
      JButton botaoEditarItem = new JButton("Editar Item");
      JButton botaoExcluirItem = new JButton("Excluir Item");
      JButton botaoCadastrarUsuario = new JButton("Cadastrar Usuário");
      JButton botaoEditarUsuario = new JButton("Editar Usuário");
      JButton botaoExcluirUsuario = new JButton("Excluir Usuário");
      JButton botaoSair = new JButton("Sair");

      botaoCadastrarItem.addActionListener(x -> cadastrarItem());
      botaoEditarItem.addActionListener(x -> editarItem());
      botaoExcluirItem.addActionListener(x -> excluirItem());
      botaoCadastrarUsuario.addActionListener(x -> cadastrarUsuario());
      botaoEditarUsuario.addActionListener(x -> editarUsuario());
      botaoExcluirUsuario.addActionListener(x -> excluirUsuario());
      botaoSair.addActionListener(x -> sair());

      painelBotoes.add(botaoCadastrarItem);
      painelBotoes.add(botaoEditarItem);
      painelBotoes.add(botaoExcluirItem);
      painelBotoes.add(botaoCadastrarUsuario);
      painelBotoes.add(botaoEditarUsuario);
      painelBotoes.add(botaoExcluirUsuario);
      painelBotoes.add(botaoSair);

      JPanel painelTabelas = new JPanel(new GridLayout(3, 1, 10, 10));

      // Painel para tabela de itens
      JPanel painelItens = new JPanel(new BorderLayout(5, 5));
      JLabel labelItens = new JLabel("Livros da Biblioteca");
      labelItens.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
      painelItens.add(labelItens, BorderLayout.NORTH);
      painelItens.add(scrollItens, BorderLayout.CENTER);

      // Painel para tabela de usuários
      JPanel painelUsuarios = new JPanel(new BorderLayout(5, 5));
      JLabel labelUsuarios = new JLabel("Usuários Cadastrados");
      labelUsuarios.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
      painelUsuarios.add(labelUsuarios, BorderLayout.NORTH);
      painelUsuarios.add(scrollUsuarios, BorderLayout.CENTER);

      // Painel para tabela de empréstimos
      JPanel painelEmprestimos = new JPanel(new BorderLayout(5, 5));
      JLabel labelEmprestimos = new JLabel("Empréstimos Ativos");
      labelEmprestimos.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
      painelEmprestimos.add(labelEmprestimos, BorderLayout.NORTH);
      painelEmprestimos.add(scrollEmprestimos, BorderLayout.CENTER);

      painelTabelas.add(painelItens);
      painelTabelas.add(painelUsuarios);
      painelTabelas.add(painelEmprestimos);

      painelPrincipal.add(painelTabelas, BorderLayout.CENTER);
      painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

      add(painelPrincipal);
   }

   private void carregarDados() {
      try {
         List<Item> itens = bibliotecaService.listarItens();
         modeloItens.setRowCount(0);
         for (Item item : itens) {
            modeloItens.addRow(new Object[] {
                  item.getId(),
                  item.getTitulo(),
                  item.getTipo(),
                  item.isDisponivel() ? "Sim" : "Não",
                  item.getLocalizacao() != null ? item.getLocalizacao().getEstante() + " - " +
                        item.getLocalizacao().getPratileira() + " - " +
                        item.getLocalizacao().getSecao() : "Não definida"
            });
         }

         List<Usuario> usuarios = bibliotecaService.listarUsuarios();
         modeloUsuarios.setRowCount(0);
         for (Usuario usuario : usuarios) {
            modeloUsuarios.addRow(new Object[] {
                  usuario.getId(),
                  usuario.getNome(),
                  usuario.getEmail(),
                  usuario.isEspecial() ? "Especial" : "Comum"
            });
         }

         List<Emprestimo> emprestimos = bibliotecaService.listarEmprestimosAtivos();
         modeloEmprestimos.setRowCount(0);
         for (Emprestimo emprestimo : emprestimos) {
            StringBuilder itensFormatados = new StringBuilder();
            List<Item> itensDoEmprestimo = emprestimo.getItens();
            for (int i = 0; i < itensDoEmprestimo.size(); i++) {
               itensFormatados.append(itensDoEmprestimo.get(i).getTitulo());
               if (i < itensDoEmprestimo.size() - 1) {
                  itensFormatados.append(", ");
               }
            }

            String dataEmprestimoFormatada = emprestimo.getDataEmprestimo().toLocalDate().toString();

            String dataPrevistaDevolucao = emprestimo.getDataEmprestimo().plusDays(15).toLocalDate().toString();

            modeloEmprestimos.addRow(new Object[] {
                  emprestimo.getId(),
                  emprestimo.getUsuario().getNome(),
                  itensFormatados.toString(),
                  dataEmprestimoFormatada,
                  dataPrevistaDevolucao
            });
         }
      } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage());
      }
   }

   private void buscarItens() {
      String titulo = campoBusca.getText();
      try {
         List<Item> itens = bibliotecaService.buscarItensPorTitulo(titulo);
         modeloItens.setRowCount(0);
         for (Item item : itens) {
            modeloItens.addRow(new Object[] {
                  item.getId(),
                  item.getTitulo(),
                  item.getTipo(),
                  item.isDisponivel() ? "Sim" : "Não",
                  item.getLocalizacao() != null ? item.getLocalizacao().getEstante() + " - " +
                        item.getLocalizacao().getPratileira() + " - " +
                        item.getLocalizacao().getSecao() : "Não definida"
            });
         }
      } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Erro ao buscar itens: " + e.getMessage());
      }
   }

   private void cadastrarItem() {
      new TelaCadastroItem(usuario).setVisible(true);
      this.dispose();
   }

   private void editarItem() {
      int linha = tabelaItens.getSelectedRow();
      if (linha == -1) {
         JOptionPane.showMessageDialog(this, "Selecione um item para editar!");
         return;
      }

      try {
         Long idItem = (Long) modeloItens.getValueAt(linha, 0);
         Item item = bibliotecaService.buscarItemPorId(idItem);
         new TelaEdicaoItem(item, usuario).setVisible(true);
         this.dispose();
      } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Erro ao editar item: " + e.getMessage());
      }
   }

   private void excluirItem() {
      int linha = tabelaItens.getSelectedRow();
      if (linha == -1) {
         JOptionPane.showMessageDialog(this, "Selecione um item para excluir!");
         return;
      }

      int confirmacao = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja excluir este item?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION);

      if (confirmacao == JOptionPane.YES_OPTION) {
         try {
            Long idItem = (Long) modeloItens.getValueAt(linha, 0);
            bibliotecaService.excluirItem(idItem);
            JOptionPane.showMessageDialog(this, "Item excluído com sucesso!");
            carregarDados();
         } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir item: " + e.getMessage());
         }
      }
   }

   private void cadastrarUsuario() {
      new TelaCadastroUsuario(usuario).setVisible(true);
      this.dispose();
   }

   private void editarUsuario() {
      int linha = tabelaUsuarios.getSelectedRow();
      if (linha == -1) {
         JOptionPane.showMessageDialog(this, "Selecione um usuário para editar!");
         return;
      }

      try {
         Long idUsuario = (Long) modeloUsuarios.getValueAt(linha, 0);
         Usuario usuario = bibliotecaService.buscarUsuarioPorId(idUsuario);
         new TelaEdicaoUsuario(usuario, this.usuario).setVisible(true);
         this.dispose();
      } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Erro ao editar usuário: " + e.getMessage());
      }
   }

   private void excluirUsuario() {
      int linha = tabelaUsuarios.getSelectedRow();
      if (linha == -1) {
         JOptionPane.showMessageDialog(this, "Selecione um usuário para excluir!");
         return;
      }

      int confirmacao = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja excluir este usuário?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION);

      if (confirmacao == JOptionPane.YES_OPTION) {
         try {
            Long idUsuario = (Long) modeloUsuarios.getValueAt(linha, 0);
            bibliotecaService.excluirUsuario(idUsuario);
            JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso!");
            carregarDados();
         } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir usuário: " + e.getMessage());
         }
      }
   }

   private void sair() {
      new TelaInicial().setVisible(true);
      this.dispose();
   }
}