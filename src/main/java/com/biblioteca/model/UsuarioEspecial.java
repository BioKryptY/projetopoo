package com.biblioteca.model;

public class UsuarioEspecial extends Usuario {
   private String codigo;

   public UsuarioEspecial() {
      super();
   }

   public UsuarioEspecial(String nome, String email, String senha, String telefone, String codigo) {
      super(nome, email, senha, telefone);
      this.codigo = codigo;
   }

   public String getCodigo() {
      return codigo;
   }

   public void setCodigo(String codigo) {
      this.codigo = codigo;
   }
}