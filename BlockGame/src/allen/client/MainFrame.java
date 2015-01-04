package allen.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;

/**
 * Created by Allen on 14/12/20.
 */

public class MainFrame extends JFrame {

    public static int blockWidth = 50;

    private int score = 0;
    ScorePanel scorePanel;

    private int width = blockWidth * 6;
    private int height = blockWidth * 13 + 20;
    PaintPanel paintPanel;
    JLabel usernameField;

    public static int blockArray[][] = new int[4][13];
    private static boolean changedBlocks[][] = new boolean[4][13];

    OutputStreamWriter out;

    public MainFrame() {

        this.setBounds(300, 100, width, height); // 设置窗体的位置、大小
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗体右上角那个叉关闭程序
        this.setVisible(true); // 设置窗体可见
        this.setBackground(Color.orange);
        scorePanel = new ScorePanel(210, 50);
        this.add(scorePanel);

        usernameField = new JLabel();
        usernameField.setBounds(210, 400, 80, 50);
        usernameField.setText(" ");
        usernameField.setBackground(Color.orange);
        usernameField.setForeground(Color.white);
        usernameField.setFocusable(false);
        usernameField.setFont(new Font("Dialog", 1, 18));
        this.add(usernameField);

        paintPanel = new PaintPanel(blockWidth * 4, height);
        paintPanel.setLocation(0, 0);
        this.add(paintPanel);
        paintPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int x = mouseEvent.getX() / blockWidth;
                int y = 12 - mouseEvent.getY() / blockWidth;
                // overflow
                if (x < 0 || y < 0 || x > 3 || y > 11 || blockArray[x][y] == 0)
                    return;
                removeBlock(x, y);
                update();
                if (gameOver()) {
                    endGame();
                    init();
                }
                paintPanel.repaint();
                scorePanel.setText(String.valueOf(score));
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });

        init();
    }

    private void init() {
        score = 0;
        scorePanel.setText(String.valueOf(score));
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                blockArray[i][j] = 0;
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                blockArray[i][j] = random.nextInt(4) + 1;
            }
        }
    }

    private void endGame() {
        JOptionPane.showMessageDialog(this, "Game Over!", "Message", JOptionPane.PLAIN_MESSAGE);
        try {
            out = MainThread.writer;
            if (out != null) {
                out.write("score:-" + score + "\n");
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void update() {

        boolean someBlockRemoved = true;
        while (someBlockRemoved) {
            // blocks down
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 11; j++) {
                    int temp = 0;
                    if (blockArray[i][j] == 0) {
                        changedBlocks[i][j] = true;
                        while (j  + temp + 1 < 12 && blockArray[i][j + temp + 1] == 0)
                            temp++;
                        blockArray[i][j] = blockArray[i][j + temp + 1];
                        blockArray[i][j + temp + 1] = 0;
                    }
                }
            }
            someBlockRemoved = findLinedBlocks();
        }


        // up one step
        for (int i = 0; i < 4; i++) {
            for (int j = 12; j >= 1; j--) {
                blockArray[i][j] = blockArray[i][j - 1];
            }
        }
        // random generation
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 4; i++) {
            blockArray[i][0] = random.nextInt(4) + 1;
        }

    }

    private boolean findLinedBlocks() {
        boolean ret = false;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                int temp = blockArray[i][j];
                if (temp == 0)
                    continue;
                if (temp == blockArray[i][j + 1] && temp == blockArray[i][j + 2]
                        && (!changedBlocks[i][j] && (changedBlocks[i][j + 1] || changedBlocks[i][j + 2]))) {
                    removeBlock(i, j);
                    ret = true;
                }
                if (j >= 1 && temp == blockArray[i][j - 1] && temp == blockArray[i][j + 1]
                        && (!changedBlocks[i][j] && (changedBlocks[i][j - 1] || changedBlocks[i][j + 1]))) {
                    removeBlock(i, j);
                    ret = true;
                }
                if ((i == 0 || i == 1) && temp == blockArray[i + 1][j] && temp == blockArray[i + 2][j]
                        && (!changedBlocks[i][j] && (changedBlocks[i + 1][j] || changedBlocks[i + 2][j]))) {
                    removeBlock(i, j);
                    ret = true;
                }
                if ((i == 1 || i == 2) && temp == blockArray[i - 1][j] && temp == blockArray[i + 1][j]
                        && (!changedBlocks[i][j] && (changedBlocks[i - 1][j] || changedBlocks[i + 1][j]))) {
                    removeBlock(i, j);
                    ret = true;
                }
                if ((i == 2 || i == 3) && temp == blockArray[i - 1][j] && temp == blockArray[i - 2][j]
                        && (!changedBlocks[i][j] && (changedBlocks[i - 1][j] || changedBlocks[i - 2][j]))) {
                    removeBlock(i, j);
                    ret = true;
                }
                changedBlocks[i][j] = false;
            }
        }
        return ret;
    }

    private void removeBlock(int i, int j) {

        int temp = blockArray[i][j];

        if (i - 1 >= 0 && temp == blockArray[i - 1][j]){
            blockArray[i][j] = 0;
            removeBlock(i - 1, j);
        }

        if (i + 1 < 4 && temp == blockArray[i + 1][j]){
            blockArray[i][j] = 0;
            removeBlock(i + 1, j);
        }

        if (j - 1 >= 0 && temp == blockArray[i][j - 1]){
            blockArray[i][j] = 0;
            removeBlock(i, j - 1);
        }

        if (j + 1 < 12 && temp == blockArray[i][j + 1]){
            blockArray[i][j] = 0;
            removeBlock(i, j + 1);
        }
        score++;
        blockArray[i][j] = 0;
    }

    private boolean gameOver() {
        boolean isOver = false;
        if (blockArray[0][12] != 0 || blockArray[1][12] != 0 || blockArray[2][12] != 0 || blockArray[3][12] != 0)
            isOver = true;
        return isOver;
    }

}
