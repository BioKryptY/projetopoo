package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Item;
import com.biblioteca.model.Usuario;

public class EmprestimoDAO implements GenericDAO<Emprestimo> {
   private Connection conexao;
   private UsuarioDAO usuarioDAO;
   private ItemDAO itemDAO;

   public EmprestimoDAO() throws SQLException {
      this.conexao = ConexaoBD.getConexao();
      this.usuarioDAO = new UsuarioDAO();
      this.itemDAO = new ItemDAO();
   }

   @Override
   public void salvar(Emprestimo emprestimo) throws Exception {
      String sqlEmprestimo = "INSERT INTO emprestimo (id_usuario, data_emprestimo, data_devolucao) VALUES (?, ?, ?)";

      try (PreparedStatement stmtEmprestimo = conexao.prepareStatement(sqlEmprestimo,
            Statement.RETURN_GENERATED_KEYS)) {
         stmtEmprestimo.setLong(1, emprestimo.getUsuario().getId());
         stmtEmprestimo.setTimestamp(2, Timestamp.valueOf(emprestimo.getDataEmprestimo()));
         stmtEmprestimo.setTimestamp(3,
               emprestimo.getDataDevolucao() != null ? Timestamp.valueOf(emprestimo.getDataDevolucao()) : null);

         stmtEmprestimo.executeUpdate();

         try (ResultSet rs = stmtEmprestimo.getGeneratedKeys()) {
            if (rs.next()) {
               emprestimo.setId(rs.getLong(1));
            }
         }
      }

      String sqlEmprestimoItem = "INSERT INTO emprestimo_item (id_emprestimo, id_item) VALUES (?, ?)";
      try (PreparedStatement stmtEmprestimoItem = conexao.prepareStatement(sqlEmprestimoItem)) {
         for (Item item : emprestimo.getItens()) {
            stmtEmprestimoItem.setLong(1, emprestimo.getId());
            stmtEmprestimoItem.setLong(2, item.getId());
            stmtEmprestimoItem.addBatch();
         }
         stmtEmprestimoItem.executeBatch();
      }
   }

   @Override
   public void atualizar(Emprestimo emprestimo) throws Exception {

      String sqlEmprestimo = "UPDATE emprestimo SET id_usuario = ?, data_emprestimo = ?, data_devolucao = ? WHERE id_emprestimo = ?";

      try (PreparedStatement stmtEmprestimo = conexao.prepareStatement(sqlEmprestimo)) {
         stmtEmprestimo.setLong(1, emprestimo.getUsuario().getId());
         stmtEmprestimo.setTimestamp(2, Timestamp.valueOf(emprestimo.getDataEmprestimo()));
         stmtEmprestimo.setTimestamp(3,
               emprestimo.getDataDevolucao() != null ? Timestamp.valueOf(emprestimo.getDataDevolucao()) : null);
         stmtEmprestimo.setLong(4, emprestimo.getId());

         stmtEmprestimo.executeUpdate();
      }

      String sqlDeleteItens = "DELETE FROM emprestimo_item WHERE id_emprestimo = ?";
      try (PreparedStatement stmtDeleteItens = conexao.prepareStatement(sqlDeleteItens)) {
         stmtDeleteItens.setLong(1, emprestimo.getId());
         stmtDeleteItens.executeUpdate();
      }

      String sqlInsertItens = "INSERT INTO emprestimo_item (id_emprestimo, id_item) VALUES (?, ?)";
      try (PreparedStatement stmtInsertItens = conexao.prepareStatement(sqlInsertItens)) {
         for (Item item : emprestimo.getItens()) {
            stmtInsertItens.setLong(1, emprestimo.getId());
            stmtInsertItens.setLong(2, item.getId());
            stmtInsertItens.addBatch();
         }
         stmtInsertItens.executeBatch();
      }
   }

