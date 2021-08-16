package galaga;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// author -Daniel Cho

// www.github.com/okelykodely

public class Galaga implements KeyListener {
    
    Clip clip;
    AudioInputStream audioInputStream;
    int icount = 0;
    GameFunctor gf = new GameFunctor();
    JFrame j = new JFrame();
    JPanel p = new JPanel();
    Graphics g = null;
    ArrayList<Enemy> enemies = new ArrayList<>();
    PlayerOne a = new PlayerOne();
    Lasers lasers = new Lasers();
    Boss boss1 = new Boss(1);
    Boss boss2 = new Boss(2);
    Boss boss3 = new Boss(3);
    Boss boss4 = new Boss(4);
    int points = 0;

    boolean move_stars = false;
    int game_width= 1300;
    int game_height = 900;
    int frame_width = 1200;
    int frame_height = 800;
    JPanel rightSidePanel = new JPanel();
    int right_side_panel_width = 100;
    int right_side_panel_height = 800;
    JPanel bottomSidePanel = new JPanel();
    int bot_side_panel_width = 1300;
    int bot_side_panel_height = 400;
    
    Rocks rocks = new Rocks();
    
    Capsules capsules = new Capsules();
    
    JButton oneBtn = new JButton("1");
    JButton twoBtn = new JButton("2");
    JButton threeBtn = new JButton("3");
    
