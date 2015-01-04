package allen.client;

import sun.applet.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Allen on 14/12/20.
 */
public class PaintPanel extends JPanel {

    Color backgroundColor = Color.orange;
    private int width = 0;
    private int height = 0;
    private int blockWidth = MainFrame.blockWidth;

    public PaintPanel(int width, int height) {
        this.setVisible(true);
        this.width = width;
        this.height = height;
        this.setSize(this.width, this.height);
    }

    /**
     * 绘制方法
     */
    public void paint(Graphics gr) {

        //双缓冲
        BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_3BYTE_BGR); // 创建一张500＊500的缓冲图片
        Graphics gr2 = image.getGraphics(); // 获取缓冲图片的画笔

        gr2.setColor(backgroundColor);
        gr2.fillRect(0, 0, this.width, this.height);

        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 12; j++) {

                switch (MainFrame.blockArray[i][11 - j]) {
                    case 1:
                        gr2.setColor(Color.white);
                        break;
                    case 2:
                        gr2.setColor(Color.cyan);
                        break;
                    case 3:
                        gr2.setColor(Color.gray);
                        break;
                    case 4:
                        gr2.setColor(Color.pink);
                        break;
                    case 0:
                        gr2.setColor(Color.orange);
                        break;
                }
                gr2.fillRect(i * blockWidth, (j + 1) * blockWidth, blockWidth, blockWidth);
            }
        }
        gr2.setColor(Color.white);
        gr2.fillRect(196, 0, 4, 670);
        gr2.fillRect(0, 47, 200, 4);
        gr.drawImage(image, 0, 0, this); // 将缓冲图片画到窗体上
    }

}
