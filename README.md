# 🧵 Simulação de Concorrência com Threads

## 📌 Descrição

Este projeto tem como objetivo demonstrar, de forma prática e interativa, o comportamento de threads em um ambiente concorrente.

A aplicação simula múltiplas threads (crianças) acessando um recurso compartilhado (cesto), evidenciando problemas como condição de corrida e deadlock, além de apresentar soluções utilizando mecanismos de sincronização como mutex e semáforos.

---

## 🎯 Objetivo

- Demonstrar concorrência entre threads
- Controlar acesso a recursos compartilhados
- Evitar condição de corrida
- Prevenir deadlocks
- Representar visualmente o comportamento das threads

---

## 🧠 Conceitos Aplicados

### 🔹 Threads
Threads são unidades de execução que permitem múltiplas tarefas rodarem simultaneamente dentro de um mesmo processo.

---

### 🔹 Região Crítica

A região crítica do projeto é o **cesto**, onde ocorre o acesso ao recurso compartilhado.

> ⚠️ Problema: duas threads não podem acessar o cesto ao mesmo tempo.

---

### 🔹 Condição de Corrida

Ocorre quando múltiplas threads acessam simultaneamente o mesmo recurso, gerando inconsistência.

---

### 🔹 Deadlock

Situação em que duas ou mais threads ficam bloqueadas esperando recursos umas das outras.

---

### 🔹 Exclusão Mútua (Mutex)

Garante que apenas uma thread por vez acesse a região crítica.

---

### 🔹 Semáforos

Controlam o acesso ao recurso compartilhado, evitando conflitos entre threads.

---

## 🧺 Ponto Crítico do Sistema

O principal ponto crítico do projeto é o **cesto**.

- Recurso compartilhado entre as threads
- Controlado por mecanismos de sincronização
- Evita acesso simultâneo
- Previne inconsistência e deadlock

---

## 📁 Estrutura do Projeto
📦 projeto
 ┣ 📂 src
 ┃ ┣ 📂 model
 ┃ ┃ ┣ 📄 Cesto.java
 ┃ ┃ ┣ 📄 StatusLinear.java
 ┃ ┃ ┗ 📄 ThreadCrianca.java
 ┃ ┃
 ┃ ┣ 📂 view
 ┃ ┃ ┣ 📄 JanelaPrincipal.java
 ┃ ┃ ┗ 📄 PainelAnimacao.java
 ┃ ┃
 ┃ ┗ 📂 img
 ┃   ┣ 📄 sprite1.png
 ┃   ┣ 📄 sprite2.png
 ┃   ┗ 📄 ...

 
### 🔹 model
Contém a lógica do sistema:

- **Cesto.java** → recurso compartilhado (região crítica)
- **StatusLinear.java** → controle de estado/processo
- **ThreadCrianca.java** → implementação das threads

### 🔹 view
Responsável pela interface gráfica:

- **JanelaPrincipal.java** → interface principal
- **PainelAnimacao.java** → animação das threads

- ### 🔹 Img
POssui as imagnes utilizadas na animação:
- Bola.png
-cesto_bola.png
-quadra.png
-spread sheets(Body, Shoes e Shirts)

---

## 🎮 Animação

O projeto utiliza **sprite sheets** para representar visualmente o comportamento das threads.

### ✔ Funcionalidades:
- Movimentação dos personagens
- Representação visual da concorrência
- Integração entre lógica e interface

---

## 🧰 Tecnologias Utilizadas

- Java
- Threads (Thread API)
- Semáforos / Mutex
- Programação concorrente
- Animação com sprite sheets

---

## ▶️ Como Executar

### 🔹 Versão compilada

O executável do projeto está disponível na release:

👉 **v1.0**

---

### 🔹 Executar manualmente

```bash
# compilar
javac -d bin src/model/*.java src/view/*.java

# executar (classe principal)
java -cp bin view.Main

```markdown
> Execute a classe que contém o método `main`.

'''

---

## 📦 Requirements

Para executar o projeto, é necessário:

- Java JDK 8 ou superior
- Variável de ambiente JAVA_HOME configurada
- Terminal ou prompt de comando

### Opcional:
- IDE (IntelliJ, Eclipse ou VS Code)
- Sistema operacional Windows, Linux ou macOS

### Recursos adicionais:
- Suporte a interface gráfica (Swing/AWT)
- Imagens utilizadas na animação (pasta `src/img`)