    class Rays {
        Image image = null;
        ArrayList<Ray> rs = new ArrayList<>();
        Rays() {
            try {
                image = ImageIO.read(this.getClass().getResource("ray.png"));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        public void addRay(int x, int y) {
            Ray r = new Ray();
            r.x = x;
            r.y = y;
            rs.add(r);
        }
        public void moveRaysAlong() {
            for(int i=0; i<rs.size(); i++) {
                Ray r = null;
                try {
                    r = new Ray();
                    r.x = rs.get(i).x;
                    r.y = rs.get(i).y + 10;
                    rs.get(i).y = r.y;
                    g.drawImage(image, r.x, r.y, 140, 118, null);
                } catch(Exception e) {}
            }
        }
    }
    
    class Ray {
        int x, y;
    }
    
    class Capsules {
        ArrayList<Capsule> caps = new ArrayList<>();
        public void addSpecifiedCapsuleAtLocation(int x, int y, Color col) {
            Capsule c = new Capsule(col);
            c.x = x;
            c.y = y;
            caps.add(c);
        }
        public void addCapsuleAtLocation(int x, int y) {
            Random r = new Random();
            int v = r.nextInt(3);
            Color col = null;
            if(v == 0)
                col = Color.RED;
            else if(v == 1)
                col = Color.CYAN;
            else if(v == 2)
                col = Color.GREEN;
            Capsule c = new Capsule(col);
            c.x = x;
            c.y = y;
            caps.add(c);
        }
        public void addRandomCapsule() {
            Random r = new Random();
            int v = r.nextInt(3);
            Color col = null;
            if(v == 0)
                col = Color.RED;
            else if(v == 1)
                col = Color.CYAN;
            else if(v == 2)
                col = Color.GREEN;
            Capsule c = new Capsule(col);
            c.x = r.nextInt(1000) + 40;
            c.y = r.nextInt(400) + 140;
            caps.add(c);
        }
        public void drawAll() {
            for(int i=0; i<caps.size(); i++) {
                if(caps.get(i).lives > 0) {
                    caps.get(i).draw();
                }
                caps.get(i).lives--;
                if(caps.get(i).lives == 0) {
                    caps.remove(caps.get(i));
                }
            }
        }
        public void checkIfAnyEaten() {
            for(int i=0; i<caps.size(); i++) {
                if(caps.get(i).isCosumned()) {
                    caps.remove(caps.get(i));
                }
            }
        }
    }
    
    class Capsule {
        int x, y;
        int width;
        int height;
        Color color;
        String kind;
        int lives = 200;
        Capsule(Color color) {
            width = 30;
            height = 34;
            this.color = color;
            if(this.color == Color.CYAN)
                kind = "triple spread";
            else if(this.color == Color.RED)
                kind = "regular";
            else
                kind = "all spread";
        }
        public void draw() {
            g.setColor(this.color);
            g.drawOval(x, y, width, height);
            g.setColor(Color. white /*E*/  );
            if(this.color == Color.CYAN)
                g.drawString("C", x, y);
            else if(this.color == Color.RED)
                g.drawString("R", x, y);
            else
                g.drawString("G", x, y);
        }
        public String kind() {
            return kind;
        }
        public boolean isCosumned() {
            if(a.x >= x && a.x <= x + width &&
                    a.y >= y && a.y <= y + height) {
                a.capsule = this;
                x = -100;
                y = -100;
                return true;
            }
            return false;
        }
    }
    
    class Rocks {
        ArrayList<Rock> rs = new ArrayList<>();
        long countDown = 200 ;
        int cnt = 0;
        final static int INIT_SIZE = 95;
        Rocks() {
            for(int i=0; i<INIT_SIZE; i++) {
                Rock rock = new Rock();
                Random r = new Random();
                rock.width = r.nextInt(36) + r.nextInt(36);
                rock.height = r.nextInt(36) + r.nextInt(50);
                rock.x = r.nextInt(1200);
                rock.y = -r.nextInt(40);
                rs.add(rock);
            }
        }
        public boolean moveDownAlong() {
            if(countDown == 0) {
                return false;
            }
            if(cnt == 2) {
                {Rock rock = new Rock();
                Random r = new Random();
                rock.width = r.nextInt(36) + r.nextInt(36);
                rock.height = r.nextInt(36) + r.nextInt(50);
                rock.x = r.nextInt(1200);
                rock.y = -r.nextInt(40);
                rs.add(rock);}
                {Rock rock = new Rock();
                Random r = new Random();
                rock.width = r.nextInt(36) + r.nextInt(36);
                rock.height = r.nextInt(36) + r.nextInt(50);
                rock.x = r.nextInt(1200);
                rock.y = -r.nextInt(40);
                rs.add(rock);}
                {Rock rock = new Rock();
                Random r = new Random();
                rock.width = r.nextInt(36) + r.nextInt(36);
                rock.height = r.nextInt(36) + r.nextInt(50);
                rock.x = r.nextInt(1200);
                rock.y = -r.nextInt(40);
                rs.add(rock);}
                {Rock rock = new Rock();
                Random r = new Random();
                rock.width = r.nextInt(36) + r.nextInt(36);
                rock.height = r.nextInt(36) + r.nextInt(50);
                rock.x = r.nextInt(1200);
                rock.y = -r.nextInt(40);
                rs.add(rock);}
                {Rock rock = new Rock();
                Random r = new Random();
                rock.width = r.nextInt(36) + r.nextInt(36);
                rock.height = r.nextInt(36) + r.nextInt(50);
                rock.x = r.nextInt(1200);
                rock.y = -r.nextInt(40);
                rs.add(rock);}
                cnt = -1;
            }
            cnt++;
            countDown--;
            for(int i=0; i<rs.size(); i++) {
                Random r = new Random();
                if(!rs.get(i).brokedOne) {
                    
                    rs.get(i).y += r.nextInt(24) + 8;
                } else {
                    rs.get(i).x += -10 - r.nextInt(74);
                    rs.get(i).y += 140 + r.nextInt(64);
                }
                if(rs.get(i).isCrashed()) {
                    rs.remove(rs.get(i));
                } else if(rs.get(i).isShotCrashed()) {
                    int x1 = rs.get(i).x;
                    int y1 = rs.get(i).y;
                    rs.remove(rs.get(i));
                    {Rock rock = new Rock();
                    rock.width = r.nextInt(36) + r.nextInt(36);
                    rock.height = r.nextInt(36) + r.nextInt(50);
                    rock.x = x1 - 40;
                    rock.y = y1;
                    rock.brokedOne = true;
                    rs.add(rock);}
                    {Rock rock = new Rock();
                    rock.width = r.nextInt(36) + r.nextInt(36);
                    rock.height = r.nextInt(36) + r.nextInt(50);
                    rock.x = x1 + 40;
                    rock.y = y1;
                    rock.brokedOne = true;
                    rs.add(rock);}
                }
            }
            this.drawAll();
            return true;
        }
        private void drawAll() {
            for(int i=0; i<rs.size(); i++) {
                Random rrrr = new Random();
                int rrr = 40+rrrr.nextInt(140);
                int r0 = rrrr.nextInt(80);
                g.setColor(new Color(210, rrr, r0));
                g.fillOval(rs.get(i).x, rs.get(i).y, rs.get(i).width, rs.get(i).height);
                g.setColor(Color.lightGray);
                g.drawOval(rs.get(i).x+5, rs.get(i).y+5, 6, 6);
                g.drawOval(rs.get(i).x+15, rs.get(i).y+15, 4, 4);
            }
        }
    }
    
    class Rock {
        int width = 0;
        int height = 0;
        int x, y;
        boolean brokedOne = false;
        public boolean isCrashed() {
            if(!brokedOne) {
                if(a.x >= x && a.x <= x + width &&
                        a.y >= y && a.y <= y + height) {
                    a.lives--;
                    return true;
                }
            }
            return false;
        }
        public boolean isShotCrashed() {
            try {
                for(int i=0; i<lasers.lasers.size(); i++) {
                    if(lasers.lasers.get(i).x >= x && lasers.lasers.get(i).x <= x + width &&
                            lasers.lasers.get(i).y >= y && lasers.lasers.get(i).y <= y + height) {
                        try{
                            AudioInputStream audioInputStream =
                                AudioSystem.getAudioInputStream(
                                    this.getClass().getResource("explosion.wav"));
                            Clip clip = AudioSystem.getClip();
                            clip.open(audioInputStream);
                            clip.start();
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                        points += 33;
                        Random r = new Random();
                        int w = r.nextInt(12);
                        if(w == 0) {
                            capsules.addCapsuleAtLocation(x, y);
                        }
                        return true;
                    }
                }
            } catch(Exception e) {}
            return false;
        }
    }

    class Function {
        int id;
        String name;
        Runnable runnable;
        Function(int id, String name, Runnable runnable) {
            this.id = id;
            this.name = name;
            this.runnable = runnable;
        }
    }    
    
    class GameFunctor {
        Vector<Function> functions = new Vector<>();
        public void addFunction(int id, String name, Runnable runnable) {
            this.functions.add(new Function(id, name, runnable));
        }
        public void startLoop() {
            for(Iterator<Function> it = functions.iterator(); it.hasNext();) {
                Function func = it.next();
                func.runnable.run();
            }
        }
    }
    
    class Boss {
        int x, y;
        int lives = 20;
        URL url = null;
        int level = 1;
        Rays rs = new Rays();
        Boss(int level) {
            this.level = level;
            if(this.level == 1) {
                try {
                    url = this.getClass().getResource("boss1.gif");
                } catch(Exception e) {}
            } else if(this.level == 2) {
                try {
                    url = this.getClass().getResource("boss2.gif");
                } catch(Exception e) {}
            } else if(this.level == 3) {
                try {
                    url = this.getClass().getResource("boss3.gif");
                } catch(Exception e) {e.printStackTrace();}
            } else if(this.level == 4) {
                try {
                    url = this.getClass().getResource("boss4.gif");
                } catch(Exception e) {e.printStackTrace();}
            }
        }
        public boolean isAlive() {
            if(lives <= 0)
                return false;
            else
                return true;
        }
        public boolean isDowned() {
            for(int i=0; i<lasers.lasers.size(); i++) {
                if(lasers.lasers.get(i).x >= x && lasers.lasers.get(i).x <= x + 200 &&
                        lasers.lasers.get(i).y >= y && lasers.lasers.get(i).y <= y + 200) {
                    lives--;
                    return false;
                }
            }
            return true;
        }
        public void draw() {
            try {
                ImageIcon icon = new ImageIcon(url);
                g.drawImage(icon.getImage(), x, y, null);
            } catch(Exception e) {e.printStackTrace();}
            Random r = new Random();
            int v = r.nextInt(60);
            if(v == 0) {
                this.rs.addRay(x, y+50);
            }
        }
    }
    
    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        switch(ke.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                try{
                    AudioInputStream audioInputStream =
                        AudioSystem.getAudioInputStream(
                            this.getClass().getResource("lasershot.wav"));
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                lasers.addLaser(a.x, a.y, a.capsule);
                break;
            case KeyEvent.VK_UP:
                if(a.y - 8 >= 0)
                    a.y -= 8;
                break;
            case KeyEvent.VK_DOWN:
                if(a.y + 8 <= 800-30)
                    a.y += 8;
                break;
            case KeyEvent.VK_LEFT:
                if(a.x - 8 >= 0)
                    a.x -= 8;
                break;
            case KeyEvent.VK_RIGHT:
                if(a.x + 8 <= 1200-30)
                    a.x += 8;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    class Lasers {
        ArrayList<Point2> lasers = new ArrayList<>();
        public void addLaser(int x, int y, Capsule userCapsule) {
            if(userCapsule == null) {
                userCapsule = new Capsule(Color.RED);
                userCapsule.kind = "regular";
            }
            if(userCapsule.kind.equals("regular")) {
                Point2 pt = new Point2();
                pt.x = x;
                pt.y = y;
                pt.kind = userCapsule.kind;
                pt.isRound =  false;
                lasers.add(pt);
            } else if(userCapsule.kind.equals("triple spread")) {
                for(int i=0; i<3; i++) {
                    Point2 pt = new Point2();
                    pt.x = x;
                    pt.y = y;
                    pt.kind = userCapsule.kind;
                    pt.isRound =  false;
                    pt.place = i+1;
                    lasers.add(pt);
                }
            } else if(userCapsule.kind.equals("all spread")) {
                for(int i=0; i<13; i++) {
                    Point2 pt = new Point2();
                    pt.x = x;
                    pt.y = y;
                    pt.kind = userCapsule.kind;
                    pt.isRound =  false;
                    pt.place = i+1;
                    lasers.add(pt);
                }
            }
        }
        public void moveLasersAlong(boolean UP) {
            for(int i=0; i<lasers.size(); i++) {
                g.setColor(Color.RED);
                if(UP)
                    g.setColor(Color.BLUE);
                Point2 pt = null;
                try {
                    pt = new Point2();
                    pt.x = lasers.get(i).x;
                    pt.y = lasers.get(i).y;
                    pt.isRound = lasers.get(i).isRound;
                    pt.kind = lasers.get(i).kind;
                    pt.place = lasers.get(i).place;
                    if(pt.kind.equals("regular")) {
                        g.fillRect(pt.x, pt.y, 5, 30 )  ;
                    } else {
                        g.fillOval(pt.x, pt.y, 14, 18)  ;
                    }
                    if(UP) {
                        lasers.get(i).y -= 30;
                        if(!pt.kind.contains("regular")) {
                            if(pt.place < 7) {
                                pt.x = pt.x - (7-pt.place);
                            }
                            else if(pt.place == 7) {
                                pt.x = pt.x;
                            }
                            else if(pt.place > 7) {
                                pt.x = pt.x + pt.place;
                            }
                        }
                        lasers.get(i).x = pt.x;
                    }
                    else
                        lasers.get(i).y += 10;
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    class Point {
        int x, y;
    }

    class Point2 {
        int x, y;
        boolean isRound  = false;
        String kind = "regular";
        int place = 1;
    }
   
    class PlayerOne {
        int lives = 100;
        int x, y;
        Image image = null;
        Capsule capsule = null;
        PlayerOne() {
            try {
                image = ImageIO.read(getClass().getResource("playerOne.png"));
            } catch(Exception e) {}
        }
        public void draw() {
            try {
                g.drawImage(image, x, y, 40, 50, null);
            } catch(Exception e) {}
        }
    }
    
    class Actions {
        ArrayList<Action> actions = new ArrayList<>();
    }
    
    class Action {
        int movex; int movey;
    }
    
    class Enemy {
        Bombs bombs = new Bombs();
        Image image = null;
        int lives = 2;
        int actionCursor = 0;
        int actionCursorIn = 99999;
        int x, y;
        Lasers lasers1 = new Lasers();
        Set<Actions> actionsSet = new HashSet<>();
        Actions actions = new Actions();
        public void draw() {
            try {
                g.drawImage(image, x, y, 30, 30, null);
                Random r = new Random();
                int v = r.nextInt(100);
                if(v == 0) {
                    this.lasers1.addLaser(x, y+50, null); //!userCapsule
                }
            } catch(Exception e) {}
        }
        public boolean isAlive() {
            for(int i=0; i<lasers.lasers.size(); i++) {
                if(lasers.lasers.get(i).x >= x && lasers.lasers.get(i).x <= x + 30 &&
                        lasers.lasers.get(i).y >= y && lasers.lasers.get(i).y <= y + 30) {
                    try{
                        AudioInputStream audioInputStream =
                            AudioSystem.getAudioInputStream(
                                this.getClass().getResource("explosion.wav"));
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioInputStream);
                        clip.start();
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    Random r = new Random();
                    int w = r.nextInt(12);
                    if(w == 0) {
                        capsules.addCapsuleAtLocation(x, y);
                    }
                    return false;
                }
            }
            return true;
        }
        Enemy(int x, int y, String plan) {
            try {
                this.x = x;
                this.y = y;
                Random r = new Random();
                if(plan.equals("D") || plan.equals("A")) {
                    int v = r.nextInt(4);
                    if(v == 0) {image = ImageIO.read(getClass().getResource("a-enemy.png"));}
                    if(v == 1) {image = ImageIO.read(getClass().getResource("b-enemy.png"));}
                    if(v == 2) {image = ImageIO.read(getClass().getResource("c-enemy.png"));}
                    if(v == 3) {image = ImageIO.read(getClass().getResource("d-enemy.png"));}
                } else if(plan.equals("B")) {
                    int v = r.nextInt(2);
                    if(v == 0) image = ImageIO.read(getClass().getResource("e-enemy.png"));
                    if(v == 1) image = ImageIO.read(getClass().getResource("f-enemy.png"));
                }
                init(plan);
            } catch(Exception e) {}
        }
        private void init(String plan) {
            if(plan.equals("A")) {
                Action a = new Action();
                a.movex = -2; a.movey = 4;
                actions.actions.add(a);

                a = new Action();
                a.movex = 0; a.movey = 2;
                actions.actions.add(a);

                a = new Action();
                a.movex = 2; a.movey = 4;
                actions.actions.add(a);

                a = new Action();
                a.movex = 2; a.movey = -4;
                actions.actions.add(a);

                a = new Action();
                a.movex = 4; a.movey = -6;
                actions.actions.add(a);

                a = new Action();
                a.movex = -0; a.movey = -4;
                actions.actions.add(a);

                actionsSet.add(actions);
            } else if(plan.equals("B")) {
                Action a = new Action();
                a.movex = 2; a.movey = 4;
                actions.actions.add(a);

                a = new Action();
                a.movex = 0; a.movey = 2;
                actions.actions.add(a);

                a = new Action();
                a.movex = 12; a.movey = 4;
                actions.actions.add(a);

                a = new Action();
                a.movex = 6; a.movey = 6;
                actions.actions.add(a);

                a = new Action();
                a.movex = 10; a.movey = 2;
                actions.actions.add(a);

                a = new Action();
                a.movex = 5; a.movey = -5;
                actions.actions.add(a);

                a = new Action();
                a.movex = 2; a.movey = -4;
                actions.actions.add(a);

                a = new Action();
                a.movex = 0; a.movey = -4;
                actions.actions.add(a);

                a = new Action();
                a.movex = 0; a.movey = -4;
                actions.actions.add(a);

                a = new Action();
                a.movex = 0; a.movey = -4;
                actions.actions.add(a);

                a = new Action();
                a.movex = -5; a.movey = -2;
                actions.actions.add(a);
                
                a = new Action();
                a.movex = -5; a.movey = -2;
                actions.actions.add(a);

                a = new Action();
                a.movex = -5; a.movey = -2;
                actions.actions.add(a);

                a = new Action();
                a.movex = -5; a.movey = -2;
                actions.actions.add(a);

                a = new Action();
                a.movex = -5; a.movey = -2;
                actions.actions.add(a);

                a = new Action();
                a.movex = -5; a.movey = -2;
                actions.actions.add(a);

                a = new Action();
                a.movex = -5; a.movey = -2;
                actions.actions.add(a);

                if(y < 100) {
                    a = new Action();
                    a.movex = -4; a.movey = 6;
                    actions.actions.add(a);
                }
                
                actionsSet.add(actions);
            } else if(plan.equals("D")) {
                Action a = new Action();
                a.movex = -12; a.movey = 4;
                actions.actions.add(a);

                a = new Action();
                a.movex = 0; a.movey = 2;
                actions.actions.add(a);

                a = new Action();
                a.movex = 12; a.movey = 4;
                actions.actions.add(a);

                a = new Action();
                a.movex = 6; a.movey = 6;
                actions.actions.add(a);

                a = new Action();
                a.movex = 10; a.movey = -12;
                actions.actions.add(a);

                if(y < 100) {
                    a = new Action();
                    a.movex = -4; a.movey = 23;
                    actions.actions.add(a);
                }
                
                actionsSet.add(actions);
            }
        }
        private void planMove() {
            for(Iterator<Actions> j = actionsSet.iterator();j.hasNext();) {
                Actions actions = j.next();
                for(int ii = 0; ii<actions.actions.size(); ii++) {
                    ArrayList<Action> action = actions.actions;
                    for(int i=0;i<=actionCursorIn;i++) {
                        try {
                            Action z = action.get(i);
                            if(actionCursorIn == 99999) {
                                x += z.movex;
                                y += z.movey;
                                actionCursorIn = 1;
                                return;
                            }
                            else if(i==actionCursorIn) {
                                x += z.movex;
                                y += z.movey;
                                actionCursorIn++;
                                if(action.size() == actionCursorIn)
                                    actionCursorIn = 0;
                                return;
                            }
                        } catch(Exception e) {}
                    }
                }
            }
        }
    }
    
    private void activateEnemies() {
        for(int i=0; i<enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.planMove();
        }
    }
    
    Stars stars = new Stars();
    int vv = 7;
    
    class Stars {
        ArrayList<Point> stars = new ArrayList<>();
        int moveLeft = 1;
        int amountMoveStars = 0;
        int movedCount = 0;
        int movedCycle = 0;
        public void init() {
            for(int i=0; i<500; i++) {
                Random r = new Random();
                Point p = new Point();
                p.x = r.nextInt(1900);
                p.y = r.nextInt(800);
                stars.add(p);
            }
        }
        public void draw() {
            g.setColor(Color.WHITE);
            for(int i=0; i<stars.size(); i++) {
                stars.get(i).x += amountMoveStars;
                g.drawOval(stars.get(i).x, stars.get(i).y, 1, 1);
            }
            if(move_stars) {
                movedCount++;
                if(moveLeft == 1) {
                    amountMoveStars = -15;
                    if(movedCount == 1) {
                        moveLeft = 0;
                        movedCount = 0;
                    }
                } else if(moveLeft != 1) {
                    amountMoveStars = 15;
                    if(movedCount == 1) {
                        moveLeft = 1;
                        movedCount = 0;
                        movedCycle++;
                        if(movedCycle >= 2) {
                            move_stars = false;
                            amountMoveStars = 0;
                            movedCycle = 0;
                        }
                    }
                }
            } else {
                if(amountMoveStars == -15) {
                    for(int i=0; i<stars.size(); i++) {
                        stars.get(i).x += 15;
                        g.drawOval(stars.get(i).x, stars.get(i).y, 1, 1);
                    }
                }
            }
        }
    }
    
    private void initEnemies() {
        
        initCount++;
        
        String plan = "A";
        int v = 50;
        for(int i=0; i<4; i++) {
            Enemy enemy = new Enemy(300, vv*100-130, plan);
            enemy.x += v*i;
            enemies.add(enemy);
        }

        plan = "B";
        v = 50;
        for(int i=0; i<6; i++) {
            Enemy enemy = new Enemy(800, vv*100-130, plan);
            enemy.x += v*i;
            enemies.add(enemy);
        }
        
        plan = "D";
        v = 50;
        for(int i=0; i<13; i++) {
            Enemy enemy = new Enemy(100, vv*50-130, plan);
            enemy.x += v*i;
            enemies.add(enemy);
        }

        vv--;
        if(vv == 3)
            vv = 7;
    }
    
    private void initEnemies2() {
        
        initCount++;
        initCount++;
        initCount++;
        
        String plan = "D";
        int v = 80;
        for(int i=0; i<13; i++) {
            Enemy enemy = new Enemy(10, vv*10, plan);
            enemy.x += v*i;
            enemies.add(enemy);
        }
    }

    int totalBombs = 0;
    
    int initCount = 0;
    
    Image iii = null;
    int screen = 1;
    
    public Galaga() {
        j.setLayout(null);
        j.setBounds(0, 0, game_width, game_height);
        p.setBounds(0, 0, frame_width, frame_height);
        rightSidePanel.setLayout(null);
        rightSidePanel.setBounds(frame_width, 0, right_side_panel_width, right_side_panel_height);
        rightSidePanel.setBackground(Color.PINK);
        j.add(p);
        j.add(rightSidePanel);
        bottomSidePanel.setLayout(null);
        bottomSidePanel.setBounds(0, frame_height, bot_side_panel_width, bot_side_panel_height);
        j.add(bottomSidePanel);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        j.setVisible(true);
        j.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        j.addKeyListener(this);
        
        
        Graphics g1 = rightSidePanel.getGraphics();
        Thread t1 = new Thread() {
            public void run() {
                Color ColorGREEN = Color.GREEN;
                int y = 10;
                String G = "g";
                String A = "A";
                String L = "L";
                String A2 = "A";
                String G2 = "G";
                String A3 = "A";
                int pos = 1;
                while(true) {
                    try {
                        Thread.sleep(100);
                        g1.setColor(Color.PINK);
                        g1.fillRect(0, 0, 100, 800);
                        g1.setFont(new Font("Arial", Font.BOLD, 16));
                        y+=2;
                        y+=2;
                        y+=3;
                        if(pos == 1)
                            ColorGREEN = Color.RED;
                        else
                            ColorGREEN = Color.GREEN;
                        g1.setColor(ColorGREEN);
                        g1.drawString(G, 10, y);
                        if(pos == 2)
                            ColorGREEN = Color.RED;
                        else
                            ColorGREEN = Color.GREEN;
                        g1.setColor(ColorGREEN);
                        g1.drawString(A, 20, y);
                        if(pos == 3)
                            ColorGREEN = Color.RED;
                        else
                            ColorGREEN = Color.GREEN;
                        ColorGREEN = Color.GREEN;
                        g1.setColor(ColorGREEN);
                        g1.drawString(L, 30, y);
                        if(pos == 4)
                            ColorGREEN = Color.RED;
                        else
                            ColorGREEN = Color.GREEN;
                        g1.setColor(ColorGREEN);
                        g1.drawString(A2, 40, y);
                        if(pos == 5)
                            ColorGREEN = Color.RED;
                        else
                            ColorGREEN = Color.GREEN;
                        g1.setColor(ColorGREEN);
                        g1.drawString(G2, 50, y);
                        if(pos == 6)
                            ColorGREEN = Color.RED;
                        else
                            ColorGREEN = Color.GREEN;
                        g1.setColor(ColorGREEN);
                        g1.drawString(A3, 60, y);
                        ColorGREEN = Color.GREEN;
                        g1.setColor(Color.black);
                        g1.drawString("GALAGA", 10, 200+y);
                        g1.drawString("GALAGA", 10, 400+y);
                        if(y > 500)
                            y=10;
                        pos++;
                        if(pos == 7)
                            pos = 1;
                    } catch(Exception e) {}
                }
            }
        };
        t1.start();

        bottomSidePanel.setLayout(null);
        oneBtn.setBounds(400, 14, 200, 40);
        twoBtn.setBounds(650, 14, 200, 40);
        threeBtn.setBounds(900, 14, 200, 40);
        bottomSidePanel.add(oneBtn);
        j.requestFocus();
        oneBtn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                try{
                    clip.stop();
                    audioInputStream =
                        AudioSystem.getAudioInputStream(
                            this.getClass().getResource("galagath.wav"));
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    j.requestFocus();
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });
        bottomSidePanel.add(twoBtn);
        twoBtn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                try{
                    clip.stop();
                    audioInputStream =
                        AudioSystem.getAudioInputStream(
                            this.getClass().getResource("2..wav"));
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    j.requestFocus();
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });
        bottomSidePanel.add(threeBtn);
        threeBtn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                try{
                    clip.stop();
                    audioInputStream =
                        AudioSystem.getAudioInputStream(
                            this.getClass().getResource("thre.wav"));
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    j.requestFocus();
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });
        
        
        stars.init();
        
        g = p.getGraphics();

        Graphics g2 = bottomSidePanel.getGraphics();

        gf.addFunction(1, "sleepOrPause", new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(130);
                } catch(Exception e) {}
                try {
                    iii = ImageIO.read(getClass().getResource("galaga.png"));
                } catch(Exception e) {}
                try {
                    g2.drawImage(iii, 0, 0, 400, 70, null);
                } catch(Exception e) {}
            }
        });
        
        gf.addFunction(2, "cleerScreen", new Runnable() {
            @Override
            public void run() {
                if(screen >= 1 && screen < 22) {
                    g.setColor(Color.GRAY);
                    screen++;
                }
                else if(screen >= 22 && screen < 43) {
                    g.setColor(new Color(30, 30, 140));
                    screen++;
                }
                else if(screen >= 43 && screen < 65) {
                    g.setColor(new Color(100,0,168));
                    screen++;
                } else if(screen == 65)
                    screen = 1;
                g.fillRect(0, 0, 1200, 800);
                stars.draw();
                capsules.checkIfAnyEaten();
                capsules.drawAll();
            }
        });
        
        gf.addFunction(3, "st", new Runnable() {
            @Override
            public void run() {
                activateEnemies();
                drawEnemies();
            }
        });
        
        gf.addFunction(4, "draw", new Runnable() {
            @Override
            public void run() {
                a.draw();
                lasers.moveLasersAlong(true);
            }
        });

        gf.addFunction(5, "checkDiedth", new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<enemies.size(); i++) {
                    try {
                        for(int j=0; j<enemies.get(j).bombs.bombs.size(); j++) {
                            try {
                                enemies.get(j).bombs.moveAlongBombs();
                                enemies.get(j).bombs.drawAll();
                            } catch(Exception e) {}
                        }
                    } catch(Exception e) {}
                    Random random = new Random();
                    int randomValue = random.nextInt(480);
                    if(randomValue == 0) {
                        enemies.get(i).bombs.throwBomb(enemies.get(i).x, enemies.get(i).y);
                        move_stars = true;
                    }
                    enemies.get(i).lasers1.moveLasersAlong(false);
                    for(int j=0; j<enemies.get(i).lasers1.lasers.size(); j++) {
                        if(enemies.get(i).lasers1.lasers.get(j).x >= a.x + 4 && enemies.get(i).lasers1.lasers.get(j).x <= a.x + 34 &&
                                enemies.get(i).lasers1.lasers.get(j).y >= a.y - 20 && enemies.get(i).lasers1.lasers.get(j).y <= a.y + 39) {
                            a.lives--;
                        }
                    }
                    if(!enemies.get(i).isAlive()) {
                        enemies.remove(enemies.get(i));
                        points += 100;
                    }
                }
                if(initCount >= 0 && initCount <= 6 && enemies.size() <= 2) {
                    if(!rocks.moveDownAlong()) {
                        initEnemies();
                        initEnemies();
                    }
                }
                else if(initCount >= 3 && initCount < 5) {
                    boss1.isDowned();
                    if(enemies.size() <= 6) {
                        enemies.clear();
                        initEnemies();
                        initEnemies();
                    } else {
                        Random v = new Random();
                        int randomX = v.nextInt(10) - v.nextInt(10);
                        boss1.x = 400+randomX;
                        boss1.y = 200;
                        boss1.draw();
                        boss1.rs.moveRaysAlong();
                        for(int i=0; i<boss1.rs.rs.size(); i++) {
                            if(a.x >= boss1.rs.rs.get(i).x && a.x <= boss1.rs.rs.get(i).x + 180 &&
                                    a.y >= boss1.rs.rs.get(i).y && a.y <= boss1.rs.rs.get(i).y + 118) {
                                a.lives -= 14;
                                boss1.rs.rs.remove(boss1.rs.rs.get(i));
                            }
                        }
                    }
                }
                else if(initCount >= 6 && initCount < 8) {
                    boss2.isDowned();
                    if(enemies.size() <= 6 && !boss2.isAlive()) {
                        enemies.clear();
                        initEnemies();
                        initEnemies();
                        initEnemies();
                    } else {
                        Random v = new Random();
                        int randomX = v.nextInt(10) - v.nextInt(10);
                        boss2.x = 400+randomX;
                        boss2.y = 200;
                        boss2.draw();
                        boss2.rs.moveRaysAlong();
                        for(int i=0; i<boss2.rs.rs.size(); i++) {
                            if(a.x >= boss2.rs.rs.get(i).x && a.x <= boss2.rs.rs.get(i).x + 180 &&
                                    a.y >= boss2.rs.rs.get(i).y && a.y <= boss2.rs.rs.get(i).y + 118) {
                                a.lives -= 14;
                                boss2.rs.rs.remove(boss2.rs.rs.get(i));
                            }
                        }
                    }
                }
                else if(initCount >= 9 && initCount < 11) {
                    boss3.isDowned();
                    boss4.isDowned();
                    if(enemies.size() <= 6) {
                        enemies.clear();
                        initEnemies();
                        initEnemies();
                    } else {
                        Random v = new Random();
                        int randomX = v.nextInt(10) - v.nextInt(10);
                        boss3.x = 400+randomX;
                        boss3.y = 200;
                        boss3.draw();

                        Random v1 = new Random();
                        int randomX1 = v1.nextInt(10) - v1.nextInt(10);
                        boss4.x = 600+randomX1;
                        boss4.y = 450;
                        boss4.draw();
                        if(icount == -1)
                            icount = 2;
                        else if(icount != 2)
                            icount = 1;

                        boss3.rs.moveRaysAlong();
                        for(int i=0; i<boss3.rs.rs.size(); i++) {
                            if(a.x >= boss3.rs.rs.get(i).x && a.x <= boss3.rs.rs.get(i).x + 180 &&
                                    a.y >= boss3.rs.rs.get(i).y && a.y <= boss3.rs.rs.get(i).y + 118) {
                                a.lives -= 14;
                                boss3.rs.rs.remove(boss3.rs.rs.get(i));
                            }
                        }

                        boss4.rs.moveRaysAlong();
                        for(int i=0; i<boss4.rs.rs.size(); i++) {
                            if(a.x >= boss4.rs.rs.get(i).x && a.x <= boss4.rs.rs.get(i).x + 180 &&
                                    a.y >= boss4.rs.rs.get(i).y && a.y <= boss4.rs.rs.get(i).y + 118) {
                                a.lives -= 14;
                                boss4.rs.rs.remove(boss4.rs.rs.get(i));
                            }
                        }
                    }
                } else if(initCount >= 10) {
                    if(icount == 1) {
                        initCount = 0;
                        icount = -1;
                    }
                    if(initCount != 0) {
                        playerOneWon();
                    }
                }
            }
        });

