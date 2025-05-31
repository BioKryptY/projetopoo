package com.biblioteca.model;

import java.time.LocalDate;

public class Midia extends Item {
   private String doi;
   private String url;
   private String editora;

   public Midia() {
      super();
   }

   public Midia(String titulo, LocalDate dataPublicacao, StatusItem status, Localizacao localizacao,
         String doi, String url, String editora) {
      super(titulo, dataPublicacao, status, localizacao);
      this.doi = doi;
      this.url = url;
      this.editora = editora;
   }

   public String getDoi() {
      return doi;
   }

   public void setDoi(String doi) {
      this.doi = doi;
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public String getEditora() {
      return editora;
   }

   public void setEditora(String editora) {
      this.editora = editora;
   }
}