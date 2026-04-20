//###################BASQUETE######################
// package view;

// import javax.swing.*;
// import java.awt.*;
// import java.io.File;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Random;
// import javax.imageio.ImageIO;

// public class PainelAnimacao extends JPanel {

//     // --- VARIÁVEIS DE IMAGEM ---
//     private Image imgCesto, imgBola, imgFundo;
//     private Image imgParado, imgRun1, imgRun2, imgDrible, imgSentado;

//     // --- MOTOR DA ANIMAÇÃO ---
//     private Timer timerAnimacao;
//     private List<Avatar> listaAvatars = new ArrayList<>();
//     private Point coordsCesto = new Point();
//     private Random random = new Random();
//     private int larguraTela, alturaTela;
//     private int bolasNoCesto = 0;
    
//     // Tamanho do desenho (escala) das imagens de 600x600
//     private final int TAMANHO_SPRITE = 200; 
//     private final int METADE_SPRITE = TAMANHO_SPRITE / 2;

//     public PainelAnimacao() {
//         setBackground(Color.BLACK); 

//         // 1. Carregamento Seguro das Imagens
//         try {
//             imgCesto = ImageIO.read(new File("img/cesto_bola.png"));
//             imgBola = ImageIO.read(new File("img/Bola.png")); 
//             imgFundo = ImageIO.read(new File("img/quadra.png"));
            
//             imgParado = ImageIO.read(new File("img/jogador_parado_sem_fundo.png"));
//             imgRun1 = ImageIO.read(new File("img/run_1.png"));
//             imgRun2 = ImageIO.read(new File("img/Run_2.png"));
//             imgDrible = ImageIO.read(new File("img/Driblando_sem_fundo.png"));
//             imgSentado = ImageIO.read(new File("img/sentado_sem_fundo.png"));
            
//         } catch (IOException e) {
//             System.out.println("Erro ao carregar imagens! Verifique os nomes na pasta 'img'.");
//         }

//         // 2. O Relógio (60 FPS)
//         timerAnimacao = new Timer(16, e -> {
//             larguraTela = getWidth(); 
//             alturaTela = getHeight();
            
//             // CORREÇÃO 1: Posição do Cesto ajustada para baixo (Chão da quadra)
//             if(coordsCesto != null) {
//                 // Em vez de alturaTela / 2, colocamos ele mais para o fundo da tela.
//                 // Se ainda ficar voando, aumente esse número 150 para 50, por exemplo.
//                 coordsCesto.setLocation(larguraTela / 2, alturaTela - 150); 
//             }
            
//             boolean precisaRedesenhar = false;

//             for (Avatar avatar : listaAvatars) {
//                 int velocidadeSteps = 5; 
//                 boolean emMovimento = false;
                
//                 // Deslocamento X e CORREÇÃO 2: Descobrindo para onde ele olha
//                 if (Math.abs(avatar.x_atual - avatar.x_destino) > velocidadeSteps) {
//                     if (avatar.x_destino > avatar.x_atual) {
//                         avatar.x_atual += velocidadeSteps;
//                         avatar.viradoParaEsquerda = false; // Olha para a direita
//                     } else {
//                         avatar.x_atual -= velocidadeSteps;
//                         avatar.viradoParaEsquerda = true;  // Olha para a esquerda
//                     }
//                     emMovimento = true;
//                 }
                
//                 // Deslocamento Y
//                 if (Math.abs(avatar.y_atual - avatar.y_destino) > velocidadeSteps) {
//                     avatar.y_atual += (avatar.y_destino > avatar.y_atual) ? velocidadeSteps : -velocidadeSteps;
//                     emMovimento = true;
//                 }

//                 // LÓGICA DA ANIMAÇÃO DE SPRITES
//                 if (emMovimento) {
//                     avatar.contadorPassos++;
//                 if ((avatar.contadorPassos / 5) % 2 == 0) {
//                     avatar.imagemAtual = imgRun1;
//                 }   
//                 else {
//                     avatar.imagemAtual = imgRun2;
//                 }
//                      precisaRedesenhar = true;
//                     } 
//                     else {
//                         avatar.contadorPassos = 0; 
//                         String st = avatar.statusVisual.toUpperCase();
    
//                      // Prioridade para a pose de sentado se ele estiver parado e o status for descansar
//                     if (st.contains("DESCANSANDO") && !st.contains("INDO")) {
//                     avatar.imagemAtual = imgSentado;
//                     } else if (st.contains("BRINCANDO")) {
//                         avatar.imagemAtual = imgDrible;
//                     } else {
//                         avatar.imagemAtual = imgParado;
//                     }
//                 }

