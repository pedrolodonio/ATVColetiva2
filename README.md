# Atividade Coletiva 2

## Sobre o Projeto

Este repositório contém os arquivos referentes a atividade coletiva do segundo bimestre da disciplina de Programação Concorrente Distribuída. A atividade consiste em um projeto para representar um sistema de busca distribuído utilizando sockets.O projeto foi desenvolvido em Java 17 na IDE VScode com uso do apache-maven-3.9.9 . 

## Como Executar

Para executar o usuário deve ter instalado o JDK 17, o Extension pack for Java (disponível na loja de expansões do vscode) e o apache-maven 3.9.9 devidamente configurado.

Após o download dos arquivos do projeto, o usuário deve localizar a pasta **arquivos.json** que contém os arquivos .json a serem utilizados na execução do programa . Após isso o caminho dos aquivods deve ser alterado na classe **Main.java** nas linhas 10 e 11,respectivamente.



~~~
        
10      String caminhoArquivoA = "path do arquivo data_A.json"; 
11      String caminhoArquivoB = "path do arquivo data_B.json"; 

~~~

Após a substituição é importante executar o comando:

~~~
mvn compile
~~~

Para executar é possível executar tanto diretamente quanto pelo código:

~~~
mvn exec:java
~~~
