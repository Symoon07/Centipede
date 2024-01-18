import java.awt.*;

class Player extends Block {
    int velocity;
    boolean fired;

    public Player( int ex, int wy, int wd, int ht, int v) {
        super(ex, wy, wd, ht);
        velocity = v;
        fired = false;

    }

    public void goLeft() {
        setX( getX() - velocity );
        if (getX() < 0) {
            setX(0);
        }
    }

    public void goRight() {
        setX(getX() + velocity);
        if (getX() + getW() > 1000) {
            setX(1000 - getW());
        }
    }

    public void goUp() {
        setY(getY() - velocity);
        if (getY() < 840) {
            setY(840);
        }
    }

    public void goDown() {
        setY(getY() + velocity);
        if (getY() + getH() > 1000) {
            setY(1000 - getH());
        }
    }

    @Override
    public void paint( Graphics window ) {
        Graphics2D g2 = (Graphics2D) window;
        Image img1 = Toolkit.getDefaultToolkit().getImage("assets/player.png");
        g2.drawImage(img1, getX(), getY(), getW(), getH(), this);
    }
}