//                 // Animação da bola quicando
//                 if (avatar.statusVisual.equals("BRINCANDO") && avatar.temBola && !emMovimento) {
//                     avatar.offset_bola += avatar.velocidade_bola;
//                     if (avatar.offset_bola > 20 || avatar.offset_bola < -20) {
//                         avatar.velocidade_bola *= -1; 
//                     }
//                     precisaRedesenhar = true;
//                 } else {
//                     avatar.offset_bola = 0; 
//                 }
//             }
            
//             if(precisaRedesenhar) {
//                 repaint();
//             } 
//         });
//         timerAnimacao.start();
//     }

//     // --- ÁREA DE DESENHO VISUAL ---
//     @Override
//     protected void paintComponent(Graphics g) {
//         super.paintComponent(g); 
//         if (larguraTela == 0) return;
        
//         if (imgFundo != null) {
//             g.drawImage(imgFundo, 0, 0, larguraTela, alturaTela, this);
//         }
        
//         if (imgCesto != null) {
//             g.drawImage(imgCesto, coordsCesto.x - 50, coordsCesto.y - 50, 100, 100, this);
//             if (bolasNoCesto > 0) {
//                 g.drawImage(imgBola, coordsCesto.x - 20, coordsCesto.y - 20, 40, 40, this);
//             }
//         }

//         for (Avatar avatar : listaAvatars) {
//             if (avatar.imagemAtual != null) {
//                 int xDraw = avatar.x_atual - METADE_SPRITE;
//                 int yDraw = avatar.y_atual - METADE_SPRITE;

//                 // CORREÇÃO 3: O TRUQUE DE ESPELHAMENTO DO JAVA
//                 if (avatar.viradoParaEsquerda) {
//                     // Desenha invertido horizontalmente
//                     int larguraImg = avatar.imagemAtual.getWidth(null);
//                     int alturaImg = avatar.imagemAtual.getHeight(null);
                    
//                     g.drawImage(avatar.imagemAtual, 
//                         xDraw + TAMANHO_SPRITE, yDraw, // Canto superior direito da tela
//                         xDraw, yDraw + TAMANHO_SPRITE, // Canto inferior esquerdo da tela
//                         0, 0,                          // Canto superior esquerdo da imagem real
//                         larguraImg, alturaImg,         // Canto inferior direito da imagem real
//                         this);
//                 } else {
//                     // Desenha normal
//                     g.drawImage(avatar.imagemAtual, xDraw, yDraw, TAMANHO_SPRITE, TAMANHO_SPRITE, this);
//                 }
//             }

//             // A Bola também precisa acompanhar a mão se ele estiver invertido!
//             if (avatar.temBola && imgBola != null) {
//                 // Se ele olha para a esquerda, a bola quica na esquerda dele
//                 int xBola = avatar.viradoParaEsquerda ? avatar.x_atual - 40 : avatar.x_atual + 20; 
//                 int yBolaAnimada = avatar.y_atual + 10 + avatar.offset_bola;
//                 g.drawImage(imgBola, xBola, yBolaAnimada, 35, 35, this);
//             }
//         }
//     }

//     // --- CONECTORES COM A LÓGICA ---
//     public void registrarAvatar(int id, boolean temBola) {
//         Avatar novo = new Avatar(id, temBola);
        
//         novo.x_atual = (id % 2 == 0) ? larguraTela + 100 : -100;
//         novo.y_atual = alturaTela - 150 + random.nextInt(100); // Nasce no chão também
        
//         if (temBola) {
//             novo.statusVisual = "BRINCANDO";
//             definirDestinoAleatorioBrincar(novo);
//         } else {
//             novo.statusVisual = "INDO_PEGAR";
//             novo.x_destino = coordsCesto.x;
//             novo.y_destino = coordsCesto.y;
//         }
        
//         novo.imagemAtual = imgParado;
//         listaAvatars.add(novo);
//         this.repaint();
//     }

//     public void onAvatarStatusChanged(int id, String novoStatusVisual) {
//         for (Avatar avatar : listaAvatars) {
//         if (avatar.id == id) {
//             // Guardamos em maiúsculo para evitar erros de comparação
//             avatar.statusVisual = novoStatusVisual.toUpperCase();
//             String status = avatar.statusVisual;

