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

public class TelaUsuarioComum extends JFrame {
   private Usuario usuario;
   private BibliotecaService bibliotecaService;
   private JTable tabelaItens;
   private JTable tabelaEmprestimos;
   private JTextField campoBusca;
   private DefaultTableModel modeloItens;
   private DefaultTableModel modeloEmprestimos;

   public TelaUsuarioComum(Usuario usuario) {
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
      setTitle("Biblioteca - Usuário Comum");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(800, 600);
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
      botaoBuscar.addActionListener(_  -> buscarItens());
      painelBusca.add(new JLabel("Buscar por título:"));
      painelBusca.add(campoBusca);
      painelBusca.add(botaoBuscar);
      painelSuperior.add(painelBusca, BorderLayout.CENTER);

      painelPrincipal.add(painelSuperior, BorderLayout.NORTH);

      String[] colunasItens = { "ID", "Título", "Tipo", "Disponível" };
      modeloItens = new DefaultTableModel(colunasItens, 0);
      tabelaItens = new JTable(modeloItens);
      JScrollPane scrollItens = new JScrollPane(tabelaItens);

      String[] colunasEmprestimos = { "ID", "Item", "Data Empréstimo", "Data Prevista Devolução" };
      modeloEmprestimos = new DefaultTableModel(colunasEmprestimos, 0);
      tabelaEmprestimos = new JTable(modeloEmprestimos);
      JScrollPane scrollEmprestimos = new JScrollPane(tabelaEmprestimos);

      JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
      JButton botaoEmprestar = new JButton("Pegar Item Emprestado");
      JButton botaoDevolver = new JButton("Devolver Item");
      JButton botaoSair = new JButton("Sair");

      botaoEmprestar.addActionListener(_  -> emprestarItem());
      botaoDevolver.addActionListener(_  -> devolverItem());
      botaoSair.addActionListener(_  -> sair());

      painelBotoes.add(botaoEmprestar);
      painelBotoes.add(botaoDevolver);
      painelBotoes.add(botaoSair);

      JPanel painelTabelas = new JPanel(new GridLayout(2, 1, 10, 10));

      // Painel para tabela de itens
      JPanel painelItens = new JPanel(new BorderLayout(5, 5));
      JLabel labelItens = new JLabel("Livros da Biblioteca");
      labelItens.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
      painelItens.add(labelItens, BorderLayout.NORTH);
      painelItens.add(scrollItens, BorderLayout.CENTER);

      // Painel para tabela de empréstimos
      JPanel painelEmprestimos = new JPanel(new BorderLayout(5, 5));
      JLabel labelEmprestimos = new JLabel("Empréstimos Ativos");
      labelEmprestimos.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
      painelEmprestimos.add(labelEmprestimos, BorderLayout.NORTH);
      painelEmprestimos.add(scrollEmprestimos, BorderLayout.CENTER);

      painelTabelas.add(painelItens);
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
                  item.isDisponivel() ? "Sim" : "Não"
            });
         }

         List<Emprestimo> emprestimos = bibliotecaService.buscarEmprestimosAtivosPorUsuario(usuario.getId());
         modeloEmprestimos.setRowCount(0);
         for (Emprestimo emprestimo : emprestimos) {
            StringBuilder itensFormatados = new StringBuilder();
            for (int i = 0; i < emprestimo.getItens().size(); i++) {
               itensFormatados.append(emprestimo.getItens().get(i).getTitulo());
               if (i < emprestimo.getItens().size() - 1) {
                  itensFormatados.append(", ");
               }
            }

            String dataEmprestimoFormatada = emprestimo.getDataEmprestimo().toLocalDate().toString();

            String dataPrevistaDevolucao = emprestimo.getDataEmprestimo().plusDays(15).toLocalDate().toString();

            modeloEmprestimos.addRow(new Object[] {
                  emprestimo.getId(),
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
                  item.isDisponivel() ? "Sim" : "Não"
            });
         }
      } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Erro ao buscar itens: " + e.getMessage());
      }
   }

   private void emprestarItem() {
      int[] linhasSelecionadas = tabelaItens.getSelectedRows();
      if (linhasSelecionadas.length == 0) {
         JOptionPane.showMessageDialog(this, "Selecione pelo menos um item para pegar emprestado!");
         return;
      }

      try {
         List<Item> itensParaEmprestar = new java.util.ArrayList<>();
         for (int linha : linhasSelecionadas) {
            Long idItem = (Long) modeloItens.getValueAt(linha, 0);
            Item item = bibliotecaService.buscarItemPorId(idItem);
            if (item != null) {
               itensParaEmprestar.add(item);
            }
         }

         if (itensParaEmprestar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum item válido selecionado para empréstimo.");
            return;
         }

         for (Item item : itensParaEmprestar) {
            if (!item.isDisponivel()) {
               JOptionPane.showMessageDialog(this,
                     "O item \"" + item.getTitulo() + "\" não está disponível para empréstimo!");
               return;
            }
         }

         bibliotecaService.realizarEmprestimo(usuario, itensParaEmprestar);
         JOptionPane.showMessageDialog(this, "Empréstimo realizado com sucesso!");
         carregarDados();
      } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Erro ao pegar item emprestado: " + e.getMessage());
      }
   }

   private void devolverItem() {
      int linha = tabelaEmprestimos.getSelectedRow();
      if (linha == -1) {
         JOptionPane.showMessageDialog(this, "Selecione um empréstimo para devolver!");
         return;
      }

      try {
         Long idEmprestimo = (Long) modeloEmprestimos.getValueAt(linha, 0);

         Emprestimo emprestimo = bibliotecaService.buscarEmprestimoPorId(idEmprestimo);

         if (emprestimo != null && !emprestimo.getItens().isEmpty()) {

            Item itemParaDevolver = emprestimo.getItens().get(0);
            bibliotecaService.realizarDevolucao(itemParaDevolver);
            JOptionPane.showMessageDialog(this, "Item devolvido com sucesso!");
            carregarDados();
         } else {
            JOptionPane.showMessageDialog(this, "Não foi possível encontrar o empréstimo ou ele não contém itens.");
         }
      } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Erro ao devolver item: " + e.getMessage());
      }
   }

   private void sair() {
      new TelaInicial().setVisible(true);
      this.dispose();
   }
}