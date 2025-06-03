package com.biblioteca.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.biblioteca.model.Item;
import com.biblioteca.model.Livro;
import com.biblioteca.model.Localizacao;
import com.biblioteca.model.Midia;
import com.biblioteca.model.Monografia;
import com.biblioteca.model.Periodico;
import com.biblioteca.model.Usuario;
import com.biblioteca.service.BibliotecaService;

public class TelaEdicaoItem extends JFrame {
   private Item item;
   private Usuario usuario;
   private JTextField campoTitulo;
   private JComboBox<String> comboTipo;
   private JTextField campoEstante;
   private JTextField campoPratileira;
   private JTextField campoSecao;
   private JTextField campoAnoPublicacao;

   private JTextField campoIsbn;
   private JTextField campoEditora;

   private JTextField campoIssn;

   private JTextField campoDoi;
   private JTextField campoUrl;

   private JTextField campoIdentificador;
   private JTextField campoAutores;

   private JPanel painelEspecifico;
   private BibliotecaService bibliotecaService;

   public TelaEdicaoItem(Item item, Usuario usuario) {
      this.item = item;
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
      setTitle("Biblioteca - Edição de Item");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(500, 600);
      setLocationRelativeTo(null);

      JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
      painelPrincipal.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

      JPanel painelFormulario = new JPanel(new GridLayout(6, 2, 5, 5));

      painelFormulario.add(new JLabel("Título:"));
      campoTitulo = new JTextField();
      painelFormulario.add(campoTitulo);

      painelFormulario.add(new JLabel("Tipo:"));
      String[] tipos = { "LIVRO", "PERIODICO", "MIDIA", "MONOGRAFIA" };
      comboTipo = new JComboBox<>(tipos);
      comboTipo.setEnabled(false);
      painelFormulario.add(comboTipo);

      painelFormulario.add(new JLabel("Estante:"));
      campoEstante = new JTextField();
      painelFormulario.add(campoEstante);

      painelFormulario.add(new JLabel("Pratileira:"));
      campoPratileira = new JTextField();
      painelFormulario.add(campoPratileira);

      painelFormulario.add(new JLabel("Seção:"));
      campoSecao = new JTextField();
      painelFormulario.add(campoSecao);

      painelFormulario.add(new JLabel("Ano de Publicação:"));
      campoAnoPublicacao = new JTextField();
      painelFormulario.add(campoAnoPublicacao);

      painelEspecifico = new JPanel(new GridLayout(0, 2, 5, 5));
      atualizarPainelEspecifico();

      JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
      JButton botaoSalvar = new JButton("Salvar");
      JButton botaoCancelar = new JButton("Cancelar");

      botaoSalvar.addActionListener(x -> salvarItem());
      botaoCancelar.addActionListener(x -> cancelar());

      painelBotoes.add(botaoSalvar);
      painelBotoes.add(botaoCancelar);

      painelPrincipal.add(painelFormulario, BorderLayout.NORTH);
      painelPrincipal.add(painelEspecifico, BorderLayout.CENTER);
      painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

      add(painelPrincipal);
   }

   private void carregarDados() {
      campoTitulo.setText(item.getTitulo());
      comboTipo.setSelectedItem(item.getTipo());

      if (item.getLocalizacao() != null) {
         campoEstante.setText(item.getLocalizacao().getEstante());
         campoPratileira.setText(item.getLocalizacao().getPratileira());
         campoSecao.setText(item.getLocalizacao().getSecao());
      }

      if (item.getDataPublicacao() != null) {
         campoAnoPublicacao.setText(String.valueOf(item.getDataPublicacao().getYear()));
      } else {
         campoAnoPublicacao.setText("");
      }

      switch (item.getTipo()) {
         case "LIVRO":
            Livro livro = (Livro) item;
            campoIsbn.setText(livro.getIsbn());
            campoEditora.setText(livro.getEditora());
            break;

         case "PERIODICO":
            Periodico periodico = (Periodico) item;
            campoIssn.setText(periodico.getIssn());
            campoEditora.setText(periodico.getEditora());
            break;

         case "MIDIA":
            Midia midia = (Midia) item;
            campoDoi.setText(midia.getDoi());
            campoUrl.setText(midia.getUrl());
            campoEditora.setText(midia.getEditora());
            break;

         case "MONOGRAFIA":
            Monografia monografia = (Monografia) item;
            campoIdentificador.setText(monografia.getIdentificador());
            campoAutores.setText(String.join(", ", monografia.getAutores()));
            break;
      }
   }