//             if (status.contains("BRINCANDO")) {
//                 avatar.temBola = true;
//                 definirDestinoAleatorioBrincar(avatar);
//             } 
//             else if (status.contains("GUARDAR") || status.contains("PEGAR")) {
//                 avatar.x_destino = coordsCesto.x - 30 + random.nextInt(60);
//                 avatar.y_destino = coordsCesto.y - 30 + random.nextInt(60);
//             } 
//             // ORDEM IMPORTANTE: Primeiro checamos se ele está "INDO"
//             else if (status.contains("INDO") && status.contains("DESCANSAR")) {
//                 avatar.temBola = false;
//                 // Alvo: Parte de baixo da arquibancada
//                 avatar.x_destino = (larguraTela / 2) + 200;
//                 avatar.y_destino = alturaTela - 200; 
//             } 
//             // Se não estiver "INDO", mas o status for "DESCANSANDO", ele chegou
//             else if (status.contains("DESCANSANDO")) {
//                 avatar.temBola = false;
//                 // Alvo: No banco lá atrás (Y menor sobe na tela)
//                 avatar.x_destino = (larguraTela / 2) + 200;
//                 avatar.y_destino = alturaTela - 200; 
             
//             }
//             break;
//         }
//     }
// }
    
   

//     public void setBolasNoCesto(int qtd) {
//         this.bolasNoCesto = qtd;
//     }
    
//     public void limparTudo() {
//         this.listaAvatars.clear();
//         this.bolasNoCesto = 0;
//         this.repaint();
//     }
    
//     // --- CLASSE INTERNA ---
//     private class Avatar {
//         int id;
//         boolean temBola;
//         int x_atual, y_atual; 
//         int x_destino, y_destino; 
//         String statusVisual = "CRIADO"; 
//         Image imagemAtual; 
        
//         boolean viradoParaEsquerda = false; // NOVO: Controle de espelhamento
        
//         int offset_bola = 0; 
//         int velocidade_bola = 3; 
//         int contadorPassos = 0; 

//         public Avatar(int id, boolean temBola) {
//             this.id = id;
//             this.temBola = temBola;
//         }
//     }
    
//     private void definirDestinoAleatorioBrincar(Avatar avatar) {
//         avatar.x_destino = 50 + random.nextInt((larguraTela / 2) - 100);
//         avatar.y_destino = alturaTela - 250 + random.nextInt(200); 
//     }
// }




//################AVATAR###########################

// package view;

// import javax.swing.*;
// import java.awt.*;
// import java.awt.image.BufferedImage;
// import java.io.File;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Random;
// import javax.imageio.ImageIO;

// public class PainelAnimacao extends JPanel {

//     // --- VARIÁVEIS DE IMAGEM ---
//     private Image imgCesto, imgBola, imgFundo;
//     private Image imgParado, imgWalk1, imgWalk2, imgDrible, imgSentado;
    
//     // --- VARIÁVEIS DAS SPRITE SHEETS ---
//     private BufferedImage sheetCorpo, sheetBlusa, sheetSapato;

//     // --- MOTOR DA ANIMAÇÃO ---
//     private Timer timerAnimacao;
//     private List<Avatar> listaAvatars = new ArrayList<>();
//     private Point coordsCesto = new Point();
//     private Random random = new Random();
//     private int larguraTela, alturaTela;
//     private int bolasNoCesto = 0;
    
//     // Tamanho do desenho (escala) das imagens
//     private final int TAMANHO_SPRITE = 200; 
//     private final int METADE_SPRITE = TAMANHO_SPRITE / 2;

//     public PainelAnimacao() {
//         setBackground(Color.BLACK); 

//         // 1. CARREGAMENTO DAS IMAGENS
//         try {
//             imgCesto = ImageIO.read(new File("img/cesto_bola.png"));
//             imgBola = ImageIO.read(new File("img/Bola.png")); 
//             imgFundo = ImageIO.read(new File("img/quadra.png"));
            
//             // Carrega as folhas de Sprite (Assegure-se que o nome na pasta img está idêntico)
//             sheetCorpo = ImageIO.read(new File("img/fbas_1body_human_00.png"));
//             sheetSapato = ImageIO.read(new File("img/fbas_03fot1_shoes_00a.png"));
//             sheetBlusa = ImageIO.read(new File("img/fbas_05shrt_shortshirt_00a.png"));

