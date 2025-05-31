package com.biblioteca.model;

import java.time.LocalDate;

public abstract class Item {
   private Long id;
   private String titulo;
   private LocalDate dataPublicacao;
   private StatusItem status;
   private Localizacao localizacao;
   private Emprestimo emprestimo;
   private String tipo;

   public Item() {
      this.status = StatusItem.DISPONIVEL;
   }

   public Item(String titulo, LocalDate dataPublicacao, StatusItem status, Localizacao localizacao) {
      this.titulo = titulo;
      this.dataPublicacao = dataPublicacao;
      this.status = status;
      this.localizacao = localizacao;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getTitulo() {
      return titulo;
   }

   public void setTitulo(String titulo) {
      this.titulo = titulo;
   }

   public LocalDate getDataPublicacao() {
      return dataPublicacao;
   }

   public void setDataPublicacao(LocalDate dataPublicacao) {
      this.dataPublicacao = dataPublicacao;
   }

   public StatusItem getStatus() {
      return status;
   }

   public void setStatus(StatusItem status) {
      this.status = status;
   }

   public Localizacao getLocalizacao() {
      return localizacao;
   }

   public void setLocalizacao(Localizacao localizacao) {
      this.localizacao = localizacao;
   }

   public Emprestimo getEmprestimo() {
      return emprestimo;
   }

   public void setEmprestimo(Emprestimo emprestimo) {
      this.emprestimo = emprestimo;
   }

   public boolean isDisponivel() {
      return this.status == StatusItem.DISPONIVEL;
   }

   public String getTipo() {
      return tipo;
   }

   public void setTipo(String tipo) {
      this.tipo = tipo;
   }
}