   @Override
   public void excluir(Long id) throws Exception {
      String sql = "DELETE FROM emprestimo WHERE id = ?";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setLong(1, id);
         stmt.executeUpdate();
      }
   }

   @Override
   public Emprestimo buscarPorId(Long id) throws Exception {
      String sql = "SELECT * FROM emprestimo WHERE id_emprestimo = ?";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setLong(1, id);

         try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
               return criarEmprestimo(rs);
            }
         }
      }
      return null;
   }

   @Override
   public List<Emprestimo> listarTodos() throws Exception {
      String sql = "SELECT * FROM emprestimo";
      List<Emprestimo> emprestimos = new ArrayList<>();

      try (PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

         while (rs.next()) {
            emprestimos.add(criarEmprestimo(rs));
         }
      }
      return emprestimos;
   }

   private Emprestimo criarEmprestimo(ResultSet rs) throws SQLException, Exception {
      Emprestimo emprestimo = new Emprestimo();
      emprestimo.setId(rs.getLong("id_emprestimo"));

      Usuario usuario = usuarioDAO.buscarPorId(rs.getLong("id_usuario"));
      emprestimo.setUsuario(usuario);

      String sqlItensEmprestimo = "SELECT id_item FROM emprestimo_item WHERE id_emprestimo = ?";
      try (PreparedStatement stmtItens = conexao.prepareStatement(sqlItensEmprestimo)) {
         stmtItens.setLong(1, emprestimo.getId());
         try (ResultSet rsItens = stmtItens.executeQuery()) {
            List<Item> itensDoEmprestimo = new ArrayList<>();
            while (rsItens.next()) {
               Long idItem = rsItens.getLong("id_item");
               Item item = itemDAO.buscarPorId(idItem);
               if (item != null) {
                  itensDoEmprestimo.add(item);
               }
            }
            emprestimo.setItens(itensDoEmprestimo);
         }
      }

      emprestimo.setDataEmprestimo(rs.getTimestamp("data_emprestimo").toLocalDateTime());

      Timestamp dataDevolucao = rs.getTimestamp("data_devolucao");
      if (dataDevolucao != null) {
         emprestimo.setDataDevolucao(dataDevolucao.toLocalDateTime());
      }

      return emprestimo;
   }

   public List<Emprestimo> buscarPorUsuario(Long idUsuario) throws Exception {
      String sql = "SELECT * FROM emprestimo WHERE id_usuario = ?";

      List<Emprestimo> emprestimos = new ArrayList<>();

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setLong(1, idUsuario);

         try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
               emprestimos.add(criarEmprestimo(rs));
            }
         }
      }
      return emprestimos;
   }

   public List<Emprestimo> buscarEmprestimosAtivosPorUsuario(Long idUsuario) throws Exception {
      String sql = "SELECT * FROM emprestimo WHERE id_usuario = ? AND data_devolucao IS NULL";

      List<Emprestimo> emprestimos = new ArrayList<>();

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setLong(1, idUsuario);

         try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
               emprestimos.add(criarEmprestimo(rs));
            }
         }
      }
      return emprestimos;
   }

   public List<Emprestimo> buscarAtrasados() throws Exception {
      String sql = "SELECT * FROM emprestimo WHERE data_devolucao IS NULL AND data_emprestimo < CURRENT_TIMESTAMP - INTERVAL '7 days'";

      List<Emprestimo> emprestimos = new ArrayList<>();

      try (PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

         while (rs.next()) {
            emprestimos.add(criarEmprestimo(rs));
         }
      }
      return emprestimos;
   }

   public Emprestimo buscarEmprestimoAtivoPorItem(Long idItem) throws Exception {
      String sql = "SELECT e.* FROM emprestimo e JOIN emprestimo_item ei ON e.id_emprestimo = ei.id_emprestimo WHERE ei.id_item = ? AND e.data_devolucao IS NULL";
      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setLong(1, idItem);
         try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
               return criarEmprestimo(rs);
            }
         }
      }
      return null;
   }

   public List<Emprestimo> buscarEmprestimosAtrasados() throws Exception {
      String sql = "SELECT * FROM emprestimo WHERE data_devolucao IS NULL AND data_emprestimo < CURRENT_TIMESTAMP - INTERVAL '7 days'";

      List<Emprestimo> emprestimos = new ArrayList<>();

      try (PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

         while (rs.next()) {
            emprestimos.add(criarEmprestimo(rs));
         }
      }
      return emprestimos;
   }

   public List<Emprestimo> listarEmprestimosAtivos() throws Exception {
      String sql = "SELECT * FROM emprestimo WHERE data_devolucao IS NULL";
      List<Emprestimo> emprestimos = new ArrayList<>();

      try (PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

         while (rs.next()) {
            emprestimos.add(criarEmprestimo(rs));
         }
      }
      return emprestimos;
   }
}