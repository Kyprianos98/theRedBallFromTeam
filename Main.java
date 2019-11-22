import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame implements ActionListener {

    public GamePanel game;
    public Timer gameTimer;

    //COMMENT
    // Game setup constants
    public static final int WIDTH  = 1500;
    public static final int HEIGHT = 800;
    public static final int BASE = 800;
    private static final int DELAY = 12;

    public Main () {
        super("Red Ball");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);

        // Game timer
        gameTimer = new Timer(DELAY, this);
        gameTimer.start();

        // Add Panel to Frame
        game = new GamePanel();
        add(game);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public void actionPerformed (ActionEvent e) {
        if (game != null && game.ready) {
            game.repaint();
        }
    }

    public static void main(String[] args){
        Main game = new Main();
    }

}