package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;

public class PainelAnimacao extends JPanel {

// --- VARIÁVEIS DE IMAGEM ---
private Image imgCesto, imgBola, imgFundo;
private Image imgParado, imgWalk1, imgWalk2, imgDrible, imgSentado,imgCostas;

// --- VARIÁVEIS DAS SPRITE SHEETS ---
private BufferedImage sheetCorpo, sheetBlusa, sheetSapato;

// --- MOTOR DA ANIMAÇÃO ---
private Timer timerAnimacao;
private List<Avatar> listaAvatars = new ArrayList<>();
private Point coordsCesto = new Point();
private Random random = new Random();
private int larguraTela, alturaTela;
private int bolasNoCesto = 0;

private final int TAMANHO_SPRITE = 200; 
private final int METADE_SPRITE = TAMANHO_SPRITE / 2;

public PainelAnimacao() {
    setBackground(Color.BLACK); 

    // 1. CARREGAMENTO DAS IMAGENS
    try {
        imgCesto = ImageIO.read(new File("img/cesto_bola.png"));
        imgBola = ImageIO.read(new File("img/Bola.png")); 
        imgFundo = ImageIO.read(new File("img/quadra.png"));
        
        sheetCorpo = ImageIO.read(new File("img/fbas_1body_human_00.png"));
        sheetSapato = ImageIO.read(new File("img/fbas_03fot1_shoes_00a.png"));
        sheetBlusa = ImageIO.read(new File("img/fbas_05shrt_shortshirt_00a.png"));

        imgParado = montarFrameVestiario(0, 2); 
        imgWalk1 = montarFrameVestiario(2, 4);   
        imgWalk2 = montarFrameVestiario(5, 4);    
        imgDrible = montarFrameVestiario(2, 0); 
        imgSentado = montarFrameVestiario(1, 2);
        imgCostas = montarFrameVestiario(0, 1);
        
    } catch (IOException e) {
        System.out.println("Erro ao carregar imagens! Verifique os nomes na pasta 'img'.");
    }

    // 2. O RELÓGIO (MOTOR DE PASSOS)
    timerAnimacao = new Timer(16, e -> {
        larguraTela = getWidth(); 
        alturaTela = getHeight();
        
        if(coordsCesto != null) {
            coordsCesto.setLocation(larguraTela / 2, alturaTela - 150); 
        }
        
        boolean precisaRedesenhar = false;

        for (Avatar avatar : listaAvatars) {
            int velocidadeSteps = 5; 
            boolean emMovimento = false;
            
            if (avatar.tempoRestante > 0) {
             long agora = System.currentTimeMillis();
             long fim = avatar.inicioContagem + avatar.tempoRestante;
             avatar.tempoRestante = Math.max(0, fim - agora);
            }
            // Deslocamento X e Direção do Olhar
            if (Math.abs(avatar.x_atual - avatar.x_destino) > velocidadeSteps) {
                if (avatar.x_destino > avatar.x_atual) {
                    avatar.x_atual += velocidadeSteps;
                    avatar.viradoParaEsquerda = false; 
                } else {
                    avatar.x_atual -= velocidadeSteps;
                    avatar.viradoParaEsquerda = true;  
                }
                emMovimento = true;
            }
            
            // Deslocamento Y
            if (Math.abs(avatar.y_atual - avatar.y_destino) > velocidadeSteps) {
                avatar.y_atual += (avatar.y_destino > avatar.y_atual) ? velocidadeSteps : -velocidadeSteps;
                emMovimento = true;
            }

            // Troca de Sprites (Animação)
            if (emMovimento) {
                avatar.contadorPassos++;
                if ((avatar.contadorPassos / 12) % 2 == 0) {
                    avatar.imagemAtual = imgWalk1;
                } else {
                    avatar.imagemAtual = imgWalk2;
                }
                precisaRedesenhar = true;
            } else {
                avatar.contadorPassos = 0; 
                String st = avatar.statusVisual.toUpperCase();

                if (st.contains("DESCANS")) {
                    avatar.imagemAtual = imgSentado;
                    avatar.viradoParaEsquerda = true;
                
                } else if (st.contains("BRINCA") || st.contains("JOGA")) {
                    avatar.imagemAtual = imgDrible;

                } else if (st.contains("ESPERANDO_CESTO")) {
                    avatar.imagemAtual = imgCostas;
                
                } else {
                    avatar.imagemAtual = imgParado;
                }
            }

            // Animação da bola quicando
            if (avatar.statusVisual.toUpperCase().contains("BRINCA") && avatar.temBola && !emMovimento) {
                avatar.offset_bola += avatar.velocidade_bola;
                if (avatar.offset_bola > 20 || avatar.offset_bola < -20) {
                    avatar.velocidade_bola *= -1; 
                }
                precisaRedesenhar = true;
            } else {
                avatar.offset_bola = 0; 
            }
        }
        
        if(precisaRedesenhar) {
            repaint();
        } 
    });
    timerAnimacao.start();
}

// =========================================================================
// MÉTODO AUXILIAR: A Costureira
// =========================================================================
private BufferedImage montarFrameVestiario(int coluna, int linha) {
    int tamanhoFrame = 64; 
    int xCorte = coluna * tamanhoFrame;
    int yCorte = linha * tamanhoFrame;

    BufferedImage frameMontado = new BufferedImage(tamanhoFrame, tamanhoFrame, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = frameMontado.createGraphics();

    BufferedImage pedacoCorpo = sheetCorpo.getSubimage(xCorte, yCorte, tamanhoFrame, tamanhoFrame);
    BufferedImage pedacoBlusa = sheetBlusa.getSubimage(xCorte, yCorte, tamanhoFrame, tamanhoFrame);
    BufferedImage pedacoSapato = sheetSapato.getSubimage(xCorte, yCorte, tamanhoFrame, tamanhoFrame);

    g2d.drawImage(pedacoCorpo, 0, 0, null);  
    g2d.drawImage(pedacoBlusa, 0, 0, null);  
    g2d.drawImage(pedacoSapato, 0, 0, null); 

    g2d.dispose(); 
    return frameMontado;
}

// --- ÁREA DE DESENHO VISUAL ---
@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g); 
    if (larguraTela == 0) return;
    
