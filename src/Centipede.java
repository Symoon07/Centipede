import java.awt.*;
import java.util.*;
import java.util.List;

class Centipede extends Canvas {
    ArrayList<Block> body;
    Block head;
    int xvelocity;
    int yvelocity;
    int direction;
    ArrayList<Integer> copyX;
    ArrayList<Integer> copyY;
    boolean poisoned;
    public Centipede(int len, int x, int y, int speed) {
        direction = 1;
        body = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            body.add(new Block(x + i * 40, y, 40, 40));
        }
        Collections.reverse(body);
        head = body.get(0);
        xvelocity = speed;
        yvelocity = 0;
    }

    public Centipede (List<Block> c, int dir, int vx, int vy, ArrayList<Integer> cx, ArrayList<Integer> cy) {
        direction = dir;
        body = new ArrayList<>();
        for (Block b : c) {
            body.add(new Block(b.getX(), b.getY(), 40, 40));
        }
        head = body.get(0);
        xvelocity = vx;
        yvelocity = vy;
        copyX = cx;
        copyY = cy;
    }

    public void setPoisoned(boolean p) {
        poisoned = p;
    }

    public int intersects(Block o) {
        Rectangle h = new Rectangle(this.head.getX(), this.head.getY(), this.head.getW(), this.head.getH());
        Rectangle left = new Rectangle(o.getX(), o.getY(), 1, o.getH());
        Rectangle right = new Rectangle(o.getX() + o.getW(), o.getY(), 1, o.getH());
        if (h.intersects(right)) {
            return 1;
        }
        if (h.intersects(left)) {
            return -1;
        }

        return 0;
    }

    @Override
    public void paint(Graphics window) {
        for (int i = 0; i < body.size(); i++) {
            Image img1;
            if (i == 0) {
                if(yvelocity != 0){
                    img1 = Toolkit.getDefaultToolkit().getImage("assets/centipede_head_down.png");
                }
                else {
                    img1 = Toolkit.getDefaultToolkit().getImage("assets/centipede_head.png");
                }
            } else {
                img1 = Toolkit.getDefaultToolkit().getImage("assets/centipede_body.png");
            }
            window.drawImage(img1, direction == 1 ? body.get(i).getX() + 40 : body.get(i).getX(), body.get(i).getY(), direction == 1 ? -body.get(i).getW() : body.get(i).getW(), body.get(i).getH(), this);
        }
    }
}