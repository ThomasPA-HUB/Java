import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
    int m_boardWith = 750;
    int m_boardHeight = 250;

    JFrame frame = new JFrame("Chrome Dinosaur");
    frame.setVisible(true);
    frame.setSize(m_boardWith, m_boardHeight);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    ChromeDinosaur ChromeDinosaur = new ChromeDinosaur();
    frame.add(ChromeDinosaur);
    frame.pack();
    ChromeDinosaur.requestFocus(true);
    frame.setVisible(true);
    }
}
