package com.biblioteca.model;

public enum StatusItem {
   DISPONIVEL("Disponível"),
   EMPRESTADO("Emprestado");

   private final String descricao;

   StatusItem(String descricao) {
      this.descricao = descricao;
   }

   public String getDescricao() {
      return descricao;
   }
}