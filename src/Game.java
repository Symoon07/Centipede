import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;

class Game extends JPanel implements Runnable, KeyListener {
    private boolean[] keys;
    ArrayList<Mushroom> shrooms;
    ArrayList<Centipede> centipedes;
    Player player;
    ArrayList<Bullet> bullet;
    int lives;
    int counter;
    int score;
    int cspeed;
    int[] cspeeds;
    int speedIndex;

    public void newGame() {
        setBackground(Color.WHITE);
        keys = new boolean[6];
        shrooms = new ArrayList<>();
        centipedes = new ArrayList<>();
        player = new Player(480, 960, 40, 40, 10);
        bullet = new ArrayList<>();
        counter= 0;
        score= 0;
        lives = 3;
        speedIndex = 0;
        cspeeds = new int[] {4, 5, 8, 10, 20, 40};
        cspeed = cspeeds[speedIndex];
        int max = 5;
        for (int i = 1; i <= 20; i++) {
            HashSet<Integer> pos = new HashSet<>();
            while (pos.size() != max) {
                pos.add((int) (Math.random() * 25));
            }
            for (int j : pos) {
                shrooms.add(new Mushroom(j * 40 + 5, i * 40 + 5, 30, 30, false));
            }
        }
        centipedes.add(new Centipede(8, -280, 0, cspeed));
    }

