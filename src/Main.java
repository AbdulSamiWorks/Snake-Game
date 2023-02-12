import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame jFrame=new JFrame("Snake Game");
        jFrame.setBounds(200,20,905,700);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           GamePannel pannel=new GamePannel();
           pannel.setBackground(Color.DARK_GRAY);
           jFrame.add(pannel);
        jFrame.setVisible(true);
    }
}
