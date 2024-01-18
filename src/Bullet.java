import java.awt.*;

public class Bullet extends Block {
    int velocity;

    public Bullet(int ex, int wy, int wd, int ht, int v) {
        super(ex, wy, wd, ht);
        velocity = v;
    }

    public int getVelocity() {
        return velocity;
    }

    @Override
    public void paint(Graphics window) {
        window.setColor(Color.RED);
        window.fillRect(getX(), getY(), getW(), getH());
    }
}
