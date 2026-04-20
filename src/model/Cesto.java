package model;

import java.util.concurrent.Semaphore;

public class Cesto {
    // Semáforos globais que resolverão os conflitos
    public Semaphore mutex;
    public Semaphore espacosVazios;
    public Semaphore bolasDisponiveis;
    
    // Variável para a interface saber quantas bolas existem para desenhar
    private int qtdBolas;

    public Cesto(int capacidadeK) {
        this.mutex = new Semaphore(1); // Garante que só 1 criança mexa no cesto por vez
        this.espacosVazios = new Semaphore(capacidadeK); // Inicia com K espaços livres
        this.bolasDisponiveis = new Semaphore(0); // Inicia com 0 bolas
        this.qtdBolas = 0;
    }

    public void adicionarBola() {
        this.qtdBolas++;
    }

    public void removerBola() {
        this.qtdBolas--;
    }

    public int getQtdBolas() {
        return this.qtdBolas;
    }
}