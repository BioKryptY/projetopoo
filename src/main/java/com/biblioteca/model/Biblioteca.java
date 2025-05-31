package com.biblioteca.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Biblioteca {
   private List<Item> itens;
   private List<Usuario> usuarios;
   private List<Emprestimo> emprestimos;

   public Biblioteca() {
      this.itens = new ArrayList<>();
      this.usuarios = new ArrayList<>();
      this.emprestimos = new ArrayList<>();
   }

   public void adicionarItem(Item item) {
      itens.add(item);
   }

   public void removerItem(Item item) {
      itens.remove(item);
   }

   public List<Item> buscarItensPorTitulo(String titulo) {
      return itens.stream()
            .filter(item -> item.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
            .toList();
   }

   public void adicionarUsuario(Usuario usuario) {
      usuarios.add(usuario);
   }

   public void removerUsuario(Usuario usuario) {
      usuarios.remove(usuario);
   }

   public List<Usuario> buscarUsuariosPorNome(String nome) {
      return usuarios.stream()
            .filter(usuario -> usuario.getNome().toLowerCase().contains(nome.toLowerCase()))
            .toList();
   }

   public Emprestimo realizarEmprestimo(Usuario usuario, List<Item> itens) {
      if (itens == null || itens.isEmpty()) {
         return null;
      }

      for (Item item : itens) {
         if (item.getStatus() != StatusItem.DISPONIVEL) {
            return null;
         }
      }

      Emprestimo emprestimo = new Emprestimo(usuario, itens, LocalDateTime.now());

      for (Item item : itens) {
         item.setStatus(StatusItem.EMPRESTADO);
      }

      emprestimos.add(emprestimo);
      return emprestimo;
   }

   public void realizarDevolucao(Item item) {
      if (item.getStatus() == StatusItem.EMPRESTADO) {
         item.setStatus(StatusItem.DISPONIVEL);
      }
   }

   public List<Item> getItens() {
      return itens;
   }

   public List<Usuario> getUsuarios() {
      return usuarios;
   }

   public List<Emprestimo> getEmprestimos() {
      return emprestimos;
   }
}