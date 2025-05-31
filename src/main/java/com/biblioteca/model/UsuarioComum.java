package com.biblioteca.model;

public class UsuarioComum extends Usuario {
   private String matricula;

   public UsuarioComum() {
      super();
   }

   public UsuarioComum(String nome, String email, String senha, String telefone, String matricula) {
      super(nome, email, senha, telefone);
      this.matricula = matricula;
   }

   public String getMatricula() {
      return matricula;
   }

   public void setMatricula(String matricula) {
      this.matricula = matricula;
   }
}