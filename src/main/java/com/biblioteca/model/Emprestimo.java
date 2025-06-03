package com.biblioteca.model;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;

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

   public String calcularMulta() {
      DecimalFormat df = new DecimalFormat("#.00");

      if (this.getDataDevolucao() == null) {
         return "0,00";
      }

      LocalDateTime dataLimite = this.getDataEmprestimo().plusDays(15);
      if (this.getDataDevolucao().isAfter(dataLimite)) {
         long diasAtraso = java.time.Duration.between(dataLimite, this.getDataDevolucao()).toDays();
         float total = diasAtraso * 1.50f; // Exemplo: R$1,50 por dia de atraso
         return df.format(total);
      }
      return "0,00";
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