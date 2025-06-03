package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
   private static final String URL = "jdbc:postgresql://localhost:5432/projetopoo";
   private static final String USUARIO = "postgres";
   private static final String SENHA = "123456";

   private static Connection conexao = null;

   public static Connection getConexao() {
      if (conexao == null) {
         try {
            Class.forName("org.postgresql.Driver");
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
         } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
         }
      }
      return conexao;
   }

   public static void fecharConexao() {
      if (conexao != null) {
         try {
            conexao.close();
            conexao = null;
         } catch (SQLException e) {
            throw new RuntimeException("Erro ao fechar conexão com o banco de dados", e);
         }
      }
   }
}