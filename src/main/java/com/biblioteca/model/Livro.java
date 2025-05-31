package com.biblioteca.model;

import java.time.LocalDate;

public class Livro extends Item {
   private String isbn;
   private String editora;

   public Livro() {
      super();
   }

   public Livro(String titulo, LocalDate dataPublicacao, StatusItem status, Localizacao localizacao,
         String isbn, String editora) {
      super(titulo, dataPublicacao, status, localizacao);
      this.isbn = isbn;
      this.editora = editora;
   }

   public String getIsbn() {
      return isbn;
   }

   public void setIsbn(String isbn) {
      this.isbn = isbn;
   }

   public String getEditora() {
      return editora;
   }

   public void setEditora(String editora) {
      this.editora = editora;
   }
}