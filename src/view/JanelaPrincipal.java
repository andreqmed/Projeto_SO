// package view;

// import model.Cesto;
// import model.StatusListener;
// import model.ThreadCrianca;

// import javax.swing.*;
// import javax.swing.table.DefaultTableModel;
// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;

// public class JanelaPrincipal extends JFrame implements StatusListener {

//     // Referência ao motor lógico (Agora começa nulo, pois será criado pelo botão)
//     private Cesto cestoGlobal = null;

//     // Componentes Visuais
//     private DefaultTableModel modeloTabela;
//     private JTextArea areaLog;
//     private JLabel labelBolasCesto;
    
//     // Referência ao painel de desenho
//     private PainelAnimacao painelAnimacao; 

//     // Campos de Entrada
//     private JTextField campoK; 
//     private JTextField campoId;
//     private JCheckBox checkBola;
//     private JTextField campoTb;
//     private JTextField campoTd;

//     // CONSTRUTOR ATUALIZADO: Não recebe mais o cesto por parâmetro
//     public JanelaPrincipal() {
//         // Configurações básicas da Janela
//         setTitle("Simulador - Brincadeira de Crianças");
//         setSize(1000, 700); 
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setLayout(new BorderLayout(10, 10));

//         // Instanciar o painel de animação
//         painelAnimacao = new PainelAnimacao();

//         // --- REORGANIZAÇÃO DO LAYOUT ---
        
//         // 1. O painel superior (entradas) continua no topo
//         add(criarPainelSuperior(), BorderLayout.NORTH);
        
//         // 2. O painel de animação ganha o espaço principal no CENTRO
//         add(painelAnimacao, BorderLayout.CENTER);
        
//         // 3. Agrupar a tabela e o log num painel inferior
//         JPanel painelDadosEmbaixo = new JPanel(new GridLayout(2, 1));
//         painelDadosEmbaixo.add(criarPainelCentral()); // A tabela
//         painelDadosEmbaixo.add(criarPainelInferior()); // O Log
//         painelDadosEmbaixo.setPreferredSize(new Dimension(1000, 250)); // Define a altura
        
//         add(painelDadosEmbaixo, BorderLayout.SOUTH);
//     }

//     // --- CONSTRUÇÃO DOS PAINÉIS ---

//     private JPanel criarPainelSuperior() {
//         JPanel painel = new JPanel(new FlowLayout());
//         painel.setBorder(BorderFactory.createTitledBorder("Criar Nova Criança"));

//         // NOVO: Campo do Cesto (K) inserido no início do formulário
//         painel.add(new JLabel("Capacidade do Cesto (K):"));
//         campoK = new JTextField(3);
//         painel.add(campoK);

//         painel.add(new JLabel("ID:"));
//         campoId = new JTextField(3);
//         painel.add(campoId);

//         checkBola = new JCheckBox("Começa com Bola?");
//         painel.add(checkBola);

//         painel.add(new JLabel("Tb (segundos):"));
//         campoTb = new JTextField(4);
//         painel.add(campoTb);

//         painel.add(new JLabel("Td (segundos):"));
//         campoTd = new JTextField(4);
//         painel.add(campoTd);

//         JButton btnCriar = new JButton("Criar Thread Criança");
//         btnCriar.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 instanciarCrianca();
//             }
//         });
//         painel.add(btnCriar);

//         // Indicador visual de bolas no cesto
//         labelBolasCesto = new JLabel(" | Bolas no Cesto: 0");
//         labelBolasCesto.setFont(new Font("Arial", Font.BOLD, 14));
//         labelBolasCesto.setForeground(Color.BLUE);
//         painel.add(labelBolasCesto);

//         return painel;
//     }

//     private JPanel criarPainelCentral() {
//         JPanel painel = new JPanel(new BorderLayout());
//         painel.setBorder(BorderFactory.createTitledBorder("Estado das Crianças"));

//         // Criação da Tabela para mostrar os status exigidos
//         String[] colunas = {"ID", "Tempo Brincar (Tb)", "Tempo Descanso (Td)", "Status Atual"};
//         modeloTabela = new DefaultTableModel(colunas, 0);
//         JTable tabela = new JTable(modeloTabela);
        
