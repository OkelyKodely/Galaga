package galaga;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

//author @Danicel Cho
//github.com/okelykodely
public class Galaga implements KeyListener {

    JFrame j = new JFrame();
    JPanel p = new JPanel();
    Graphics g = null;
    ArrayList<Enemy> enemies = new ArrayList<>();
    PlayerOne a = new PlayerOne();
    Lasers lasers = new Lasers();
    int points = 0;
    
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
    
    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        switch(ke.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                lasers.addLaser(a.x, a.y);
                break;
            case KeyEvent.VK_UP:
                a.y -= 10;
                break;
            case KeyEvent.VK_DOWN:
                a.y += 10;
                break;
            case KeyEvent.VK_LEFT:
                a.x -= 10;
                break;
            case KeyEvent.VK_RIGHT:
                a.x += 10;
                break;
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
        public void moveLasersAlong(boolean UP) {
            for(Iterator<Point> it = lasers.iterator(); it.hasNext(); ) {
                g.setColor(Color.WHITE);
                Point pt = null;
                try {
                    pt = it.next();
                    g.fillRect(pt.x, pt.y, 20, 40);
                    if(UP)
                        pt.y -= 50;
                    else
                        pt.y += 10;
                } catch(Exception e) {}
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
        Lasers lasers1 = new Lasers();
        Set<Actions> actionsSet = new HashSet<>();
        Actions actions = new Actions();
        public void draw() {
            try {
                g.drawImage(image, x, y, 30, 30, null);
                Random r = new Random();
                int v = r.nextInt(100);
                if(v == 0) {
                    this.lasers1.addLaser(x, y+50);
                }
            } catch(Exception e) {}
        }
        public boolean isAlive() {
            for(int i=0; i<lasers.lasers.size(); i++) {
                if(lasers.lasers.get(i).x >= x && lasers.lasers.get(i).x <= x + 30 &&
                        lasers.lasers.get(i).y >= y && lasers.lasers.get(i).y <= y + 30) {
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
                if(plan.equals("A")) {
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
                a.movex = -2; a.movey = -4;
                actions.actions.add(a);

                a = new Action();
                a.movex = -4; a.movey = -6;
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
                a.movex = 5; a.movey = -8;
                actions.actions.add(a);

                a = new Action();
                a.movex = 2; a.movey = -14;
                actions.actions.add(a);

                a = new Action();
                a.movex = 0; a.movey = -14;
                actions.actions.add(a);

                a = new Action();
                a.movex = 0; a.movey = -14;
                actions.actions.add(a);

                a = new Action();
                a.movex = 0; a.movey = -14;
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
        public void init() {
            for(int i=0; i<200; i++) {
                Random r = new Random();
                Point p = new Point();
                p.x = r.nextInt(1200);
                p.y = r.nextInt(800);
                stars.add(p);
            }
        }
        public void draw() {
            g.setColor(Color.WHITE);
            for(int i=0; i<stars.size(); i++) {
                g.drawOval(stars.get(i).x, stars.get(i).y, 1, 1);
            }
        }
    }
    
    private void initEnemies() {
        String plan = "A";
        int v = 50;
        for(int i=0; i<3; i++) {
            Enemy enemy = new Enemy(300, vv*100, plan);
            enemy.x += v*i;
            enemies.add(enemy);
        }

        plan = "B";
        v = 50;
        for(int i=0; i<4; i++) {
            Enemy enemy = new Enemy(800, vv*100, plan);
            enemy.x += v*i;
            enemies.add(enemy);
        }
        
        vv--;
        if(vv == 3)
            vv = 7;
    }
    
    int game_width= 1300;
    int game_height = 900;
    
    int frame_width = 1200;
    int frame_height = 800;
    
    JPanel rightSidePanel = new JPanel();
    int right_side_panel_width = 100;
    int right_side_panel_height = 800;
    
    JPanel bottomSidePanel = new JPanel();
    int bot_side_panel_width = 1300;
    int bot_side_panel_height = 100;

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
        bottomSidePanel.setBackground(Color.YELLOW);
        j.add(bottomSidePanel);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        j.setVisible(true);
        j.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        j.setVisible(true);        
        g = p.getGraphics();
        j.addKeyListener(this);
        
        stars.init();
        initEnemies();
        initEnemies();
        
        play();
    }
    
    private void drawEnemies() {
        for(int i=0; i<enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.draw();
        }
    }
    
    private void play() {
        //where u at?
        a.x = 500;
        a.y = 700;

        GameFunctor gf = new GameFunctor();
        gf.addFunction(1, "sleepOrPause", new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch(Exception e) {}
            }
        });
        
        gf.addFunction(2, "cleerScreen", new Runnable() {
            @Override
            public void run() {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, 1200, 800);
                stars.draw();
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
                    enemies.get(i).lasers1.moveLasersAlong(false);
                    for(int j=0; j<enemies.get(i).lasers1.lasers.size(); j++) {
                        if(enemies.get(i).lasers1.lasers.get(j).x >= a.x && enemies.get(i).lasers1.lasers.get(j).x <= a.x + 30 &&
                                enemies.get(i).lasers1.lasers.get(j).y >= a.y && enemies.get(i).lasers1.lasers.get(j).y <= a.y + 30) {
                            System.exit(0);
                        }
                    }
                    if(!enemies.get(i).isAlive()) {
                        enemies.remove(enemies.get(i));
                        points += 100;
                    }
                }

                if(enemies.size() <= 2) {
                    initEnemies();
                    initEnemies();
                }
            }
        });

        gf.addFunction(6, "titleIt", new Runnable() {
            @Override
            public void run() {
                g.setColor(Color.WHITE);
                g.drawString("Points: " + points, 100, 100);
                j.setTitle("Enemies: " + enemies.size());
            }
        });
        
        Thread main = new Thread() {
            public void run() {
                while(true) {
                    gf.startLoop();
                }
            }
        };
        main.start();
    }
    
    public static void main(String[] args) {
        try {
            EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    Galaga galaga = new Galaga();
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}
