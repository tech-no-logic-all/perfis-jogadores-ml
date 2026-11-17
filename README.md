# perfis-jogadores-ml
Análise de perfis de jogadores com KNN e K-Means em Java, integrada a banco de dados

## Compilar e executar

Com JDK 25 e Maven instalados (conforme o pom.xml), execute na raiz do repositório:

```sh
cd demo
mvn package
java -cp target/classes Main
```

As classes Java ficam em `src/main/java/` e o script do banco H2 em `src/main/resources/sql/jogadores.sql`. O Maven grava a compilação em `target/`. A pasta `bin/` está disponível para compilação pela IDE.

## K-Means: partes da Pessoa 1

Documentação técnica organizada por parte:

1. [Preparação dos jogadores](demo/docs/01-dados.md)
2. [Normalização e cenários](demo/docs/02-normalizacao.md)
3. [Avaliação](demo/docs/03-avaliacao.md)
4. [Gráfico do cotovelo](demo/docs/04-grafico.md)
5. [Verificações e execução](demo/docs/05-verificacoes.md)
6. [Algoritmo e experimentos da Pessoa 2](demo/docs/06-algoritmo-experimentos.md)

O código usa classes, arrays, listas, laços e condicionais. A Pessoa 2 implementou o algoritmo, a seleção de sementes e a entrada `kmeans.ExecutarKMeans`, com saídas para ambos os cenários. Esta documentação explica o código; não inclui o relatório escolar. A compilação direta das verificações também foi validada com JDK 11.

## Organização dos pacotes

```text
demo/src/
  main/
    java/
      Main.java          # Entrada do teste de KNN
      modelo/            # Jogador e Perfil, compartilhados
      sql/               # Interface DAO e implementação JDBC
      knn/               # Algoritmo KNN
      kmeans/            # Preparação, normalização, avaliação e gráfico
    resources/
      sql/jogadores.sql  # Script de criação e carga do banco
  test/
    java/kmeans/         # Verificações da Pessoa 1
```

As declarações package acompanham as pastas. Classes de outros pacotes são usadas por import. O Main continua no pacote padrão. Para executar a preparação, use kmeans.PrepararKMeans; para as verificações, kmeans.VerificarPessoa1. Os comandos completos estão na documentação de verificações.
