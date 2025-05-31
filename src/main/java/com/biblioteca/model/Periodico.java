package com.biblioteca.model;

import java.time.LocalDate;

public class Periodico extends Item {
   private String issn;
   private String editora;

   public Periodico() {
      super();
   }

   public Periodico(String titulo, LocalDate dataPublicacao, StatusItem status, Localizacao localizacao,
         String issn, String editora) {
      super(titulo, dataPublicacao, status, localizacao);
      this.issn = issn;
      this.editora = editora;
   }

   public String getIssn() {
      return issn;
   }

   public void setIssn(String issn) {
      this.issn = issn;
   }

   public String getEditora() {
      return editora;
   }

   public void setEditora(String editora) {
      this.editora = editora;
   }
}