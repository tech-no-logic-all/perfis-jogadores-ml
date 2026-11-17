# 4. Gráfico do cotovelo

GraficoCotovelo.gerar recebe seis inércias prontas, caminho e título. Índice zero significa K=1; índice cinco, K=6. Valores devem ser finitos e não negativos.

BufferedImage guarda a imagem, Graphics2D desenha e ImageIO salva PNG. São recursos do JDK, sem bibliotecas externas nem janela. X representa K, Y é proporcional à inércia. Quando todos os valores são zero, a escala usa 1 para evitar divisão por zero.

Gere imagens separadas para os dois cenários. A classe não calcula inércia, escolhe sementes ou escreve relatório.
