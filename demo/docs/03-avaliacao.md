# 3. Avaliação

ResultadoKMeans preserva executar e acrescenta os getters combinados para grupos, centroides, inércia, iterações e semente. A Pessoa 2 implementa esse contrato.

AvaliadorKMeans.avaliar(resultado, cenarios, normalizado) valida tamanho e números de grupo. A posição i precisa representar o mesmo jogador da matriz de entrada. Sem IDs no resultado, uma reordenação silenciosa não pode ser detectada.

Um laço conta os perfis de cada grupo. A pureza soma a maior contagem de cada linha e divide pelo total. Grupo vazio soma zero. Número do grupo não é perfil.

AvaliacaoKMeans oferece getTabela(), getTamanhos(), getCentroidesOriginais(), getPureza() e formatar() para tabelas no console. Os arrays devolvidos são cópias. Não gera relatório escolar.

Não há treinamento, seleção de sementes ou cálculo de inércia aqui. A Pessoa 2 fornece a melhor execução. Não compare diretamente inércias originais e normalizadas, pois usam escalas diferentes.
