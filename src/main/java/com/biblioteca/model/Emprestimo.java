package com.biblioteca.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Emprestimo {
   private Long id;
   private Usuario usuario;
   private List<Item> itens;
   private LocalDateTime dataEmprestimo;
   private LocalDateTime dataDevolucao;

   public Emprestimo() {
      this.itens = new ArrayList<>();
   }

   public Emprestimo(Usuario usuario, List<Item> itens, LocalDateTime dataEmprestimo) {
      this.usuario = usuario;
      this.itens = itens;
      this.dataEmprestimo = dataEmprestimo;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Usuario getUsuario() {
      return usuario;
   }

   public void setUsuario(Usuario usuario) {
      this.usuario = usuario;
   }

   public List<Item> getItens() {
      return itens;
   }

   public void setItens(List<Item> itens) {
      this.itens = itens;
   }

   public void adicionarItem(Item item) {
      if (this.itens == null) {
         this.itens = new ArrayList<>();
      }
      this.itens.add(item);
   }

   public LocalDateTime getDataEmprestimo() {
      return dataEmprestimo;
   }

   public void setDataEmprestimo(LocalDateTime dataEmprestimo) {
      this.dataEmprestimo = dataEmprestimo;
   }

   public LocalDateTime getDataDevolucao() {
      return dataDevolucao;
   }

   public void setDataDevolucao(LocalDateTime dataDevolucao) {
      this.dataDevolucao = dataDevolucao;
   }
}