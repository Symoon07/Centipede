import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;

class CentipedeRunner extends JFrame
{
    private static final int WIDTH = 2560;
    private static final int HEIGHT = 1440;

    public CentipedeRunner() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        super("Centipede");

        setSize(WIDTH,HEIGHT);

        //This line loads the BreakOut game
        getContentPane().add( new Game() );

        setVisible(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        new CentipedeRunner();
    }
}