    if (imgFundo != null) {
        g.drawImage(imgFundo, 0, 0, larguraTela, alturaTela, this);
    }
    
    if (imgCesto != null) {
        g.drawImage(imgCesto, coordsCesto.x - 50, coordsCesto.y - 50, 100, 100, this);
        if (bolasNoCesto > 0) {
            g.drawImage(imgBola, coordsCesto.x - 20, coordsCesto.y - 20, 40, 40, this);
        }
    }

    for (Avatar avatar : listaAvatars) {
        if (avatar.imagemAtual != null) {
            int xDraw = avatar.x_atual - METADE_SPRITE;
            int yDraw = avatar.y_atual - METADE_SPRITE;
            
            g.setColor(avatar.cor);
            g.setFont(new Font("Arial", Font.BOLD, 16));
                        
            g.drawString(
                "ID: " + avatar.id,
                avatar.x_atual - 20,
                avatar.y_atual - 80
            );

            long agora = System.currentTimeMillis();
            long decorrido = agora - avatar.inicioContagem;
            long restante = avatar.tempoTotalAtual - decorrido;

            if (restante < 0) restante = 0;

            if (avatar.tempoTotalAtual > 0) {
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.BOLD, 14));
            
                long segundos = restante / 1000;
            
                g.drawString(
                    segundos + "s",
                    avatar.x_atual - 10,
                    avatar.y_atual - 60
                );
            }
   
