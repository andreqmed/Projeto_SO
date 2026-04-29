package view;

import model.Cesto;
import model.StatusListener;
import model.ThreadCrianca;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class JanelaPrincipal extends JFrame implements StatusListener {

private Cesto cestoGlobal = null;
private List<Thread> listaThreads = new ArrayList<>(); // Para gerenciar o botão reiniciar
private List<Integer> idsEmUso = new ArrayList<>();

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
    setTitle("Simulador 8-Bits: Quadra de Basquete");
    setSize(1100, 750);
    // setResizable(false); // Trava a tela! Bloqueia o botão de maximizar e as bordas.
    // setLocationRelativeTo(null); // Faz a janela abrir bem no centro do monitor do usuário.
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

        // Checa se o ID já existe na nossa memória
        if (idsEmUso.contains(id)) {
            JOptionPane.showMessageDialog(this, "O Jogador com ID " + id + " já está na quadra!", "ID Duplicado", JOptionPane.WARNING_MESSAGE);
            campoId.setText(""); // Limpa só o campo do ID pra ele tentar de novo
            campoId.requestFocus();
            return; // O 'return' cancela o resto do método, impedindo a criação do clone!
        }
        idsEmUso.add(id);

        // 1. Avisa o Painel para desenhar o boneco na tela
        painelAnimacao.registrarAvatar(id, temBola, tbLogica, tdLogica);

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
        if (t != null && t.isAlive()) {
            t.interrupt(); // Manda o sinal de parada para o SO
        }
    }
    listaThreads.clear();

    idsEmUso.clear(); // Libera os IDs para serem usados novamente
    
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