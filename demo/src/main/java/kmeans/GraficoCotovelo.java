package kmeans;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import javax.imageio.ImageIO;


// Desenha o cotovelo com recursos do Java, sem biblioteca de graficos
public final class GraficoCotovelo {
    // Recebe seis inercias ja calculadas, uma para cada K de 1 a 6
    public void gerar(double[] inercias, Path caminho, String titulo) throws IOException {
        if (inercias == null || inercias.length != 6) throw new IllegalArgumentException("Informe seis inércias, de K = 1 a 6.");
        double max = 0;
        for (double v : inercias) {
            if (!Double.isFinite(v) || v < 0) throw new IllegalArgumentException("Inércias devem ser finitas e não negativas.");
            max = Math.max(max, v);
        }
        // Mesmo com todos os valores zerados, precisamos de uma escala diferente de zero
        if (max == 0) max = 1;
        // Criamos uma imagem em memoria O Graphics2D funciona como um pincel sobre ela
        BufferedImage imagem = new BufferedImage(960, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = imagem.createGraphics();
        try {
            // Suaviza as bordas e prepara o fundo, o titulo e os nomes dos eixos
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.WHITE); g.fillRect(0, 0, 960, 600);
            g.setFont(new Font("SansSerif", Font.BOLD, 20)); g.setColor(Color.BLACK);
            g.drawString(titulo, 120, 40);
            g.setFont(new Font("SansSerif", Font.PLAIN, 15));
            g.drawString("Inércia", 20, 75); g.drawString("K (quantidade de grupos)", 370, 570);
            // Divide a altura do grafico em cinco intervalos e escreve os valores da escala
            for (int i = 0; i <= 5; i++) {
                int y = 510 - i * 80;
                g.setColor(new Color(225, 225, 225)); g.drawLine(140, y, 900, y);
                g.setColor(Color.BLACK);
                g.drawString(String.format(Locale.ROOT, "%.3g", max * i / 5), 25, y + 5);
            }
            g.drawLine(140, 110, 140, 510); g.drawLine(140, 510, 900, 510);
            int anteriorX = 0, anteriorY = 0;
            g.setStroke(new BasicStroke(2));
            // Na imagem, Y cresce para baixo Por isso subtraimos a altura calculada de 510
            // X avanca uma distancia fixa para cada K A linha liga o ponto atual ao anterior
            for (int i = 0; i < 6; i++) {
                int x = 140 + i * 152, y = 510 - (int) Math.round(inercias[i] / max * 400);
                g.setColor(Color.BLACK); g.drawString(Integer.toString(i + 1), x - 4, 535);
                g.setColor(new Color(25, 95, 170));
                if (i > 0) g.drawLine(anteriorX, anteriorY, x, y);
                g.fillOval(x - 5, y - 5, 10, 10);
                anteriorX = x; anteriorY = y;
            }
        // Libera os recursos do desenho mesmo se alguma operacao falhar
        } finally { g.dispose(); }
        // Cria a pasta de destino, se necessario, e grava a imagem como PNG
        Path absoluto = caminho.toAbsolutePath();
        Files.createDirectories(absoluto.getParent());
        if (!ImageIO.write(imagem, "png", absoluto.toFile())) throw new IOException("Codificador PNG indisponível.");
    }
}