            // ESPELHAMENTO DO JAVA
            if (avatar.viradoParaEsquerda) {
                int larguraImg = avatar.imagemAtual.getWidth(null);
                int alturaImg = avatar.imagemAtual.getHeight(null);
                g.drawImage(avatar.imagemAtual, 
                    xDraw + TAMANHO_SPRITE, yDraw, 
                    xDraw, yDraw + TAMANHO_SPRITE, 
                    0, 0, larguraImg, alturaImg, this);
            } else {
                g.drawImage(avatar.imagemAtual, xDraw, yDraw, TAMANHO_SPRITE, TAMANHO_SPRITE, this);
            }
        }

        if (avatar.temBola && imgBola != null) {
            int xBola = avatar.viradoParaEsquerda ? avatar.x_atual - 40 : avatar.x_atual + 20; 
            int yBolaAnimada = avatar.y_atual + 10 + avatar.offset_bola;
            g.drawImage(imgBola, xBola, yBolaAnimada, 35, 35, this);
        }
    }
}

// --- CONECTORES COM A LÓGICA ---
public void registrarAvatar(int id, boolean temBola, long tb, long td) {
    Avatar novo = new Avatar(id, temBola, tb, td);
    novo.x_atual = (id % 2 == 0) ? larguraTela + 100 : -100;
    novo.y_atual = 500; 
    
    if (temBola) {
        novo.statusVisual = "BRINCANDO";
        definirDestinoAleatorioBrincar(novo);
    } else {
        novo.statusVisual = "INDO_PEGAR";
        novo.x_destino = (larguraTela / 2) + 80 + ((id % 10) * 45); 
        novo.y_destino = alturaTela - 150;
    }
    
    novo.imagemAtual = imgParado;
    listaAvatars.add(novo);
    this.repaint();
}

public void onAvatarStatusChanged(int id, String novoStatusVisual) {
    for (Avatar avatar : listaAvatars) {
        if (avatar.id == id) {
            avatar.statusVisual = novoStatusVisual.toUpperCase();
            String status = avatar.statusVisual;
            //avatar.inicioContagem = System.currentTimeMillis();

            if (status.contains("BRINCA")) {
                avatar.tempoTotalAtual = avatar.tempoBrincar;
                avatar.inicioContagem = System.currentTimeMillis();
            } 
            else if (status.contains("DESCANSAR")) {
                avatar.tempoTotalAtual = avatar.tempoDescansar;
                avatar.inicioContagem = System.currentTimeMillis();
            } 
            else if (status.contains("FILA") || status.contains("ESPERA") || status.contains("PEGAR")) {
                // 👇 FILA NÃO MOSTRA TEMPO
                avatar.tempoTotalAtual = 0;
            }
            // --- SOLUÇÃO 2: MAIS PALAVRAS-CHAVE ---
            
            // 1. ZONA DE BRINCADEIRA
            if (status.contains("BRINCA") || status.contains("JOGA")) {
                avatar.temBola = true;
                definirDestinoAleatorioBrincar(avatar);
            } 
            // 2. LARGANDO OU BUSCANDO A BOLA NO CESTO
            // Agora ele entende a palavra "CESTO" do seu Log!
            else if (status.contains("GUARDAR") || status.contains("DEVOLVER") || status.contains("BUSCAR") || (status.contains("CESTO") && avatar.temBola)) {
                avatar.x_destino = (larguraTela / 2);
                avatar.y_destino = alturaTela - 150;
            } 
            // 3. ZONA DE FILA 
            else if (status.contains("PEGAR") || status.contains("ESPERA") || status.contains("FILA") || (status.contains("CESTO") && !avatar.temBola)) {
                // avatar.temBola = false;
                // avatar.x_destino = (larguraTela / 2) + 80 + ((avatar.id % 10) * 45);
                // avatar.y_destino = alturaTela - 150;
                avatar.temBola = false;
    
                // Configurações do bloco da fila
                int limitePorLinha = 5;
                int espacamentoX = 50; // Distância horizontal entre os ombros dos bonecos
                int espacamentoY = 50; // Distância vertical entre a linha da frente e a de trás
    
                // A Matemática de quebra de linha
                int coluna = avatar.id % limitePorLinha;
                int linha  = avatar.id / limitePorLinha;
    
                // Ponto inicial da fila
                int xInicial = (larguraTela / 2) + 70;
                int yInicial = alturaTela - 150; // Subimos de 150 para 250 para caber mais linhas abaixo
    
                // Aplicando os destinos
                avatar.x_destino = xInicial + (coluna * espacamentoX);
                avatar.y_destino = yInicial + (linha * espacamentoY);
            } 
            // 4. ZONA DE DESCANSO 
            else if (status.contains("DESCANSAR") || status.contains("BANCO")) {
                avatar.temBola = false;
                avatar.x_destino = larguraTela - 250 + ((avatar.id % 5) * 30);
                avatar.y_destino = alturaTela - 200; 
            }
            // 5. ZONA DE ESPERA PARA DEVOLVER (Embaixo do cesto)
            else if (status.contains("ESPERANDO_CESTO")) {
                // Aqui a criança AINDA TEM a bola, ela só não conseguiu guardar!
                avatar.temBola = true; 

                 // Configurações do bloco da fila central
                int limitePorLinha = 5;
                int espacamentoX = 45; 
                int espacamentoY = 50; 
                
                int posicaoFila = 0;

                for (Avatar a : listaAvatars) {
                    if (a == avatar) continue;

                    if (a.statusVisual.contains("ESPERANDO_CESTO")) {
                        posicaoFila++;
                    }
                }

                int coluna = posicaoFila % limitePorLinha;
                int linha  = posicaoFila / limitePorLinha;

                // MATEMÁTICA DA POSIÇÃO:
                // Puxamos um pouco para a esquerda (-90) para o meio da fila alinhar com o cesto
                int xInicial = (larguraTela / 2) - 90; 
                // Colocamos o Y maior (-50 ou -20) para ficar ABAIXO do cesto na tela
                int yInicial = alturaTela - 50; 

                avatar.x_destino = xInicial + (coluna * espacamentoX);
                avatar.y_destino = yInicial + (linha * espacamentoY);
            }
             break;
        }
    }
}

