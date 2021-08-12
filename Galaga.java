package galaga;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

//author @Danicel Cho
//github.com/okelykodely
public class Galaga implements KeyListener {
    
    int lives = 10;
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
                g.setColor(Color.RED);
                if(UP)
                    g.setColor(Color.BLUE);
                Point pt = null;
                try {
                    pt = it.next();
                    g.fillRect(pt.x, pt.y, 5, 30 )  ;
                    if(UP)
                        pt.y -= 30;
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
        
        vv--;
        if(vv == 3)
            vv = 7;
    }
    
    int totalBombs = 0;
    
    int initCount = 0;
    
    Image iii = null;
    
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
        j.setVisible(true);        
        j.addKeyListener(this);
        
        
        Graphics g1 = rightSidePanel.getGraphics();
        Thread t1 = new Thread() {
            public void run() {
                int y = 10;
                while(true) {
                    try {
                        Thread.sleep(10);
                        g1.setColor(Color.PINK);
                        g1.fillRect(0, 0, 100, 800);
                        g1.setColor(Color.BLACK);
                        g1.setFont(new Font("Arial", Font.BOLD, 16));
                        y+=2;
                        y+=2;
                        y+=3;
                        g1.drawString("GALAGA", 10, y);
                        g1.drawString("GALAGA", 10, 200+y);
                        g1.drawString("GALAGA", 10, 400+y);
                        if(y > 500)
                            y=10;
                    } catch(Exception e) {}
                }
            }
        };
        //t1.start();
        
        stars.init();
        initEnemies();
        initEnemies();
        
        g = p.getGraphics();

        Graphics g2 = bottomSidePanel.getGraphics();
        
        try {
            Thread.sleep(1130);
        } catch(Exception e) {}
        try {
            iii = ImageIO.read(getClass().getResource("bot1.png"));
        } catch(Exception e) {}
        try {
            g2.drawImage(iii, 0, 0, 1300, 180, null);
        } catch(Exception e) {}

        gf.addFunction(1, "sleepOrPause", new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(130);
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
                        if(enemies.get(i).lasers1.lasers.get(j).x >= a.x && enemies.get(i).lasers1.lasers.get(j).x <= a.x + 30 &&
                                enemies.get(i).lasers1.lasers.get(j).y >= a.y && enemies.get(i).lasers1.lasers.get(j).y <= a.y + 30) {
                            lives --;
                        }
                    }
                    if(!enemies.get(i).isAlive()) {
                        enemies.remove(enemies.get(i));
                        points += 100;
                    }
                }
                if(initCount >= 0 && initCount <= 2 && enemies.size() <= 2) {
                    boss1.isDowned();
                    initEnemies();
                    initEnemies();
                }
                if(initCount >= 3 && initCount < 5) {
                    boss1.isDowned();
                    if(enemies.size() <= 2) {
                        initEnemies();
                        initEnemies();
                    } else {
                        Random v = new Random();
                        int randomX = v.nextInt(10) - v.nextInt(10);
                        boss1.x = 400+randomX;
                        boss1.y = 200;
                        boss1.draw();
                    }
                }
                else if(initCount >= 6 && initCount < 8) {
                    boss2.isDowned();
                    if(enemies.size() <= 3 && !boss2.isAlive()) {
                        initEnemies();
                        initEnemies();
                        initEnemies();
                    } else {
                        Random v = new Random();
                        int randomX = v.nextInt(10) - v.nextInt(10);
                        boss2.x = 400+randomX;
                        boss2.y = 200;
                        boss2.draw();
                    }
                }
                else if(initCount >= 9 && initCount < 11) {
                    boss3.isDowned();
                    if(enemies.size() <= 3 && !boss3.isAlive()) {
                        initEnemies();
                        initEnemies();
                        initEnemies();
                    } else {
                        Random v = new Random();
                        int randomX = v.nextInt(10) - v.nextInt(10);
                        boss3.x = 400+randomX;
                        boss3.y = 200;
                        boss3.draw();
                    }
                }else if(initCount >= 12 && initCount < 14) {
                    boss4.isDowned();
                    if(enemies.size() <= 3 && !boss4.isAlive()) {
                        initEnemies();
                        initEnemies();
                    } else {
                        Random v = new Random();
                        int randomX = v.nextInt(10) - v.nextInt(10);
                        boss4.x = 400+randomX;
                        boss4.y = 200;
                        boss4.draw();
                    }
                }
            }
        });

        gf.addFunction(6, "titleIt", new Runnable() {
            @Override
            public void run() {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Points: " + points, 100, 100);
                g.drawString("Arrow Keys to move & SPACEBAR to shoot with.", 700, 100);
                j.setTitle("Lives: " + lives);
                if(lives == 0)
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
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Galaga galaga = new Galaga();
                }
            });
        } catch(Exception e) {}
    }
}