//             // 2. RECORTA E VESTE O BONECO 
//             // Formato: montarFrameVestiario(Coluna, Linha)
//             // IMPORTANTE: Altere os números abaixo para bater com as poses corretas da sua imagem!
//             imgParado = montarFrameVestiario(0, 2); 
//             imgWalk1 = montarFrameVestiario(2, 4);   // Perna direita à frente
//             imgWalk2 = montarFrameVestiario(5, 4);   // Perna esquerda à frente   
//             imgDrible = montarFrameVestiario(2, 0); // Usando a de parado provisoriamente
//             imgSentado = montarFrameVestiario(2, 1); 
            
//         } catch (IOException e) {
//             System.out.println("Erro ao carregar imagens! Verifique os nomes na pasta 'img'.");
//         }

//         // 3. O RELÓGIO (MOTOR DE PASSOS)
//         timerAnimacao = new Timer(16, e -> {
//             larguraTela = getWidth(); 
//             alturaTela = getHeight();
            
//             // Mantém o cesto no chão
//             if(coordsCesto != null) {
//                 coordsCesto.setLocation(larguraTela / 2, alturaTela - 150); 
//             }
            
//             boolean precisaRedesenhar = false;

//             for (Avatar avatar : listaAvatars) {
//                 int velocidadeSteps = 5; 
//                 boolean emMovimento = false;
                
//                 // Deslocamento X e Direção do Olhar
//                 if (Math.abs(avatar.x_atual - avatar.x_destino) > velocidadeSteps) {
//                     if (avatar.x_destino > avatar.x_atual) {
//                         avatar.x_atual += velocidadeSteps;
//                         avatar.viradoParaEsquerda = false; 
//                     } else {
//                         avatar.x_atual -= velocidadeSteps;
//                         avatar.viradoParaEsquerda = true;  
//                     }
//                     emMovimento = true;
//                 }
                
//                 // Deslocamento Y
//                 if (Math.abs(avatar.y_atual - avatar.y_destino) > velocidadeSteps) {
//                     avatar.y_atual += (avatar.y_destino > avatar.y_atual) ? velocidadeSteps : -velocidadeSteps;
//                     emMovimento = true;
//                 }

//                 // Troca de Sprites (Animação)
//                 if (emMovimento) {
//                         avatar.contadorPassos++;
                    
//                     // Mudamos de 5 para 12 para deixar a caminhada mais lenta e natural
//                         if ((avatar.contadorPassos / 12) % 2 == 0) {
//                             avatar.imagemAtual = imgWalk1;
//                         }
//                         else {
//                         avatar.imagemAtual = imgWalk2;
//                         }
//                         precisaRedesenhar = true;
//                 } else {
//                     avatar.contadorPassos = 0; 
//                     String st = avatar.statusVisual.toUpperCase();
    
//                     if (st.contains("DESCANSANDO") && !st.contains("INDO")) {
//                         avatar.imagemAtual = imgSentado;
//                     } else if (st.contains("BRINCANDO")) {
//                         avatar.imagemAtual = imgDrible;
//                     } else {
//                         avatar.imagemAtual = imgParado;
//                     }
//                 }

//                 // Animação da bola quicando
//                 if (avatar.statusVisual.toUpperCase().contains("BRINCANDO") && avatar.temBola && !emMovimento) {
//                     avatar.offset_bola += avatar.velocidade_bola;
//                     if (avatar.offset_bola > 20 || avatar.offset_bola < -20) {
//                         avatar.velocidade_bola *= -1; 
//                     }
//                     precisaRedesenhar = true;
//                 } else {
//                     avatar.offset_bola = 0; 
//                 }
//             }
            
//             if(precisaRedesenhar) {
//                 repaint();
//             } 
//         });
//         timerAnimacao.start();
//     }

//     // =========================================================================
//     // NOVO MÉTODO AUXILIAR: A Costureira
//     // =========================================================================
//     private BufferedImage montarFrameVestiario(int coluna, int linha) {
//         int tamanhoFrame = 64; 
        
//         int xCorte = coluna * tamanhoFrame;
//         int yCorte = linha * tamanhoFrame;

//         BufferedImage frameMontado = new BufferedImage(tamanhoFrame, tamanhoFrame, BufferedImage.TYPE_INT_ARGB);
//         Graphics2D g2d = frameMontado.createGraphics();

