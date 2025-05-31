package com.biblioteca.dao;

import java.util.List;

public interface GenericDAO<T> {
   void salvar(T objeto) throws Exception;

   void atualizar(T objeto) throws Exception;

   void excluir(Long id) throws Exception;

   T buscarPorId(Long id) throws Exception;

   List<T> listarTodos() throws Exception;
}