package com.biblioteca.model;

public class Localizacao {
   private Long id;
   private String estante;
   private String pratileira;
   private String secao;

   public Localizacao() {
   }

   public Localizacao(String estante, String pratileira, String secao) {
      this.estante = estante;
      this.pratileira = pratileira;
      this.secao = secao;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getEstante() {
      return estante;
   }

   public void setEstante(String estante) {
      this.estante = estante;
   }

   public String getPratileira() {
      return pratileira;
   }

   public void setPratileira(String pratileira) {
      this.pratileira = pratileira;
   }

   public String getSecao() {
      return secao;
   }

   public void setSecao(String secao) {
      this.secao = secao;
   }
}