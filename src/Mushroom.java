import java.awt.*;

public class Mushroom extends Block {
    int health;
    boolean poison;

    public Mushroom(int ex, int wy, int wd, int ht, boolean p) {
        super(ex, wy, wd, ht);
        poison = p;
        health = 4;
    }

    @Override
    public void paint( Graphics window ) {
        Image img1;
        switch (health) {
            case 3 -> img1 = Toolkit.getDefaultToolkit().getImage("assets/mushroom3.png");
            case 2 -> img1 = Toolkit.getDefaultToolkit().getImage("assets/mushroom2.png");
            case 1 -> img1 = Toolkit.getDefaultToolkit().getImage("assets/mushroom1.png");
            default -> img1 = Toolkit.getDefaultToolkit().getImage("assets/mushroom4.png");
        }
        window.drawImage(img1, getX(), getY(), getW(), getH(), this);
    }
}