    public Game() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        newGame();
        AudioInputStream ai = AudioSystem.getAudioInputStream(new File("assets/Driftveil City.wav"));
        Clip clip = AudioSystem.getClip();
        clip.open(ai);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
        addKeyListener( this );   	//
        setFocusable( true );		// Do NOT DELETE these three lines
        new Thread(this).start();	//
    }

    public void paint( Graphics window ) {
        window.setColor(Color.BLACK); window.fillRect( 0,0, 1000, 1000);
        if (lives == 0) {
            newGame();
        }
        if (speedIndex > 0) {
            keys[5] = false;
            window.setColor(Color.BLACK);
            window.fillRect( 0,0, 1000, 1000);
            Font Font1 = new Font("Courier New", Font.BOLD, 64);
            window.setFont(Font1);
            window.drawString("YOU WIN!!!", 450, 500);
            Font1 = new Font("Courier New", Font.BOLD, 25);
            window.setFont(Font1);
            window.drawString("Press G to play again ", 390, 25);
        }
        if (centipedes.isEmpty()) {
            if (score - (score % 1000) == 0) {
                cspeed = cspeeds[++speedIndex];
            }
            centipedes.add(new Centipede(8, -280, 0, cspeed));
            counter = 0;
        }
        player.paint(window);
        for (Centipede c : centipedes) {
            c.paint(window);
        }
        for (Mushroom m : shrooms) {
            m.paint(window);
        }
        window.setColor(Color.WHITE);
        Font Font1 = new Font("Courier New", Font.BOLD, 36);
        window.setFont(Font1);
        window.drawString("Score: "+score, 10, 990);
        window.setColor(Color.RED);
        window.drawString("Lives: "+lives, 820, 990);
        if(!keys[5]){
            Font1 = new Font("Courier New", Font.BOLD, 25);
            window.setFont(Font1);
            window.drawString("Press G to start ", 390, 25);
        }
        else {
            if (keys[0]) {
                if (bullet.isEmpty()) {
                    bullet.add(new Bullet(player.getX() + 18, player.getY() - 40, 4, 40, 30));
                }
            }
            if (keys[1]) {
                player.goLeft();
            }
            if (keys[2]) {
                player.goRight();
            }
            if (keys[3]) {
                player.goUp();
            }
            if (keys[4]) {
                player.goDown();
            }
            if (!centipedes.isEmpty()) {
                for (Centipede c : centipedes) {
                    if (c.head.getY() + c.head.getH() > 1000) {
                        lives--;
                        centipedes.clear();
                        centipedes.add(new Centipede(8, -280, 0, cspeed));
                        counter = 0;
                    }
                }
                for (Centipede c : centipedes) {
                    if (c.poisoned) {
                        c.xvelocity = 0;
                        c.yvelocity = cspeed;
                    }
                    else {
                        for (Mushroom m : shrooms) {
                            if (m.poison) {
                                c.poisoned = true;
                                break;
                            }
                            if (c.yvelocity == 0 && c.intersects(m) == 1) {
                                c.yvelocity = cspeed;
                                c.xvelocity = 0;
                                c.direction = 1;
                                break;
                            } else if (c.yvelocity == 0 && c.intersects(m) == -1) {
                                c.yvelocity = cspeed;
                                c.xvelocity = 0;
                                c.direction = -1;
                                break;
                            }
                        }
                        if (!c.poisoned) {
                            if (c.yvelocity != 0 && c.head.getY() % 40 == 0) {
                                c.head.setY(c.head.getY() + c.yvelocity);
                                c.yvelocity = 0;
                                c.xvelocity = c.direction == 1 ? cspeed : -cspeed;
                            } else if (c.head.getX() + c.head.getW() > 998) {
                                c.yvelocity = cspeed;
                                c.xvelocity = 0;
                                c.direction = -1;
                            } else if (c.head.getX() < 0) {
                                c.yvelocity = cspeed;
                                c.xvelocity = 0;
                                c.direction = 1;
                            }
                        }
                    }
                    for (Block b : c.body) {
                        if (b.intersects(player)) {
                            lives--;
                            centipedes.clear();
                            centipedes.add(new Centipede(8, -280, 0, cspeed));
                            counter = 0;
                        }
                    }
                }
                for (Centipede c : centipedes) {
                    if (counter % (40 / cspeed) == 0) {
                        c.copyX = new ArrayList<>();
                        c.copyY = new ArrayList<>();
                        for (Block a : c.body) {
                            c.copyX.add(a.getX());
                            c.copyY.add(a.getY());
                        }
                        counter = 0;
                    }
                    c.head.setX(c.head.getX() + c.xvelocity);
                    c.head.setY(c.head.getY() + c.yvelocity);
                    for (int j = 1; j < c.body.size(); j++) {
                        if (c.copyX.get(j - 1) > c.body.get(j).getX()) {
                            c.body.get(j).setX(c.body.get(j).getX() + cspeed);
                        } else if (c.copyX.get(j - 1) < c.body.get(j).getX()) {
                            c.body.get(j).setX(c.body.get(j).getX() - cspeed);
                        }
                        if (c.copyY.get(j - 1) > c.body.get(j).getY()) {
                            c.body.get(j).setY(c.body.get(j).getY() + cspeed);
                        }
                    }
                }
            }
            if (!bullet.isEmpty()) {
                bullet.get(0).paint(window);
                bullet.get(0).setY(bullet.get(0).getY() - bullet.get(0).getVelocity());
                if (bullet.get(0).getY() < 0) {
                    bullet.remove(0);
                }
            }
            for (int i = 0; i < shrooms.size(); i++) {
                if (!bullet.isEmpty() && shrooms.get(i).intersects(bullet.get(0))) {
                    shrooms.get(i).health--;
                    if (shrooms.get(i).health == 0) {
                        shrooms.remove(i);
                        score++;
                    }
                    bullet.remove(0);
                    break;
                }
            }
            for (int i = 0; i < centipedes.size(); i++) {
                for (int j = 0; j < centipedes.get(i).body.size(); j++) {
                    if (!bullet.isEmpty() && centipedes.get(i).body.get(j).intersects(bullet.get(0))) {
                        bullet.remove(0);
                        if (j != centipedes.get(i).body.size() - 1 && j != 0) {
                            shrooms.add(new Mushroom(centipedes.get(i).body.get(j).getX(), centipedes.get(i).body.get(j).getY(), 30, 30, false));
                            centipedes.add(new Centipede(centipedes.get(i).body.subList(0, j), centipedes.get(i).direction, centipedes.get(i).xvelocity, centipedes.get(i).yvelocity, centipedes.get(i).copyX, centipedes.get(i).copyY));
                            centipedes.add(new Centipede(centipedes.get(i).body.subList(j + 1, centipedes.get(i).body.size()), centipedes.get(i).direction, centipedes.get(i).xvelocity, centipedes.get(i).yvelocity, centipedes.get(i).copyX, centipedes.get(i).copyY));
                            centipedes.remove(i);
                            score += 10;
                        } else {
                            if (centipedes.get(i).body.size() == 1) {
                                centipedes.remove(i);
                                score += 100;
                            }
                            else {
                                shrooms.add(new Mushroom(centipedes.get(i).body.get(j).getX(), centipedes.get(i).body.get(j).getY(), 30, 30, false));
                                centipedes.get(i).body.remove(j);
                                centipedes.get(i).head = centipedes.get(i).body.get(0);
                                if(j == 0){
                                    score += 100;
                                }
                            }
                        }
                        break;
                    }
                }
            }
            counter++;
        }
    }

    public void keyPressed(KeyEvent e) {
        //Java KeyEvent docs
        //https://docs.oracle.com/javase/8/docs/api/java/awt/event/KeyEvent.html

        if( e.getKeyCode()  == KeyEvent.VK_SPACE ) {
            keys[0] = true;
        }
        if( e.getKeyCode()  == KeyEvent.VK_LEFT || e.getKeyCode()  == KeyEvent.VK_A) {
            keys[1] = true;
        }
        if( e.getKeyCode()  == KeyEvent.VK_RIGHT || e.getKeyCode()  == KeyEvent.VK_D) {
            keys[2] = true;
        }
        if ( e.getKeyCode()  == KeyEvent.VK_UP || e.getKeyCode()  == KeyEvent.VK_W) {
            keys[3] = true;
        }
        if ( e.getKeyCode()  == KeyEvent.VK_DOWN || e.getKeyCode()  == KeyEvent.VK_S) {
            keys[4] = true;
        }
        if( e.getKeyCode()  == KeyEvent.VK_G ) {
            keys[5]=true;
        }
    }

    public void keyUnpressed(KeyEvent e) {
        if( e.getKeyCode()  == KeyEvent.VK_SPACE ) {
            keys[0] = false;
        }
        if( e.getKeyCode()  == KeyEvent.VK_LEFT || e.getKeyCode()  == KeyEvent.VK_A) {
            keys[1] = false;
        }
        if( e.getKeyCode()  == KeyEvent.VK_RIGHT || e.getKeyCode()  == KeyEvent.VK_D) {
            keys[2] = false;
        }
        if ( e.getKeyCode()  == KeyEvent.VK_UP || e.getKeyCode()  == KeyEvent.VK_W) {
            keys[3] = false;
        }
        if ( e.getKeyCode()  == KeyEvent.VK_DOWN || e.getKeyCode()  == KeyEvent.VK_S) {
            keys[4] = false;
        }
    }

    public void keyTyped(KeyEvent e) { keyPressed( e ); }
    public void keyReleased(KeyEvent e) { keyUnpressed( e ); }

    public void run() {
        try {
            while( true ) {
                Thread.sleep( 10 );
                repaint();
            }
        }
        catch( Exception ignored ) {
        }
    }
}