//         painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
//         return painel;
//     }

//     private JPanel criarPainelInferior() {
//         JPanel painel = new JPanel(new BorderLayout());
//         painel.setBorder(BorderFactory.createTitledBorder("Log de Eventos"));

//         areaLog = new JTextArea(8, 50);
//         areaLog.setEditable(false);
        
//         painel.add(new JScrollPane(areaLog), BorderLayout.CENTER);
//         return painel;
//     }

//     // --- LÓGICA DE INTEGRAÇÃO COM AS THREADS ---

//     private void instanciarCrianca() {
//         try {
//             // --- NOVO: LÓGICA DE CRIAÇÃO DO CESTO (Apenas na 1ª criança) ---
//             if (cestoGlobal == null) {
//                 int capacidadeK = Integer.parseInt(campoK.getText());
//                 cestoGlobal = new Cesto(capacidadeK);
//                 // Trava o campo para garantir a integridade da simulação
//                 campoK.setEnabled(false); 
//             }
//             // -------------------------------------------------------------

//             // Lemos os valores originais como TEXTO
//             String valorTbTela = campoTb.getText();
//             String valorTdTela = campoTd.getText();

//             // Conversão matemática para MILISSEGUNDOS
//             long tbLogica = Long.parseLong(valorTbTela) * 1000;
//             long tdLogica = Long.parseLong(valorTdTela) * 1000;

//             int id = Integer.parseInt(campoId.getText());
//             boolean temBola = checkBola.isSelected();

//             // Atualiza a tabela com o valor original digitado
//             modeloTabela.addRow(new Object[]{id, valorTbTela, valorTdTela, "Iniciando..."});

//             // 1. Avisa o Painel para desenhar o boneco na tela
//             painelAnimacao.registrarAvatar(id, temBola);

//             // 2. Cria a thread passando o cesto (que agora com certeza existe)
//             ThreadCrianca novaCrianca = new ThreadCrianca(id, temBola, tbLogica, tdLogica, cestoGlobal, this, painelAnimacao);
//             new Thread(novaCrianca).start();

//             // Limpar os campos (menos o campoK, que fica travado com o valor escolhido)
//             campoId.setText("");
//             campoTb.setText("");
//             campoTd.setText("");
//             checkBola.setSelected(false);

//         } catch (NumberFormatException ex) {
//             JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos com números válidos!", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
//         }
//     }

//     // --- IMPLEMENTAÇÃO DO CONTRATO STATUSLISTENER ---

//     @Override
//     public void onStatusMudou(int idCrianca, String novoStatus) {
//         SwingUtilities.invokeLater(() -> {
//             // Atualizar Log
//             areaLog.append("[ID: " + idCrianca + "] " + novoStatus + "\n");
//             areaLog.setCaretPosition(areaLog.getDocument().getLength()); 

//             // Atualizar Tabela
//             for (int i = 0; i < modeloTabela.getRowCount(); i++) {
//                 if ((int) modeloTabela.getValueAt(i, 0) == idCrianca) {
//                     modeloTabela.setValueAt(novoStatus, i, 3);
//                     break;
//                 }
//             }
//         });
//     }

//     @Override
//     public void onQuantidadeBolasMudou(int qtdAtual) {
//         SwingUtilities.invokeLater(() -> {
//             labelBolasCesto.setText(" | Bolas no Cesto: " + qtdAtual);
//             // Sincroniza o visual do cesto dinâmico
//             if (painelAnimacao != null) {
//                 painelAnimacao.setBolasNoCesto(qtdAtual);
//             }
//         });
//     }
// }

package view;

