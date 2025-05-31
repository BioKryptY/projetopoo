package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.biblioteca.model.Usuario;
import com.biblioteca.model.UsuarioComum;
import com.biblioteca.model.UsuarioEspecial;

public class UsuarioDAO implements GenericDAO<Usuario> {

   private Connection conexao;

   public UsuarioDAO() throws SQLException {
      this.conexao = ConnectionFactory.getConexao();
   }

   @Override
   public void salvar(Usuario usuario) throws Exception {
      String sql = "INSERT INTO usuario (nome, email, senha, tipo, telefone) VALUES (?, ?, ?, ?, ?)";

      try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
         stmt.setString(1, usuario.getNome());
         stmt.setString(2, usuario.getEmail());
         stmt.setString(3, usuario.getSenha());
         stmt.setString(4, usuario.getTipo());
         stmt.setString(5, usuario.getTelefone());

         stmt.executeUpdate();

         try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
               usuario.setId(rs.getLong(1));

               if (usuario instanceof UsuarioComum) {
                  salvarUsuarioComum((UsuarioComum) usuario);
               } else if (usuario instanceof UsuarioEspecial) {
                  salvarUsuarioEspecial((UsuarioEspecial) usuario);
               }
            }
         }
      }
   }

   private void salvarUsuarioComum(UsuarioComum usuario) throws SQLException {
      String sql = "INSERT INTO usuario_comum (id_usuario, matricula) VALUES (?, ?)";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setLong(1, usuario.getId());
         stmt.setString(2, usuario.getMatricula());
         stmt.executeUpdate();
      }
   }

   private void salvarUsuarioEspecial(UsuarioEspecial usuario) throws SQLException {
      String sql = "INSERT INTO usuario_especial (id_usuario, codigo) VALUES (?, ?)";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setLong(1, usuario.getId());
         stmt.setString(2, usuario.getCodigo());
         stmt.executeUpdate();
      }
   }

   @Override
   public void atualizar(Usuario usuario) throws Exception {
      String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ?, tipo = ?, telefone = ? WHERE id_usuario = ?";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setString(1, usuario.getNome());
         stmt.setString(2, usuario.getEmail());
         stmt.setString(3, usuario.getSenha());
         stmt.setString(4, usuario.getTipo());
         stmt.setString(5, usuario.getTelefone());
         stmt.setLong(6, usuario.getId());

         stmt.executeUpdate();

         if (usuario instanceof UsuarioComum) {
            atualizarUsuarioComum((UsuarioComum) usuario);
         } else if (usuario instanceof UsuarioEspecial) {
            atualizarUsuarioEspecial((UsuarioEspecial) usuario);
         }
      }
   }

   private void atualizarUsuarioComum(UsuarioComum usuario) throws SQLException {
      String sql = "UPDATE usuario_comum SET matricula = ? WHERE id_usuario = ?";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setString(1, usuario.getMatricula());
         stmt.setLong(2, usuario.getId());
         stmt.executeUpdate();
      }
   }

   private void atualizarUsuarioEspecial(UsuarioEspecial usuario) throws SQLException {
      String sql = "UPDATE usuario_especial SET codigo = ? WHERE id_usuario = ?";

      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setString(1, usuario.getCodigo());
         stmt.setLong(2, usuario.getId());
         stmt.executeUpdate();
      }
   }

   @Override
   public void excluir(Long id) throws Exception {
      Usuario usuario = buscarPorId(id);

      if (usuario != null) {
         if (usuario instanceof UsuarioComum) {
            String sqlTipo = "DELETE FROM usuario_comum WHERE id_usuario = ?";
            try (PreparedStatement stmtTipo = conexao.prepareStatement(sqlTipo)) {
               stmtTipo.setLong(1, id);
               stmtTipo.executeUpdate();
            }
         } else if (usuario instanceof UsuarioEspecial) {
            String sqlTipo = "DELETE FROM usuario_especial WHERE id_usuario = ?";
            try (PreparedStatement stmtTipo = conexao.prepareStatement(sqlTipo)) {
               stmtTipo.setLong(1, id);
               stmtTipo.executeUpdate();
            }
         }

         String sql = "DELETE FROM usuario WHERE id_usuario = ?";

         try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
         }
      }
   }

   @Override
   public Usuario buscarPorId(Long id) throws Exception {
      String sql = "SELECT u.*, uc.matricula, ue.codigo " +
            "FROM usuario u " +
            "LEFT JOIN usuario_comum uc ON u.id_usuario = uc.id_usuario " +
            "LEFT JOIN usuario_especial ue ON u.id_usuario = ue.id_usuario " +
            "WHERE u.id_usuario = ?";
      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setLong(1, id);
         ResultSet rs = stmt.executeQuery();
         if (rs.next()) {
            return criarUsuario(rs);
         }
      }
      return null;
   }

   public Usuario buscarPorEmail(String email) throws Exception {
      String sql = "SELECT u.*, uc.matricula, ue.codigo " +
            "FROM usuario u " +
            "LEFT JOIN usuario_comum uc ON u.id_usuario = uc.id_usuario " +
            "LEFT JOIN usuario_especial ue ON u.id_usuario = ue.id_usuario " +
            "WHERE u.email = ?";
      try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
         stmt.setString(1, email);
         ResultSet rs = stmt.executeQuery();
         if (rs.next()) {
            return criarUsuario(rs);
         }
      }
      return null;
   }

   @Override
   public List<Usuario> listarTodos() throws Exception {
      String sql = "SELECT u.*, uc.matricula, ue.codigo " +
            "FROM usuario u " +
            "LEFT JOIN usuario_comum uc ON u.id_usuario = uc.id_usuario " +
            "LEFT JOIN usuario_especial ue ON u.id_usuario = ue.id_usuario";

      List<Usuario> usuarios = new ArrayList<>();

      try (PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

         while (rs.next()) {
            usuarios.add(criarUsuario(rs));
         }
      }

      return usuarios;
   }

   private Usuario criarUsuario(ResultSet rs) throws SQLException {
      String tipo = rs.getString("tipo");
      Usuario usuario;

      if ("COMUM".equals(tipo)) {
         UsuarioComum usuarioComum = new UsuarioComum();
         usuarioComum.setMatricula(rs.getString("matricula"));
         usuario = usuarioComum;
      } else {
         UsuarioEspecial usuarioEspecial = new UsuarioEspecial();
         usuarioEspecial.setCodigo(rs.getString("codigo"));
         usuario = usuarioEspecial;
      }

      usuario.setId(rs.getLong("id_usuario"));
      usuario.setNome(rs.getString("nome"));
      usuario.setEmail(rs.getString("email"));
      usuario.setSenha(rs.getString("senha"));
      usuario.setTipo(tipo);
      usuario.setTelefone(rs.getString("telefone"));
      usuario.setEspecial("ESPECIAL".equals(tipo));

      return usuario;
   }
}