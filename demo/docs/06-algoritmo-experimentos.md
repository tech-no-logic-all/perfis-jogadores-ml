# 6. Algoritmo e experimentos da Pessoa 2

## Funcionamento

`KMeans.executar(dados, k, maxIteracoes, semente)` recebe apenas características numéricas. O perfil real nunca participa do treinamento nem da escolha da melhor execução. A implementação usa somente o JDK.

1. Sorteia K índices distintos entre as linhas, com `Random(semente)` e embaralhamento parcial. Valores repetidos são permitidos, mas a mesma linha não é sorteada duas vezes.
2. Atribui cada ponto ao centroide com menor distância euclidiana ao quadrado. Isso produz a mesma escolha que a distância euclidiana, sem calcular a raiz. Em empate exato, mantém o grupo anterior; na primeira atribuição vence o menor índice.
3. Para cada grupo vazio, transfere o ponto mais distante de seu centroide atual, considerando somente grupos com pelo menos dois membros. Empates são resolvidos pela ordem das linhas. Assim não esvazia o grupo doador, inclusive quando há pontos idênticos.
4. Atualiza os centroides pela média das características dos membros.
5. Para quando as atribuições completas deixam de mudar ou atinge o limite. Uma iteração inclui atribuição, reparação e atualização. Ao atingir o limite, os centroides ainda são as médias dos grupos retornados, mas a convergência não é garantida.
6. Calcula a inércia usando os grupos e centroides finais: soma das distâncias quadradas.

O resultado implementa a interface `ResultadoKMeans` da Pessoa 1 e guarda grupos, centroides, inércia, iterações e semente. Os arrays são devolvidos por cópia e a entrada não é modificada. O método `executar` exigido pela interface delega a um novo treinamento, sem alterar o resultado existente.

Entradas devem ser matrizes retangulares não vazias, com números finitos, 1 <= K <= número de pontos e limite positivo. Operações que excedam a capacidade de `double` são rejeitadas.

## Configurações e seleção

`ExperimentosKMeans` executa uma lista de sementes e escolhe a menor inércia. Em empate exato, mantém a primeira execução da lista. É possível consultar todas as execuções para estudar a variação entre inicializações.

`ExecutarKMeans` usa sementes de **0 a 29**, limite de **300 iterações**, K de **1 a 6**, nos cenários min-max e original. A normalização usa todos os jogadores, pois não há divisão treino/teste. Para K=3, usa a avaliação da Pessoa 1 para tamanhos, centroides em escala original, tabela por perfil e pureza.

As inércias são comparadas apenas dentro do mesmo cenário e K ao selecionar a semente. Seus valores absolutos não são comparáveis entre escalas. A pureza é calculada depois do treinamento e não influencia a seleção. Números de grupos não representam nomes de perfis.

## Compilar e verificar

No Linux/WSL, a partir de `demo` (JDK 11 ou superior):

```sh
mkdir -p target/verificacoes-classes
javac -encoding UTF-8 -d target/verificacoes-classes src/main/java/*.java src/main/java/*/*.java src/test/java/kmeans/*.java
java -Djava.awt.headless=true -cp target/verificacoes-classes kmeans.VerificarPessoa2
```

Para verificar também JDBC e todos os 360 treinamentos, use o jar H2 2.2.224:

```sh
java -Djava.awt.headless=true -cp 'target/verificacoes-classes:CAMINHO_H2' kmeans.VerificarPessoa2 src/main/resources/sql/jogadores.sql
```

Essa verificação carrega o SQL exclusivamente em banco descartável em memória. Os artefatos ficam em `target/verificacoes/pessoa2`. Os testes conferem médias e inércia conhecidas, reprodutibilidade, cópias, limite de uma iteração, K=1, K=N, grupos vazios, pontos idênticos, entradas inválidas e seleção entre sementes. Execute também `VerificarPessoa1` conforme o documento 05.

Para usar um banco existente, sem executar o SQL de criação:

```sh
java -Djava.awt.headless=true -cp 'target/verificacoes-classes:CAMINHO_H2' kmeans.ExecutarKMeans 'URL_JDBC_EXISTENTE' target/kmeans
```

No PowerShell, use `;` como separador do classpath. Credenciais opcionais vêm de `JDBC_USER` e `JDBC_PASSWORD`. O driver correspondente ao banco precisa estar no classpath.

Saídas: `resultados.txt`, `execucoes.csv` (todas as sementes, com inércia e pureza), `grupos-min-max.csv`, `grupos-original.csv` (ID e grupo para K=3) e dois gráficos `cotovelo-*.png`.

## Execução de referência

Com o SQL versionado, JDK 11 e H2 2.2.224, a verificação integrada produziu:

| Cenário, K=3 | Semente selecionada | Iterações | Inércia | Pureza | Tamanhos |
| --- | --- | --- | --- | --- | --- |
| Min-max | 1 | 7 | 10,554837136368 | 80% | 57, 51, 42 |
| Original | 6 | 10 | 256229,3140640782 | 51,33% | 53, 42, 55 |

Os resultados indicam maior correspondência com os perfis após min-max nesse experimento. A comparação da pureza é válida; a comparação direta das duas inércias não é. O banco usado em produção pode gerar resultados diferentes se os dados mudarem. Este documento descreve a parte da Pessoa 2 e fornece material para o relatório conjunto; não substitui o relatório escolar completo nem a pergunta específica da Parte E.