//         BufferedImage pedacoCorpo = sheetCorpo.getSubimage(xCorte, yCorte, tamanhoFrame, tamanhoFrame);
//         BufferedImage pedacoBlusa = sheetBlusa.getSubimage(xCorte, yCorte, tamanhoFrame, tamanhoFrame);
//         BufferedImage pedacoSapato = sheetSapato.getSubimage(xCorte, yCorte, tamanhoFrame, tamanhoFrame);

//         g2d.drawImage(pedacoCorpo, 0, 0, null);  
//         g2d.drawImage(pedacoBlusa, 0, 0, null);  
//         g2d.drawImage(pedacoSapato, 0, 0, null); 

//         g2d.dispose(); 
//         return frameMontado;
//     }

//     // --- ÁREA DE DESENHO VISUAL ---
//     @Override
//     protected void paintComponent(Graphics g) {
//         super.paintComponent(g); 
//         if (larguraTela == 0) return;
        
//         if (imgFundo != null) {
//             g.drawImage(imgFundo, 0, 0, larguraTela, alturaTela, this);
//         }
        
//         if (imgCesto != null) {
//             g.drawImage(imgCesto, coordsCesto.x - 50, coordsCesto.y - 50, 100, 100, this);
//             if (bolasNoCesto > 0) {
//                 g.drawImage(imgBola, coordsCesto.x - 20, coordsCesto.y - 20, 40, 40, this);
//             }
//         }

//         for (Avatar avatar : listaAvatars) {
//             if (avatar.imagemAtual != null) {
//                 int xDraw = avatar.x_atual - METADE_SPRITE;
//                 int yDraw = avatar.y_atual - METADE_SPRITE;

//                 // ESPELHAMENTO DO JAVA
//                 if (avatar.viradoParaEsquerda) {
//                     int larguraImg = avatar.imagemAtual.getWidth(null);
//                     int alturaImg = avatar.imagemAtual.getHeight(null);
                    
//                     g.drawImage(avatar.imagemAtual, 
//                         xDraw + TAMANHO_SPRITE, yDraw, 
//                         xDraw, yDraw + TAMANHO_SPRITE, 
//                         0, 0, larguraImg, alturaImg, this);
//                 } else {
//                     g.drawImage(avatar.imagemAtual, xDraw, yDraw, TAMANHO_SPRITE, TAMANHO_SPRITE, this);
//                 }
//             }

//             if (avatar.temBola && imgBola != null) {
//                 int xBola = avatar.viradoParaEsquerda ? avatar.x_atual - 40 : avatar.x_atual + 20; 
//                 int yBolaAnimada = avatar.y_atual + 10 + avatar.offset_bola;
//                 g.drawImage(imgBola, xBola, yBolaAnimada, 35, 35, this);
//             }
//         }
//     }

//     // --- CONECTORES COM A LÓGICA ---
//     public void registrarAvatar(int id, boolean temBola) {
//         Avatar novo = new Avatar(id, temBola);
        
//         novo.x_atual = (id % 2 == 0) ? larguraTela + 100 : -100;
//         novo.y_atual = 500; // Valor fixo seguro para não nascer no teto
        
//         if (temBola) {
//             novo.statusVisual = "BRINCANDO";
//             definirDestinoAleatorioBrincar(novo);
//         } else {
//             novo.statusVisual = "INDO_PEGAR";
//             novo.x_destino = coordsCesto.x;
//             novo.y_destino = coordsCesto.y;
//         }
        
//         novo.imagemAtual = imgParado;
//         listaAvatars.add(novo);
//         this.repaint();
//     }

//     public void onAvatarStatusChanged(int id, String novoStatusVisual) {
//         for (Avatar avatar : listaAvatars) {
//             if (avatar.id == id) {
//                 avatar.statusVisual = novoStatusVisual.toUpperCase();
//                 String status = avatar.statusVisual;

//                 if (status.contains("BRINCANDO")) {
//                     avatar.temBola = true;
//                     definirDestinoAleatorioBrincar(avatar);
//                 } 
//                 else if (status.contains("GUARDAR") || status.contains("PEGAR")) {
//                     avatar.x_destino = coordsCesto.x - 30 + random.nextInt(60);
//                     avatar.y_destino = coordsCesto.y - 30 + random.nextInt(60);
//                 } 
//                 else if (status.contains("INDO") && status.contains("DESCANSAR")) {
//                     avatar.temBola = false;
//                     avatar.x_destino = (larguraTela / 2) + 200;
//                     avatar.y_destino = alturaTela - 200; 
//                 } 
//                 else if (status.contains("DESCANSANDO")) {
//                     avatar.temBola = false;
//                     avatar.x_destino = (larguraTela / 2) + 200;
//                     avatar.y_destino = alturaTela - 200; 
//                 }
//                 break;
//             }
//         }
//     }