        gf.addFunction(6, "titleIt", new Runnable() {
            @Override
            public void run() {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.ITALIC, 20));
                g.drawString("points = " + points, 100, 100);
                g.setColor( new Color (100, 70, 230));
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                g.drawString("arrow Keys to move & SPACEBAR to shoot with.", 700, 100);
                j.setTitle("lives: " + a.lives);
                if(a.lives == 0)
                    System.exit(0);
            }
        });

        play();
    }

    public static enum BombState {
        MOVE, TRAP
    }
    
    class Bombs {
        ArrayList<Bomb> bombs = new ArrayList<>();
        class Bomb {
            int x, y;
            BombState bs = BombState.MOVE;
            int iter = 0;
            int widthHeight = 20;
        }
        public void throwBomb(int enemyx, int enemyy) {
            Bomb bomb = new Bomb();
            bomb.x = enemyx;
            bomb.y = enemyy;
            bombs.add(bomb);
        }
        public void drawAll() {
            for(int i=0; i<bombs.size(); i++) {
                Bomb bomb = bombs.get(i);
                draw(bomb);
            }
        }
        public void draw(Bomb bomb) {
            g.setColor(Color.PINK);
            g.drawOval(bomb.x, bomb.y, bomb.widthHeight, bomb.widthHeight);
        }
        public void moveAlongBombs() {
            for(int i=0; i<bombs.size(); i++) {
                Bomb bomb = bombs.get(i);
                if(bomb.bs == BombState.MOVE) {
                    bomb.y += 3;
                }
                bomb.iter++;
                if(bomb.iter > 9) {
                    bomb.bs = BombState.TRAP;
                }
                if(bomb.bs == BombState.TRAP) {
                    bomb.widthHeight++;
                    if(bomb.iter > 95) {
                        bombs.remove(bomb);
                    }
                }
            }
        }
    }
    
    public void playerOneWon() {
        g.setFont(new Font("SERIF", Font.BOLD, 41));
        g.setColor(Color.YELLOW);
        g.drawString("CONGRATULATIONS, PLAYER ONE, YOU WON", 100, 300);
        Thread t = new Thread() {
            public void run() {
                try {
                    Thread.sleep(10000);
                    System.exit(0);     
                } catch(Exception e) {}
            }
        };
        t.start();
    }
    
    private void drawEnemies() {
        for(int i=0; i<enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.draw();
        }
    }

    Graphics g2;

    private void play() {
        try{
            audioInputStream =
                AudioSystem.getAudioInputStream(
                    this.getClass().getResource("galagath.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        Capsule cred = new Capsule(Color.RED);
        cred.x = -100;
        cred.y = -100;

        a.x = 500;
        a.y = 700;
        a.capsule = cred;

        g2 = bottomSidePanel.getGraphics();
        
        Thread main = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    gf.startLoop();
                }
            }
        });
        main.start();
    }
    
    public static void main(String[] args) {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        JFrame j = new JFrame();
                        JPanel p = new JPanel();
                        j.setBounds(100, 100, 500, 600);
                        p.setBounds(j.getBounds());
                        j.add(p);
                        j.setVisible(true);
                        Graphics g = p.getGraphics();
                                            
                        URL url = this.getClass().getResource("large.gif");
                        ImageIcon icon = new ImageIcon(url);
                        Thread t = new Thread() {
                            public void run() {
                                while(true) {
                                    g.drawImage(icon.getImage(), 0, 0, 500, 500, null);
                                    try {
                                        Thread.sleep(300);
                                    } catch(Exception e) {}
                                    g.setColor(Color.white);
                                    g.fillRect(0, 0, 500, 500);
                                }
                            }
                        };
                        t.start();

                        Thread.sleep(13020);
                        
                        j.dispose();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    
                    Galaga galaga = new Galaga();
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}