   private void atualizarPainelEspecifico() {
      painelEspecifico.removeAll();
      String tipo = item.getTipo();

      switch (tipo) {
         case "LIVRO":
            painelEspecifico.add(new JLabel("ISBN:"));
            campoIsbn = new JTextField();
            painelEspecifico.add(campoIsbn);

            painelEspecifico.add(new JLabel("Editora:"));
            campoEditora = new JTextField();
            painelEspecifico.add(campoEditora);
            break;

         case "PERIODICO":
            painelEspecifico.add(new JLabel("ISSN:"));
            campoIssn = new JTextField();
            painelEspecifico.add(campoIssn);

            painelEspecifico.add(new JLabel("Editora:"));
            campoEditora = new JTextField();
            painelEspecifico.add(campoEditora);
            break;

         case "MIDIA":
            painelEspecifico.add(new JLabel("DOI:"));
            campoDoi = new JTextField();
            painelEspecifico.add(campoDoi);

            painelEspecifico.add(new JLabel("URL:"));
            campoUrl = new JTextField();
            painelEspecifico.add(campoUrl);

            painelEspecifico.add(new JLabel("Editora:"));
            campoEditora = new JTextField();
            painelEspecifico.add(campoEditora);
            break;

         case "MONOGRAFIA":
            painelEspecifico.add(new JLabel("Identificador:"));
            campoIdentificador = new JTextField();
            painelEspecifico.add(campoIdentificador);

            painelEspecifico.add(new JLabel("Autores (separados por vírgula):"));
            campoAutores = new JTextField();
            painelEspecifico.add(campoAutores);
            break;
      }

      painelEspecifico.revalidate();
      painelEspecifico.repaint();
   }

   private void salvarItem() {
      String titulo = campoTitulo.getText();
      String estante = campoEstante.getText();
      String pratileira = campoPratileira.getText();
      String secao = campoSecao.getText();
      int anoPublicacao = Integer.parseInt(campoAnoPublicacao.getText());

      if (titulo.isEmpty() || estante.isEmpty() || pratileira.isEmpty() || secao.isEmpty()) {
         JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios!");
         return;
      }

      try {
         Localizacao localizacao = new Localizacao();
         localizacao.setEstante(estante);
         localizacao.setPratileira(pratileira);
         localizacao.setSecao(secao);

         if (item.getLocalizacao() != null) {
            localizacao.setId(item.getLocalizacao().getId());
         }

         switch (item.getTipo()) {
            case "LIVRO":
               Livro livro = (Livro) item;
               livro.setIsbn(campoIsbn.getText());
               livro.setEditora(campoEditora.getText());
               break;

            case "PERIODICO":
               Periodico periodico = (Periodico) item;
               periodico.setIssn(campoIssn.getText());
               periodico.setEditora(campoEditora.getText());
               break;

            case "MIDIA":
               Midia midia = (Midia) item;
               midia.setDoi(campoDoi.getText());
               midia.setUrl(campoUrl.getText());
               midia.setEditora(campoEditora.getText());
               break;

            case "MONOGRAFIA":
               Monografia monografia = (Monografia) item;
               monografia.setIdentificador(campoIdentificador.getText());
               monografia.getAutores().clear();
               String[] autores = campoAutores.getText().split(",");
               for (String autor : autores) {
                  monografia.adicionarAutor(autor.trim());
               }
               break;
         }

         item.setTitulo(titulo);
         item.setDataPublicacao(LocalDate.of(anoPublicacao, 1, 1));
         item.setLocalizacao(localizacao);

         bibliotecaService.atualizarItem(item);
         JOptionPane.showMessageDialog(this, "Item atualizado com sucesso!");
         voltar();
      } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Erro ao atualizar item: " + e.getMessage());
      }
   }

   private void cancelar() {
      voltar();
   }

   private void voltar() {
      new TelaUsuarioEspecial(usuario).setVisible(true);
      this.dispose();
   }
}