import model.Cesto;
import model.StatusListener;
import model.ThreadCrianca;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class JanelaPrincipal extends JFrame implements StatusListener {

    private Cesto cestoGlobal = null;
    private List<Thread> listaThreads = new ArrayList<>(); // Para gerenciar o botão reiniciar

    // Componentes Visuais Estilo Game
    private JTextArea areaLog;
    private JLabel labelBolasCesto;
    private PainelAnimacao painelAnimacao;

    // Campos de Entrada (Painel Lateral)
    private JTextField campoK;
    private JTextField campoId;
    private JCheckBox checkBola;
    private JTextField campoTb;
    private JTextField campoTd;
    private JButton btnCriar, btnReiniciar;

    // Fontes estilo "Retro"
    private Font fonteRetro = new Font("Courier New", Font.BOLD, 14);
    private Font fonteTitulo = new Font("Courier New", Font.BOLD, 18);
    private Color corFundoMenu = new Color(40, 40, 45);
    private Color corTextoPrimario = new Color(200, 200, 200);
    private Color corDestaque = new Color(255, 165, 0); // Laranja Hawks

    public JanelaPrincipal() {
        setTitle("Simulador 8-Bits: Quadra do Hawks");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0)); // Sem margens para colar na borda
        getContentPane().setBackground(Color.BLACK);

        painelAnimacao = new PainelAnimacao();

        // 1. Painel de Animação no Centro (Visão Principal)
        add(painelAnimacao, BorderLayout.CENTER);

        // 2. Menu de Criação na Lateral (Estilo Seleção de Personagem)
        add(criarPainelLateralArcade(), BorderLayout.EAST);

        // 3. O HUD de Status na parte inferior (Estilo Caixa de Diálogo)
        add(criarPainelHUDBottom(), BorderLayout.SOUTH);
    }

    // --- CONSTRUÇÃO DOS PAINÉIS "GAMIFICADOS" ---

    private JPanel criarPainelLateralArcade() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(corFundoMenu);
        painel.setPreferredSize(new Dimension(280, 0));
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título Bonito
        JLabel titulo = new JLabel("NOVO JOGADOR");
        titulo.setFont(fonteTitulo);
        titulo.setForeground(corDestaque);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(titulo);
        painel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Estiliza os campos
        campoK = estilizarCampo("Capacidade Cesto (K):", painel);
        campoId = estilizarCampo("ID do Jogador:", painel);
        campoTb = estilizarCampo("Tempo Brincar (seg):", painel);
        campoTd = estilizarCampo("Tempo Descansar (seg):", painel);

        // Checkbox Gamificado
        checkBola = new JCheckBox("Começa com a Bola?");
        checkBola.setFont(fonteRetro);
        checkBola.setForeground(corTextoPrimario);
        checkBola.setBackground(corFundoMenu);
        checkBola.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(checkBola);
        painel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Botão de Criar (Verde)
        btnCriar = new JButton("ADICIONAR NA QUADRA");
        estilizarBotaoArcade(btnCriar, new Color(50, 150, 50));
        btnCriar.addActionListener(e -> instanciarCrianca());
        painel.add(btnCriar);
        
        painel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Botão de Reiniciar (Vermelho)
        btnReiniciar = new JButton("RESETAR SIMULAÇÃO");
        estilizarBotaoArcade(btnReiniciar, new Color(180, 50, 50));
        btnReiniciar.addActionListener(e -> reiniciarSimulacao());
        painel.add(btnReiniciar);

        return painel;
    }

    private JPanel criarPainelHUDBottom() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.BLACK);
        painel.setPreferredSize(new Dimension(1000, 120));
        
        // Uma borda que parece feita de blocos 8-bits
        TitledBorder borda = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(corDestaque, 3), 
                "  EVENTOS DO JOGO  "
        );
        borda.setTitleColor(corDestaque);
        borda.setTitleFont(fonteRetro);
        painel.setBorder(borda);

        // O Log é a "Caixa de Diálogo"
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setBackground(new Color(20, 20, 20));
        areaLog.setForeground(Color.WHITE);
        areaLog.setFont(new Font("Courier New", Font.PLAIN, 14));
        
        JScrollPane scroll = new JScrollPane(areaLog);
        scroll.setBorder(null); // Tira a borda feia do JScrollPane
        painel.add(scroll, BorderLayout.CENTER);

        // Indicador de Bolas (Display digital estilo placa)
        JPanel painelPlacar = new JPanel();
        painelPlacar.setBackground(Color.BLACK);
        painelPlacar.setBorder(new EmptyBorder(0, 20, 0, 20));
        
        labelBolasCesto = new JLabel("CESTO: 0");
        labelBolasCesto.setFont(new Font("Courier New", Font.BOLD, 24));
        labelBolasCesto.setForeground(new Color(0, 255, 255)); // Azul Neon
        painelPlacar.add(labelBolasCesto);
        
        painel.add(painelPlacar, BorderLayout.EAST);

        return painel;
    }

    // --- FUNÇÕES AUXILIARES DE ESTILO ---

    private JTextField estilizarCampo(String labelText, JPanel painel) {
        JLabel label = new JLabel(labelText);
        label.setFont(fonteRetro);
        label.setForeground(corTextoPrimario);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(label);

        JTextField campo = new JTextField(10);
        campo.setFont(fonteRetro);
        campo.setHorizontalAlignment(JTextField.CENTER);
        campo.setMaximumSize(new Dimension(150, 30));
        campo.setBackground(new Color(60, 60, 65));
        campo.setForeground(Color.WHITE);
        campo.setBorder(BorderFactory.createLineBorder(corDestaque, 1));
        painel.add(campo);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        return campo;
    }

    private void estilizarBotaoArcade(JButton btn, Color corBase) {
        btn.setFont(fonteRetro);
        btn.setForeground(Color.WHITE);
        btn.setBackground(corBase);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // --- LÓGICA DE INTEGRAÇÃO COM AS THREADS ---

    private void instanciarCrianca() {
        try {
            if (cestoGlobal == null) {
                int capacidadeK = Integer.parseInt(campoK.getText());
                cestoGlobal = new Cesto(capacidadeK);
                campoK.setEnabled(false);
                campoK.setBackground(Color.DARK_GRAY);
                areaLog.append(">>> SISTEMA INICIADO: Capacidade Máxima do Cesto = " + capacidadeK + "\n");
            }

            long tbLogica = Long.parseLong(campoTb.getText()) * 1000;
            long tdLogica = Long.parseLong(campoTd.getText()) * 1000;
            int id = Integer.parseInt(campoId.getText());
            boolean temBola = checkBola.isSelected();

            // 1. Avisa o Painel para desenhar o boneco na tela
            painelAnimacao.registrarAvatar(id, temBola);

            // 2. Cria a thread e guarda na lista (para poder matar no Reset)
            ThreadCrianca novaCrianca = new ThreadCrianca(id, temBola, tbLogica, tdLogica, cestoGlobal, this, painelAnimacao);
            Thread t = new Thread(novaCrianca);
            listaThreads.add(t);
            t.start();

            // Limpa os campos
            campoId.setText("");
            campoTb.setText("");
            campoTd.setText("");
            checkBola.setSelected(false);
            campoId.requestFocus();

        } catch (NumberFormatException ex) {
            areaLog.append("[ERRO] Falha no sistema! Insira números válidos nos parâmetros.\n");
        }
    }

    private void reiniciarSimulacao() {
        // Interrompe (mata) todas as threads ativas
        for (Thread t : listaThreads) {
            t.interrupt();
        }
        listaThreads.clear();
        
        // Destrói o cesto atual para permitir novo 'K'
        cestoGlobal = null; 
        campoK.setEnabled(true);
        campoK.setBackground(new Color(60, 60, 65));
        campoK.setText("");
        
        // Limpa a interface
        painelAnimacao.limparTudo();
        areaLog.setText(">>> SIMULAÇÃO RESETADA. INSERIR NOVOS PARÂMETROS.\n");
        labelBolasCesto.setText("CESTO: 0");
    }

    // --- IMPLEMENTAÇÃO DO CONTRATO STATUSLISTENER ---

    @Override
    public void onStatusMudou(int idCrianca, String novoStatus) {
        SwingUtilities.invokeLater(() -> {
            // Log estilo RPG
            areaLog.append("[Jogador " + idCrianca + "] -> " + novoStatus + "\n");
            areaLog.setCaretPosition(areaLog.getDocument().getLength());
        });
    }

    @Override
    public void onQuantidadeBolasMudou(int qtdAtual) {
        SwingUtilities.invokeLater(() -> {
            labelBolasCesto.setText("CESTO: " + qtdAtual);
            if (painelAnimacao != null) {
                painelAnimacao.setBolasNoCesto(qtdAtual);
            }
        });
    }
}