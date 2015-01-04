package allen.client;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Allen on 14/12/21.
 */
public class ScorePanel extends JLabel{

    public ScorePanel(int x, int y) {
        this.setSize(60, 160);
        this.setVisible(true);
        this.setBackground(Color.orange);
        this.setFocusable(false);
        this.setForeground(Color.white);
        this.setFont(new Font("Dialog", 1, 70));
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setUI(new VerticalLabelUI());
        this.setLocation(x, y);
    }

}
