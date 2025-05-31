package com.biblioteca;

import javax.swing.SwingUtilities;

import com.biblioteca.view.TelaInicial;

public class Main {
   public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> {
         new TelaInicial().setVisible(true);
      });
   }
}