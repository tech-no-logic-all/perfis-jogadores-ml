# 2. Normalização e cenários

NormalizadorMinMax.ajustar guarda mínimos e máximos. transformar aplica (valor - mínimo) / (máximo - mínimo). Coluna constante recebe zero. inverter recupera as unidades originais. Vetores e matrizes retornados são cópias.

No K-Means, ajustar com todos os jogadores. No KNN, somente com o treino. Dados novos podem sair de [0,1]; não recortamos nem reajustamos automaticamente.

CenariosKMeans oferece getOriginais(), getNormalizados() e inverterCentroides(). Guarda os parâmetros internamente.

PrepararKMeans.carregar(conexao) conecta a preparação ao DAO. O main recebe a URL JDBC de banco já carregado. Credenciais opcionais: JDBC_USER e JDBC_PASSWORD. Não recria tabelas.
