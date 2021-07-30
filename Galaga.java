package galaga;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Galaga implements KeyListener {

    JFrame j = new JFrame();
    JPanel p = new JPanel();
    Graphics g = null;
    ArrayList<Enemy> enemies = new ArrayList<>();
    PlayerOne a = new PlayerOne();
    Lasers lasers = new Lasers();
    
    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if(ke.getKeyCode() == KeyEvent.VK_SPACE) {
            lasers.addLaser(a.x, a.y);
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    class Lasers {
        ArrayList<Point> lasers = new ArrayList<>();
        public void addLaser(int x, int y) {
            Point pt = new Point();
            pt.x = x;
            pt.y = y;
            lasers.add(pt);
        }
        public void moveLasersAlong() {
            for(Iterator<Point> it = lasers.iterator(); it.hasNext(); ) {
                g.setColor(Color.PINK);
                Point pt = it.next();
                g.drawRect(pt.x, pt.y, 20, 40);
                pt.y -= 20;
            }
        }
    }
    
    class Point {
        int x, y;
    }
    
    class PlayerOne {
        int x, y;
        Image image = null;
        PlayerOne() {
            try {
                image = ImageIO.read(getClass().getResource("playerOne.png"));
            } catch(Exception e) {}
        }
        public void draw() {
            try {
                g.drawImage(image, x, y, 30, 30, null);
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
        Image image = null;
        int lives = 2;
        int actionCursor = 0;
        int actionCursorIn = 99999;
        int x, y;
        Set<Actions> actionsSet = new HashSet<>();
        Actions actions = new Actions();
        public void draw() {
            try {
                g.drawImage(image, x, y, 30, 30, null);
            } catch(Exception e) {}
        }
        public boolean isAlive() {
            return true;
        }
        Enemy(int x, int y, String plan) {
            try {
                this.x = x;
                this.y = y;
                Random r = new Random();
                int v = r.nextInt(7);
                if(v == 0) {
                    image = ImageIO.read(getClass().getResource("a-enemy.png"));
                }
                if(v == 1) {
                    image = ImageIO.read(getClass().getResource("b-enemy.png"));
                }
                if(v == 2) {
                    image = ImageIO.read(getClass().getResource("c-enemy.png"));
                }
                if(v == 3) {
                    image = ImageIO.read(getClass().getResource("d-enemy.png"));
                }
                if(v == 4) {
                    image = ImageIO.read(getClass().getResource("e-enemy.png"));
                }
                if(v == 5) {
                    image = ImageIO.read(getClass().getResource("f-enemy.png"));
                }
                if(v == 6) {
                    image = ImageIO.read(getClass().getResource("g-enemy.png"));
                }
                init(plan);
            } catch(Exception e) {}
        }
        private void init(String plan) {
            if(plan.equals("A")) {
                Action a = new Action();
                a.movex = -2;
                a.movey = 4;

                actions.actions.add(a);

                a = new Action();
                a.movex = 0;
                a.movey = 2;

                actions.actions.add(a);

                a = new Action();
                a.movex = 2;
                a.movey = 4;

                actions.actions.add(a);


                a = new Action();
                a.movex = -2;
                a.movey = -4;

                actions.actions.add(a);

                a = new Action();
                a.movex = -4;
                a.movey = -6;

                actions.actions.add(a);

                a = new Action();
                a.movex = -0;
                a.movey = -4;

                actions.actions.add(a);

                actionsSet.add(actions);
            }
            else if(plan.equals("B")) {
                Action a = new Action();
                a.movex = 2;
                a.movey = 4;

                actions.actions.add(a);

                a = new Action();
                a.movex = 0;
                a.movey = 2;

                actions.actions.add(a);

                a = new Action();
                a.movex = 12;
                a.movey = 4;

                actions.actions.add(a);


                a = new Action();
                a.movex = 6;
                a.movey = 6;

                actions.actions.add(a);

                a = new Action();
                a.movex = 10;
                a.movey = 2;

                actions.actions.add(a);

                a = new Action();
                a.movex = 5;
                a.movey = -8;

                actions.actions.add(a);

                a = new Action();
                a.movex = 2;
                a.movey = -14;

                actions.actions.add(a);

                a = new Action();
                a.movex = 0;
                a.movey = -14;

                actions.actions.add(a);

                a = new Action();
                a.movex = 0;
                a.movey = -14;

                actions.actions.add(a);

                a = new Action();
                a.movex = 0;
                a.movey = -14;

                actions.actions.add(a);

                a = new Action();
                a.movex = -5;
                a.movey = -2;

                actions.actions.add(a);
                
                a = new Action();
                a.movex = -5;
                a.movey = -2;

                actions.actions.add(a);

                a = new Action();
                a.movex = -5;
                a.movey = -2;

                actions.actions.add(a);

                a = new Action();
                a.movex = -5;
                a.movey = -2;

                actions.actions.add(a);

                a = new Action();
                a.movex = -5;
                a.movey = -2;

                actions.actions.add(a);

                a = new Action();
                a.movex = -5;
                a.movey = -2;

                actions.actions.add(a);

                a = new Action();
                a.movex = -5;
                a.movey = -2;

                actions.actions.add(a);

                if(y < 100) {
                    a = new Action();
                    a.movex = -4;
                    a.movey = 6;

                    actions.actions.add(a);
                }
                
                actionsSet.add(actions);
            }
        }
        private void planMove() {
            for(Iterator<Actions> j = actionsSet.iterator();j.hasNext();) {
                Actions q = j.next();
                for(int ii = 0; ii<q.actions.size(); ii++) {
                    ArrayList<Action> aa = q.actions;
                    for(int i=0;i<=actionCursorIn;i++) {
                        try {
                            Action z = aa.get(i);
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
                                if(aa.size() == actionCursorIn)
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
    
    private void initEnemies() {
        String plan = "A";
        int v = 50;
        for(int i=0; i<3; i++) {
            Enemy enemy = new Enemy(300, 500, plan);
            enemy.x += v*i;
            enemies.add(enemy);
        }

        plan = "B";
        v = 50;
        for(int i=0; i<4; i++) {
            Enemy enemy = new Enemy(800, 500, plan);
            enemy.x += v*i;
            enemies.add(enemy);
        }
    }
    
    public Galaga() {
        
        j.setLayout(null);
        j.setBounds(0, 0, 1200, 800);
        p.setBounds(j.getBounds());
        j.add(p);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        j.setVisible(true);
        
        g = p.getGraphics();
        
        initEnemies();
        
        j.addKeyListener(this);
        
        play();
    }
    
    private void drawEnemies() {
        for(int i=0; i<enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.draw();
        }
    }
    
    private void play() {
        a.x = 500;
        a.y = 700;
        Thread t = new Thread() {
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(100);
                    } catch(Exception e) {}
                    
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, 1200, 800);
                    
                    activateEnemies();
                    drawEnemies();
                    
                    a.draw();
                    lasers.moveLasersAlong();
                }
            }
        };
        t.start();
    }
    
    public static void main(String[] args) {
        Galaga galaga = new Galaga();
    }
    
}
