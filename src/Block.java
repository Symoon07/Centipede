import java.awt.*;

public class Block extends Canvas
{
    private int x, y, w, h;

    public Block(int ex, int wy, int wd, int ht)
    {
        x = ex;
        y = wy;
        w = wd;
        h = ht;
    }

    public int getX( ){ return x; }
    public void setX( int ex ){ x = ex; }
    public int getY( ){ return y; }
    public void setY( int wy ){ y = wy; }
    public int getW(){ return w; }
    public int getH(){ return h; }

    public boolean intersects( Block o ) {
        Rectangle cur = new Rectangle(this.x, this.y, this.w, this.h);
        Rectangle other = new Rectangle(o.x, o.y, o.w, o.h);
        return cur.intersects(other);
    }
}