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
import com.biblioteca.model.StatusItem;
import com.biblioteca.model.Usuario;
import com.biblioteca.service.BibliotecaService;

public class TelaCadastroItem extends JFrame {
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

   public TelaCadastroItem(Usuario usuario) {
      this.usuario = usuario;
      try {
         bibliotecaService = new BibliotecaService();
         configurarTela();
      } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Erro ao inicializar a tela: " + e.getMessage());
         System.exit(1);
      }
   }

   private void configurarTela() {
      setTitle("Biblioteca - Cadastro de Item");
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
      comboTipo.addActionListener(e -> atualizarPainelEspecifico());
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

      botaoSalvar.addActionListener(e -> salvarItem());
      botaoCancelar.addActionListener(e -> cancelar());

      painelBotoes.add(botaoSalvar);
      painelBotoes.add(botaoCancelar);

      painelPrincipal.add(painelFormulario, BorderLayout.NORTH);
      painelPrincipal.add(painelEspecifico, BorderLayout.CENTER);
      painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

      add(painelPrincipal);
   }

   private void atualizarPainelEspecifico() {
      painelEspecifico.removeAll();
      String tipo = (String) comboTipo.getSelectedItem();

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
      String tipo = (String) comboTipo.getSelectedItem();
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

         Item item = null;
         switch (tipo) {
            case "LIVRO":
               Livro livro = new Livro();
               livro.setIsbn(campoIsbn.getText());
               livro.setEditora(campoEditora.getText());
               item = livro;
               break;

            case "PERIODICO":
               Periodico periodico = new Periodico();
               periodico.setIssn(campoIssn.getText());
               periodico.setEditora(campoEditora.getText());
               item = periodico;
               break;

            case "MIDIA":
               Midia midia = new Midia();
               midia.setDoi(campoDoi.getText());
               midia.setUrl(campoUrl.getText());
               midia.setEditora(campoEditora.getText());
               item = midia;
               break;

            case "MONOGRAFIA":
               Monografia monografia = new Monografia();
               monografia.setIdentificador(campoIdentificador.getText());
               String[] autores = campoAutores.getText().split(",");
               for (String autor : autores) {
                  monografia.adicionarAutor(autor.trim());
               }
               item = monografia;
               break;
         }

         item.setTitulo(titulo);
         item.setDataPublicacao(LocalDate.of(anoPublicacao, 1, 1));
         item.setStatus(StatusItem.DISPONIVEL);
         item.setLocalizacao(localizacao);
         item.setTipo(tipo);

         bibliotecaService.cadastrarItem(item);
         JOptionPane.showMessageDialog(this, "Item cadastrado com sucesso!");
         voltar();
      } catch (Exception e) {
         JOptionPane.showMessageDialog(this, "Erro ao cadastrar item: " + e.getMessage());
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