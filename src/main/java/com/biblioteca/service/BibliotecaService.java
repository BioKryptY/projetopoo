package com.biblioteca.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import com.biblioteca.dao.EmprestimoDAO;
import com.biblioteca.dao.ItemDAO;
import com.biblioteca.dao.LocalizacaoDAO;
import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Item;
import com.biblioteca.model.StatusItem;
import com.biblioteca.model.Usuario;

public class BibliotecaService {
   private UsuarioDAO usuarioDAO;
   private ItemDAO itemDAO;
   private LocalizacaoDAO localizacaoDAO;
   private EmprestimoDAO emprestimoDAO;

   public BibliotecaService() throws Exception {
      this.usuarioDAO = new UsuarioDAO();
      this.itemDAO = new ItemDAO();
      this.localizacaoDAO = new LocalizacaoDAO();
      this.emprestimoDAO = new EmprestimoDAO();
   }

   private String gerarHashSenha(String senha) throws Exception {
      String salt = "biblioteca_puc_salt";
      String senhaComSalt = senha + salt;

      try {
         MessageDigest digest = MessageDigest.getInstance("SHA-256");
         byte[] hash = digest.digest(senhaComSalt.getBytes(StandardCharsets.UTF_8));
         return Base64.getEncoder().encodeToString(hash);
      } catch (NoSuchAlgorithmException e) {
         throw new Exception("Erro ao gerar hash da senha", e);
      }
   }

   public boolean verificarSenha(String senhaFornecida, String hashArmazenado) throws Exception {
      String salt = "biblioteca_puc_salt";
      String senhaFornecidaComSalt = senhaFornecida + salt;

      try {
         MessageDigest digest = MessageDigest.getInstance("SHA-256");
         byte[] hashFornecido = digest.digest(senhaFornecidaComSalt.getBytes(StandardCharsets.UTF_8));
         String hashFornecidoBase64 = Base64.getEncoder().encodeToString(hashFornecido);
         return hashFornecidoBase64.equals(hashArmazenado);
      } catch (NoSuchAlgorithmException e) {
         throw new Exception("Erro ao verificar hash da senha", e);
      }
   }

   public void cadastrarUsuario(Usuario usuario) throws Exception {
      Usuario usuarioExistente = usuarioDAO.buscarPorEmail(usuario.getEmail());
      if (usuarioExistente != null) {
         throw new Exception("Já existe um usuário cadastrado com este email.");
      }

      usuario.setSenha(gerarHashSenha(usuario.getSenha()));
      usuarioDAO.salvar(usuario);
   }

   public void atualizarUsuario(Usuario usuario) throws Exception {
      if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
         usuario.setSenha(gerarHashSenha(usuario.getSenha()));
      }
      usuarioDAO.atualizar(usuario);
   }

   public void excluirUsuario(Long id) throws Exception {
      usuarioDAO.excluir(id);
   }

   public Usuario buscarUsuarioPorId(Long id) throws Exception {
      return usuarioDAO.buscarPorId(id);
   }

   public Usuario buscarUsuarioPorEmail(String email) throws Exception {
      return usuarioDAO.buscarPorEmail(email);
   }

   public List<Usuario> listarUsuarios() throws Exception {
      return usuarioDAO.listarTodos();
   }

   public void cadastrarItem(Item item) throws Exception {
      if (item.getLocalizacao() != null) {
         localizacaoDAO.salvar(item.getLocalizacao());
      }
      itemDAO.salvar(item);
   }

   public void atualizarItem(Item item) throws Exception {
      if (item.getLocalizacao() != null) {
         localizacaoDAO.salvar(item.getLocalizacao());
      }
      itemDAO.atualizar(item);
   }

   public void excluirItem(Long id) throws Exception {
      itemDAO.excluir(id);
   }

   public Item buscarItemPorId(Long id) throws Exception {
      return itemDAO.buscarPorId(id);
   }

   public List<Item> listarItens() throws Exception {
      return itemDAO.listarTodos();
   }

   public List<Item> buscarItensPorTitulo(String titulo) throws Exception {
      return itemDAO.buscarPorTitulo(titulo);
   }

   public Emprestimo buscarEmprestimoPorId(Long idEmprestimo) throws Exception {
      return emprestimoDAO.buscarPorId(idEmprestimo);
   }

   public void realizarEmprestimo(Usuario usuario, List<Item> itens) throws Exception {
      if (itens == null || itens.isEmpty()) {
         throw new Exception("Nenhum item selecionado para empréstimo.");
      }

      for (Item item : itens) {
         if (!item.isDisponivel()) {
            throw new Exception("O item \"" + item.getTitulo() + "\" não está disponível para empréstimo.");
         }
      }

      Emprestimo emprestimo = new Emprestimo();
      emprestimo.setUsuario(usuario);
      emprestimo.setItens(itens);
      emprestimo.setDataEmprestimo(LocalDateTime.now());

      emprestimoDAO.salvar(emprestimo);

      for (Item item : itens) {
         item.setStatus(StatusItem.EMPRESTADO);
         itemDAO.atualizar(item);
      }
   }

   public void realizarDevolucao(List<Item> itens) throws Exception {

      Emprestimo emprestimoAtivo = emprestimoDAO.buscarEmprestimoAtivoPorItem(itens.get(0).getId());

      if (emprestimoAtivo == null) {
         throw new Exception("Item não possui empréstimo ativo registrado.");
      }

      for (Item item : itens) {
         emprestimoAtivo.setDataDevolucao(LocalDateTime.now());
         emprestimoDAO.atualizar(emprestimoAtivo);

         item.setStatus(StatusItem.DISPONIVEL);
         itemDAO.atualizar(item);
      }
   }

   public List<Emprestimo> listarEmprestimos() throws Exception {
      return emprestimoDAO.listarTodos();
   }

   public List<Emprestimo> listarEmprestimosAtivos() throws Exception {
      return emprestimoDAO.listarEmprestimosAtivos();
   }

   public List<Emprestimo> buscarEmprestimosPorUsuario(Long idUsuario) throws Exception {
      return emprestimoDAO.buscarPorUsuario(idUsuario);
   }

   public List<Emprestimo> buscarEmprestimosAtivosPorUsuario(Long idUsuario) throws Exception {
      return emprestimoDAO.buscarEmprestimosAtivosPorUsuario(idUsuario);
   }

   public List<Emprestimo> buscarEmprestimosAtrasados() throws Exception {
      return emprestimoDAO.buscarAtrasados();
   }
}