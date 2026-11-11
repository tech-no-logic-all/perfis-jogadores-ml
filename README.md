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
