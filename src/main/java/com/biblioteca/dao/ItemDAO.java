package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.biblioteca.model.Item;
import com.biblioteca.model.Livro;
import com.biblioteca.model.Localizacao;
import com.biblioteca.model.Midia;
import com.biblioteca.model.Monografia;
import com.biblioteca.model.Periodico;
import com.biblioteca.model.StatusItem;

public class ItemDAO implements GenericDAO<Item> {
   private Connection conexao;

   public ItemDAO() throws SQLException {
      this.conexao = ConnectionFactory.getConexao();
   }

   @Override
   public void salvar(Item item) throws Exception {
      String sql = "INSERT INTO item (titulo, status, data_publicacao, id_localizacao, tipo) VALUES (?, ?, ?, ?, ?)";

      try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
         stmt.setString(1, item.getTitulo());
         stmt.setString(2, item.getStatus().name());
         stmt.setString(3, String.valueOf(item.getDataPublicacao().getYear()));
         stmt.setLong(4, item.getLocalizacao().getId());
         stmt.setString(5, item.getTipo());

         stmt.executeUpdate();

         try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
               item.setId(rs.getLong(1));

               if (item instanceof Livro) {
                  salvarLivro((Livro) item);
               } else if (item instanceof Periodico) {
                  salvarPeriodico((Periodico) item);
               } else if (item instanceof Midia) {
                  salvarMidia((Midia) item);
               } else if (item instanceof Monografia) {
                  salvarMonografia((Monografia) item);
               }
            }
         }
      }
   }

   private void salvarLivro(Livro livro) throws SQLException {
      String sql = "INSERT INTO livro (id_item, isbn, editora) VALUES (?, ?, ?)";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setLong(1, livro.getId());
         stmt.setString(2, livro.getIsbn());
         stmt.setString(3, livro.getEditora());
         stmt.executeUpdate();
      }
   }

   private void salvarPeriodico(Periodico periodico) throws SQLException {
      String sql = "INSERT INTO periodico (id_item, issn, editora) VALUES (?, ?, ?)";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setLong(1, periodico.getId());
         stmt.setString(2, periodico.getIssn());
         stmt.setString(3, periodico.getEditora());
         stmt.executeUpdate();
      }
   }

   private void salvarMidia(Midia midia) throws SQLException {
      String sql = "INSERT INTO midia (id_item, doi, url, editora) VALUES (?, ?, ?, ?)";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setLong(1, midia.getId());
         stmt.setString(2, midia.getDoi());
         stmt.setString(3, midia.getUrl());
         stmt.setString(4, midia.getEditora());
         stmt.executeUpdate();
      }
   }

   private void salvarMonografia(Monografia monografia) throws SQLException {
      String sql = "INSERT INTO monografia (id_item, identificador) VALUES (?, ?)";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setLong(1, monografia.getId());
         stmt.setString(2, monografia.getIdentificador());
         stmt.executeUpdate();

         for (String autor : monografia.getAutores()) {
            salvarAutorMonografia(monografia.getId(), autor);
         }
      }
   }

   private void salvarAutorMonografia(Long idMonografia, String autor) throws SQLException {
      String sql = "INSERT INTO monografia_autores (id_item, autor) VALUES (?, ?)";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setLong(1, idMonografia);
         stmt.setString(2, autor);
         stmt.executeUpdate();
      }
   }

   @Override
   public void atualizar(Item item) throws Exception {
      String sql = "UPDATE item SET titulo = ?, status = ?, id_localizacao = ?, tipo = ?, data_publicacao = ? WHERE id_item = ?";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setString(1, item.getTitulo());
         stmt.setString(2, item.getStatus().name());
         stmt.setLong(3, item.getLocalizacao().getId());
         stmt.setString(4, item.getTipo());
         if (item.getDataPublicacao() != null) {
            stmt.setString(5, String.valueOf(item.getDataPublicacao().getYear()));
         } else {
            stmt.setNull(5, java.sql.Types.VARCHAR);
         }
         stmt.setLong(6, item.getId());

         stmt.executeUpdate();

         if (item instanceof Livro) {
            atualizarLivro((Livro) item);
         } else if (item instanceof Periodico) {
            atualizarPeriodico((Periodico) item);
         } else if (item instanceof Midia) {
            atualizarMidia((Midia) item);
         } else if (item instanceof Monografia) {
            atualizarMonografia((Monografia) item);
         }
      }
   }

   private void atualizarLivro(Livro livro) throws SQLException {
      String sql = "UPDATE livro SET isbn = ?, editora = ? WHERE id_item = ?";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setString(1, livro.getIsbn());
         stmt.setString(2, livro.getEditora());
         stmt.setLong(3, livro.getId());
         stmt.executeUpdate();
      }
   }

   private void atualizarPeriodico(Periodico periodico) throws SQLException {
      String sql = "UPDATE periodico SET issn = ?, editora = ? WHERE id_item = ?";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setString(1, periodico.getIssn());
         stmt.setString(2, periodico.getEditora());
         stmt.setLong(3, periodico.getId());
         stmt.executeUpdate();
      }
   }

   private void atualizarMidia(Midia midia) throws SQLException {
      String sql = "UPDATE midia SET doi = ?, url = ?, editora = ? WHERE id_item = ?";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setString(1, midia.getDoi());
         stmt.setString(2, midia.getUrl());
         stmt.setString(3, midia.getEditora());
         stmt.setLong(4, midia.getId());
         stmt.executeUpdate();
      }
   }

   private void atualizarMonografia(Monografia monografia) throws SQLException {
      String sql = "UPDATE monografia SET identificador = ? WHERE id_item = ?";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setString(1, monografia.getIdentificador());
         stmt.setLong(2, monografia.getId());
         stmt.executeUpdate();

         excluirAutoresMonografia(monografia.getId());
         for (String autor : monografia.getAutores()) {
            salvarAutorMonografia(monografia.getId(), autor);
         }
      }
   }

   private void excluirAutoresMonografia(Long idMonografia) throws SQLException {
      String sql = "DELETE FROM monografia_autores WHERE id_item = ?";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setLong(1, idMonografia);
         stmt.executeUpdate();
      }
   }

   @Override
   public void excluir(Long id) throws Exception {

      Item itemParaExcluir = buscarPorId(id);
      if (itemParaExcluir == null) {
         return;
      }

      String tipo = itemParaExcluir.getTipo();
      String sqlEspecializacao = null;

      switch (tipo) {
         case "LIVRO":
            sqlEspecializacao = "DELETE FROM livro WHERE id_item = ?";
            break;
         case "PERIODICO":
            sqlEspecializacao = "DELETE FROM periodico WHERE id_item = ?";
            break;
         case "MIDIA":
            sqlEspecializacao = "DELETE FROM midia WHERE id_item = ?";
            break;
         case "MONOGRAFIA":
            sqlEspecializacao = "DELETE FROM monografia WHERE id_item = ?";

            excluirAutoresMonografia(id);
            break;
      }

      if (sqlEspecializacao != null) {
         try (PreparedStatement stmtEspecializacao = conexao.prepareStatement(sqlEspecializacao)) {
            stmtEspecializacao.setLong(1, id);
            stmtEspecializacao.executeUpdate();
         }
      }

      String sqlItem = "DELETE FROM item WHERE id_item = ?";
      try (PreparedStatement stmtItem = conexao.prepareStatement(sqlItem)) {
         stmtItem.setLong(1, id);
         stmtItem.executeUpdate();
      }
   }

   @Override
   public Item buscarPorId(Long id) throws Exception {
      String sql = "SELECT i.*, l.*, p.*, m.*, mo.*, loc.* " +
            "FROM item i " +
            "LEFT JOIN livro l ON i.id_item = l.id_item " +
            "LEFT JOIN periodico p ON i.id_item = p.id_item " +
            "LEFT JOIN midia m ON i.id_item = m.id_item " +
            "LEFT JOIN monografia mo ON i.id_item = mo.id_item " +
            "LEFT JOIN localizacao loc ON i.id_localizacao = loc.id_localizacao " +
            "WHERE i.id_item = ?";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setLong(1, id);

         try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
               return criarItem(rs);
            }
         }
      }

      return null;
   }

   @Override
   public List<Item> listarTodos() throws Exception {
      String sql = "SELECT i.*, l.*, p.*, m.*, mo.*, loc.* " +
            "FROM item i " +
            "LEFT JOIN livro l ON i.id_item = l.id_item " +
            "LEFT JOIN periodico p ON i.id_item = p.id_item " +
            "LEFT JOIN midia m ON i.id_item = m.id_item " +
            "LEFT JOIN monografia mo ON i.id_item = mo.id_item " +
            "LEFT JOIN localizacao loc ON i.id_localizacao = loc.id_localizacao";

      List<Item> itens = new ArrayList<>();

      try (PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

         while (rs.next()) {
            itens.add(criarItem(rs));
         }
      }

      return itens;
   }

   private Item criarItem(ResultSet rs) throws SQLException {
      String tipo = rs.getString("tipo");
      Item item;

      if ("LIVRO".equals(tipo)) {
         Livro livro = new Livro();
         livro.setIsbn(rs.getString("isbn"));
         livro.setEditora(rs.getString("editora"));
         item = livro;
      } else if ("PERIODICO".equals(tipo)) {
         Periodico periodico = new Periodico();
         periodico.setIssn(rs.getString("issn"));
         periodico.setEditora(rs.getString("editora"));
         item = periodico;
      } else if ("MIDIA".equals(tipo)) {
         Midia midia = new Midia();
         midia.setDoi(rs.getString("doi"));
         midia.setUrl(rs.getString("url"));
         midia.setEditora(rs.getString("editora"));
         item = midia;
      } else {
         Monografia monografia = new Monografia();
         monografia.setIdentificador(rs.getString("identificador"));
         carregarAutoresMonografia(monografia);
         item = monografia;
      }

      item.setId(rs.getLong("id_item"));
      item.setTitulo(rs.getString("titulo"));
      item.setStatus(StatusItem.valueOf(rs.getString("status")));
      item.setTipo(tipo);
      item.setDataPublicacao(LocalDate.of(Integer.valueOf(rs.getString("data_publicacao")), 1, 1));

      Localizacao localizacao = new Localizacao();
      localizacao.setId(rs.getLong("id_localizacao"));
      localizacao.setEstante(rs.getString("estante"));
      localizacao.setPratileira(rs.getString("pratileira"));
      localizacao.setSecao(rs.getString("secao"));
      item.setLocalizacao(localizacao);

      return item;
   }

   private void carregarAutoresMonografia(Monografia monografia) throws SQLException {
      String sql = "SELECT autor FROM monografia_autores WHERE id_item = ?";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setLong(1, monografia.getId());

         try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
               monografia.adicionarAutor(rs.getString("autor"));
            }
         }
      }
   }

   public List<Item> buscarPorTitulo(String titulo) throws Exception {
      String sql = "SELECT i.*, l.*, p.*, m.*, mo.*, loc.* " +
            "FROM item i " +
            "LEFT JOIN livro l ON i.id_item = l.id_item " +
            "LEFT JOIN periodico p ON i.id_item = p.id_item " +
            "LEFT JOIN midia m ON i.id_item = m.id_item " +
            "LEFT JOIN monografia mo ON i.id_item = mo.id_item " +
            "LEFT JOIN localizacao loc ON i.id_localizacao = loc.id_localizacao " +
            "WHERE UPPER(i.titulo) LIKE UPPER(?)";

      List<Item> itens = new ArrayList<>();

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setString(1, "%" + titulo + "%");

         try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
               itens.add(criarItem(rs));
            }
         }
      }

      return itens;
   }

   public Localizacao salvarLocalizacao(Localizacao localizacao) throws SQLException {
      String sql = "INSERT INTO localizacao (estante, pratileira, secao) VALUES (?, ?, ?)";

      try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
         stmt.setString(1, localizacao.getEstante());
         stmt.setString(2, localizacao.getPratileira());
         stmt.setString(3, localizacao.getSecao());

         stmt.executeUpdate();

         try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
               localizacao.setId(rs.getLong(1));
            }
         }
      }
      return localizacao;
   }
}