//     public void setBolasNoCesto(int qtd) {
//         this.bolasNoCesto = qtd;
//     }
    
//     public void limparTudo() {
//         this.listaAvatars.clear();
//         this.bolasNoCesto = 0;
//         this.repaint();
//     }
    
//     // --- CLASSE INTERNA ---
//     private class Avatar {
//         int id;
//         boolean temBola;
//         int x_atual, y_atual; 
//         int x_destino, y_destino; 
//         String statusVisual = "CRIADO"; 
//         Image imagemAtual; 
        
//         boolean viradoParaEsquerda = false; 
//         int offset_bola = 0; 
//         int velocidade_bola = 3; 
//         int contadorPassos = 0; 

//         public Avatar(int id, boolean temBola) {
//             this.id = id;
//             this.temBola = temBola;
//         }
//     }
    
//     private void definirDestinoAleatorioBrincar(Avatar avatar) {
//         avatar.x_destino = 50 + random.nextInt((larguraTela / 2) - 100);
//         avatar.y_destino = alturaTela - 250 + random.nextInt(200); 
//     }
// }




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
    private Image imgParado, imgWalk1, imgWalk2, imgDrible, imgSentado;
    
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
                    
                    // --- SOLUÇÃO 1: CORREÇÃO DA DIREÇÃO (O OLHAR) ---
                    // Se parou na fila esperando, forçamos ele a olhar para a esquerda (pro cesto)
                    if (st.contains("ESPERA") || st.contains("FILA") || st.contains("PEGAR") || st.contains("CESTO")) {
                        avatar.viradoParaEsquerda = true;
                    }

                    if (st.contains("DESCANSAR") && !st.contains("INDO")) {
                        avatar.imagemAtual = imgSentado;
                        avatar.viradoParaEsquerda = true; // Senta olhando para o jogo
                    } else if (st.contains("BRINCA") || st.contains("JOGA")) {
                        avatar.imagemAtual = imgDrible;
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
    public void registrarAvatar(int id, boolean temBola) {
        Avatar novo = new Avatar(id, temBola);
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

                // --- SOLUÇÃO 2: MAIS PALAVRAS-CHAVE ---
                
                // 1. ZONA DE BRINCADEIRA
                if (status.contains("BRINCA") || status.contains("JOGA")) {
                    avatar.temBola = true;
                    definirDestinoAleatorioBrincar(avatar);
                } 
                // 2. LARGANDO A BOLA NO CESTO
                // Agora ele entende a palavra "CESTO" do seu Log!
                else if (status.contains("GUARDAR") || status.contains("DEVOLVER") || (status.contains("CESTO") && avatar.temBola)) {
                    avatar.x_destino = (larguraTela / 2);
                    avatar.y_destino = alturaTela - 150;
                } 
                // 3. ZONA DE FILA 
                else if (status.contains("PEGAR") || status.contains("ESPERA") || status.contains("FILA") || (status.contains("CESTO") && !avatar.temBola)) {
                    avatar.temBola = false;
                    avatar.x_destino = (larguraTela / 2) + 80 + ((avatar.id % 10) * 45);
                    avatar.y_destino = alturaTela - 150;
                } 
                // 4. ZONA DE DESCANSO 
                else if (status.contains("DESCANSAR") || status.contains("BANCO")) {
                    avatar.temBola = false;
                    avatar.x_destino = larguraTela - 250 + ((avatar.id % 5) * 30);
                    avatar.y_destino = alturaTela - 200; 
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
    
    // --- CLASSE INTERNA ---
    private class Avatar {
        int id;
        boolean temBola;
        int x_atual, y_atual; 
        int x_destino, y_destino; 
        String statusVisual = "CRIADO"; 
        Image imagemAtual; 
        
        boolean viradoParaEsquerda = false; 
        int offset_bola = 0; 
        int velocidade_bola = 3; 
        int contadorPassos = 0; 

        public Avatar(int id, boolean temBola) {
            this.id = id;
            this.temBola = temBola;
        }
    }
    
    private void definirDestinoAleatorioBrincar(Avatar avatar) {
        avatar.x_destino = 50 + random.nextInt((larguraTela / 2) - 80);
        avatar.y_destino = alturaTela - 250 + random.nextInt(200); 
    }
}