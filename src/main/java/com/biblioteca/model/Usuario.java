package com.biblioteca.model;

public abstract class Usuario {
   private Long id;
   private String nome;
   private String email;
   private String senha;
   private String tipo;
   private String telefone;
   private boolean especial;

   public Usuario() {
   }

   public Usuario(String nome, String email, String senha, String telefone) {
      this.nome = nome;
      this.email = email;
      this.senha = senha;
      this.telefone = telefone;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getNome() {
      return nome;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getSenha() {
      return senha;
   }

   public void setSenha(String senha) {
      this.senha = senha;
   }

   public String getTipo() {
      return tipo;
   }

   public void setTipo(String tipo) {
      this.tipo = tipo;
   }

   public String getTelefone() {
      return telefone;
   }

   public void setTelefone(String telefone) {
      this.telefone = telefone;
   }

   public boolean isEspecial() {
      return especial;
   }

   public void setEspecial(boolean especial) {
      this.especial = especial;
      this.tipo = especial ? "ESPECIAL" : "COMUM";
   }
}