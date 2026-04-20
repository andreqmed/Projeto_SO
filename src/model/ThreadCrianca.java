package model;

import view.PainelAnimacao; // Importação necessária para conversar com a tela

public class ThreadCrianca implements Runnable {
    
    // Parâmetros exclusivos desta criança
    private int id;
    private boolean temBola;
    private long tb; 
    private long td; 
    
    // Referências compartilhadas
    private Cesto cesto;
    private StatusListener listener; 
    private PainelAnimacao painel; // A ponte de comunicação com os desenhos

    // Construtor atualizado para receber o painel de animação
    public ThreadCrianca(int id, boolean temBola, long tb, long td, Cesto cesto, StatusListener listener, PainelAnimacao painel) {
        this.id = id;
        this.temBola = temBola;
        this.tb = tb;
        this.td = td;
        this.cesto = cesto;
        this.listener = listener;
        this.painel = painel;
    }

    // A tarefa CPU-bound que substitui o "dormir"
    private void esperaAtiva(long tempoMillis) {
        long inicio = System.currentTimeMillis();
        while (System.currentTimeMillis() - inicio < tempoMillis) {
            // Mantém o processador trabalhando pesado para simular o tempo no IFCE
        }
    }

    // Atalho para não poluir o código principal (Tabela e Log)
    private void avisarTela(String status) {
        if (listener != null) {
            listener.onStatusMudou(this.id, status);
        }
    }

    // Cole isto na sua classe ThreadCrianca, fora do run()
    private void atualizarInterface() {
        if (listener != null) {
            listener.onQuantidadeBolasMudou(cesto.getQtdBolas());
        }
        if (painel != null) {
            painel.setBolasNoCesto(cesto.getQtdBolas());
        }
    }


@Override
public void run() {
    while (true) {
        try {
            if(!this.temBola){
            // --- FASE 1: CAMINHAR ATÉ O CESTO (Espera Ativa - CPU 100%) ---
            avisarTela("Caminhando para o cesto");
            if (painel != null) painel.onAvatarStatusChanged(this.id, "INDO_PEGAR");
            esperaAtiva(2000); 

            // --- FASE 2: PEGAR BOLA (Bloqueio Passivo - CPU 0%) ---
            avisarTela("Aguardando bola no cesto");
            // Se não houver bolas, a linha abaixo PAUSA a thread no SO
            cesto.bolasDisponiveis.acquire(); 
            
            cesto.mutex.acquire(); 
            cesto.removerBola();
            atualizarInterface();
            cesto.mutex.release();
            cesto.espacosVazios.release(); 
            temBola = true;
        }
            // --- FASE 3: BRINCAR (Espera Ativa - CPU 100%) ---
            avisarTela("Brincando");
            if (painel != null) painel.onAvatarStatusChanged(this.id, "BRINCANDO");
            esperaAtiva(tb); 

            // --- FASE 4: DEVOLVER BOLA (Pode bloquear se o cesto estiver cheio) ---
            avisarTela("Indo guardar a bola");
            if (painel != null) painel.onAvatarStatusChanged(this.id, "INDO_GUARDAR");
            esperaAtiva(2000);

            cesto.espacosVazios.acquire();
            cesto.mutex.acquire();
            cesto.adicionarBola();
            atualizarInterface();
            cesto.mutex.release();
            cesto.bolasDisponiveis.release(); 
            temBola = false;

            // --- FASE 5: DESCANSAR (Espera Ativa - CPU 100%) ---
            avisarTela("Descansando");
            if (painel != null) painel.onAvatarStatusChanged(this.id, "DESCANSANDO");
            esperaAtiva(td); 

            } catch (InterruptedException e) { break; }
        }
    }
}
