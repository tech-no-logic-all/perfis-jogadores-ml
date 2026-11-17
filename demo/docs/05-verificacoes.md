# 5. Verificações e execução

Com JDK 25, execute em PowerShell a partir de demo:

```powershell
New-Item -ItemType Directory -Force target/verificacoes-classes | Out-Null
javac -encoding UTF-8 -d target/verificacoes-classes src/main/java/*.java src/test/java/VerificarPessoa1.java
java '-Djava.awt.headless=true' -cp target/verificacoes-classes VerificarPessoa1
```

Os testes usam comparações explícitas, AssertionError e dados artificiais separados, sem JUnit. Conferem normalização, inversa, coluna constante, uso no KNN, cópias, alinhamento, pureza, grupo vazio, entradas inválidas e PNG. O gráfico artificial fica em target/verificacoes.

Para incluir JDBC, substitua CAMINHO_H2 pelo jar H2 2.2.224:

```powershell
java '-Djava.awt.headless=true' -cp 'target/verificacoes-classes;CAMINHO_H2' VerificarPessoa1 src/main/resources/jogadores.sql
```

O SQL roda somente em banco descartável em memória. A conferência exige 150 registros, 50 por perfil, via DAO. No Linux, separe o classpath por dois-pontos.

Para preparar dados oficiais sem recriar tabelas:

```powershell
java -cp 'target/verificacoes-classes;CAMINHO_H2' PrepararKMeans 'URL_JDBC_EXISTENTE'
```

A Pessoa 2 ainda precisa fornecer resultados K=3 e as inércias mínimas para K=1 a 6. Não foram gerados resultados oficiais nem relatório escolar.
