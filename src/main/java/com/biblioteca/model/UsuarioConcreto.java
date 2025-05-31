package com.biblioteca.model;

public class UsuarioConcreto extends Usuario {
   public UsuarioConcreto() {
      super();
   }

   public UsuarioConcreto(String nome, String email, String senha, String telefone) {
      super(nome, email, senha, telefone);
   }
}