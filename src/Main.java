
import view.JanelaPrincipal;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        
        // Garante que a interface gráfica inicie de forma segura no Java
        SwingUtilities.invokeLater(() -> {
            
            // 1. Instancia a Visão: Cria a tela e entrega o cesto para ela
            JanelaPrincipal tela = new JanelaPrincipal();
            
            // 2. Exibe a interface para o utilizador
            tela.setVisible(true);
            
        });
    }
}
