package com.biblioteca.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Monografia extends Item {
   private String identificador;
   private List<String> autores;

   public Monografia() {
      super();
      this.autores = new ArrayList<>();
   }

   public Monografia(String titulo, LocalDate dataPublicacao, StatusItem status, Localizacao localizacao,
         String identificador) {
      super(titulo, dataPublicacao, status, localizacao);
      this.identificador = identificador;
      this.autores = new ArrayList<>();
   }

   public String getIdentificador() {
      return identificador;
   }

   public void setIdentificador(String identificador) {
      this.identificador = identificador;
   }

   public List<String> getAutores() {
      return autores;
   }

   public void setAutores(List<String> autores) {
      this.autores = autores;
   }

   public void adicionarAutor(String autor) {
      this.autores.add(autor);
   }

   public void removerAutor(String autor) {
      this.autores.remove(autor);
   }
}