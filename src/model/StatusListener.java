package model;

public interface StatusListener {
    // A thread vai usar isso para gritar: "Mudei de status!"
    void onStatusMudou(int idCrianca, String novoStatus);
    
    // A thread vai usar isso para avisar que tirou/colocou uma bola
    void onQuantidadeBolasMudou(int qtdAtual);
}
