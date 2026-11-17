# perfis-jogadores-ml
Análise de perfis de jogadores com KNN e K-Means em Java, integrada a banco de dados

## Compilar e executar

Com JDK 11 ou superior e Maven instalados, execute na raiz do repositório:

```sh
cd demo
mvn package
java -cp target/classes Main
```

As classes Java ficam em `src/main/java/` e o script do banco H2 em `src/main/resources/jogadores.sql`. O Maven grava a compilação em `target/`. A pasta `bin/` está disponível para compilação pela IDE.

## K-Means: partes da Pessoa 1

Documentação técnica organizada por parte:

1. [Preparação dos jogadores](demo/docs/01-dados.md)
2. [Normalização e cenários](demo/docs/02-normalizacao.md)
3. [Avaliação](demo/docs/03-avaliacao.md)
4. [Gráfico do cotovelo](demo/docs/04-grafico.md)
5. [Verificações e execução](demo/docs/05-verificacoes.md)

O código usa classes, arrays, listas, laços e condicionais. O algoritmo e a seleção de sementes são da Pessoa 2. Esta documentação explica o código; não inclui o relatório escolar. O pom.xml atual exige JDK 25.
