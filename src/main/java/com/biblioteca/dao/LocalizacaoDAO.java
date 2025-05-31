package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.biblioteca.model.Localizacao;

public class LocalizacaoDAO implements GenericDAO<Localizacao> {

   private Connection conexao;

   public LocalizacaoDAO() throws SQLException {
      this.conexao = ConnectionFactory.getConexao();
   }

   @Override
   public void salvar(Localizacao localizacao) throws Exception {

      Localizacao localizacaoExistente = buscarPorDetalhes(localizacao.getEstante(), localizacao.getPratileira(),
            localizacao.getSecao());

      if (localizacaoExistente != null) {

         localizacao.setId(localizacaoExistente.getId());
      } else {

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
      }
   }

   @Override
   public void atualizar(Localizacao localizacao) throws Exception {
      String sql = "UPDATE localizacao SET estante = ?, pratileira = ?, secao = ? WHERE id_localizacao = ?";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setString(1, localizacao.getEstante());
         stmt.setString(2, localizacao.getPratileira());
         stmt.setString(3, localizacao.getSecao());
         stmt.setLong(4, localizacao.getId());

         stmt.executeUpdate();
      }
   }

   @Override
   public void excluir(Long id) throws Exception {
      String sql = "DELETE FROM localizacao WHERE id_localizacao = ?";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setLong(1, id);
         stmt.executeUpdate();
      }
   }

   @Override
   public Localizacao buscarPorId(Long id) throws Exception {
      String sql = "SELECT * FROM localizacao WHERE id_localizacao = ?";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setLong(1, id);

         try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
               return criarLocalizacao(rs);
            }
         }
      } catch (SQLException e) {
         throw new RuntimeException("Erro ao buscar localização por ID", e);
      }
      return null;
   }

   @Override
   public List<Localizacao> listarTodos() {
      String sql = "SELECT * FROM localizacao";
      List<Localizacao> localizacoes = new ArrayList<>();

      try (Connection conn = ConnectionFactory.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

         while (rs.next()) {
            localizacoes.add(criarLocalizacao(rs));
         }
      } catch (SQLException e) {
         throw new RuntimeException("Erro ao listar localizações", e);
      }
      return localizacoes;
   }

   private Localizacao criarLocalizacao(ResultSet rs) throws SQLException {
      Localizacao localizacao = new Localizacao();
      localizacao.setId(rs.getLong("id_localizacao"));
      localizacao.setEstante(rs.getString("estante"));
      localizacao.setPratileira(rs.getString("pratileira"));
      localizacao.setSecao(rs.getString("secao"));
      return localizacao;
   }

   public Localizacao buscarPorDetalhes(String estante, String pratileira, String secao) throws SQLException {
      String sql = "SELECT * FROM localizacao WHERE estante = ? AND pratileira = ? AND secao = ?";
      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setString(1, estante);
         stmt.setString(2, pratileira);
         stmt.setString(3, secao);
         try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
               return criarLocalizacao(rs);
            }
         }
      }
      return null;
   }
}