public void setBolasNoCesto(int qtd) {
    this.bolasNoCesto = qtd;
}

public void limparTudo() {
    this.listaAvatars.clear();
    this.bolasNoCesto = 0;
    this.repaint();
}

public void atualizarCronometroAvatar(int id, long segundosRestantes) {
        for (Avatar avatar : listaAvatars) {
            if (avatar.id == id) {
                // Atualize aqui com o nome exato da variável que você usou para desenhar o texto!
                avatar.tempoRestante = segundosRestantes; 
                break;
            }
        }
        repaint();
    }

private Color gerarCorAleatoria() {
Random rand = new Random();
return new Color(
    rand.nextInt(200) + 30,
    rand.nextInt(200) + 30,
    rand.nextInt(200) + 30
);
}
// --- CLASSE INTERNA ---
private class Avatar {
    int id;
    boolean temBola;
    int x_atual, y_atual; 
    int x_destino, y_destino; 
    String statusVisual = "CRIADO"; 
    Image imagemAtual; 
    Color cor;
    boolean viradoParaEsquerda = false; 
    int offset_bola = 0; 
    int velocidade_bola = 3; 
    int contadorPassos = 0; 
    long tempoBrincar;
    long tempoDescansar;
    
    long tempoRestante;
    long tempoTotalAtual = 0;
    long inicioContagem = 0;
    
    public Avatar(int id, boolean temBola, long tb, long td) {
        this.id = id;
        this.temBola = temBola;
        this.tempoBrincar = tb;
        this.tempoDescansar = td;
        this.cor = gerarCorAleatoria();
}
}

private void definirDestinoAleatorioBrincar(Avatar avatar) {
    avatar.x_destino = 50 + random.nextInt((larguraTela / 2) - 80);
    avatar.y_destino = alturaTela - 250 + random.nextInt(200); 
}
}
