import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import java.util.Vector;

public class GamePanel extends JPanel implements KeyListener, MouseListener {
    private int key,longPressed=0,backgroundX1,backgroundX2,currentBackground,pausePressed,firstTickPause,ticksUntilNextSpike,firstTickOver;
    static final int backgroundSpeed=2;
    private static final int pausePositionX=Main.WIDTH-100;
    private static final int pausePositionY=25;
    private static final int playPositionX=Main.WIDTH/2-100;
    private static final int playPositionY=Main.HEIGHT/2-100;
    private static final int restartPositionX=playPositionX+100;
    private static final int restartPositionY=playPositionY;
    private Ball ball;
    boolean ready;
    private boolean rightPressed,leftPressed,leftLongPressed,jumpPressed,rightLongPressed,doubleJump;
    private BufferedImage background,pause,play,restart;
    private double delayDoubleJump = 0.0D;
    private Vector<Bullet> bullets;
    private Vector<Obstacle> spikes;
    private Vector<Integer> toDeleteSpikes;
    private Random rand=new Random();

    GamePanel(){
        try {
            File backgroundFile = new File("background.png");
            background = ImageIO.read(backgroundFile);
            File playFile = new File("play.png");
            play=ImageIO.read(playFile);
            File pauseFile = new File("pause.png");
            pause=ImageIO.read(pauseFile);
            File restartFile = new File("restart.png");
            restart=ImageIO.read(restartFile);

        }catch(Exception e){
            System.out.println("Cant open Image");
            return;
        }
        this.restart();
        this.addKeyListener(this);
        this.addMouseListener(this);
    }

    public void addNotify() {
        super.addNotify();
        this.requestFocus();
        this.ready = true;
    }

    private void restart(){
        ball=new Ball(50,600);
        bullets=new Vector<Bullet>(1);
        spikes=new Vector<Obstacle>(1);
        toDeleteSpikes=new Vector<Integer>(1);
        backgroundX1=0;
        backgroundX2=0;
        currentBackground=1;
        rightPressed=false;
        leftPressed=false;
        jumpPressed=false;
        rightLongPressed=false;
        longPressed=0;
        doubleJump=false;
        delayDoubleJump=0.0D;
        pausePressed=0;
        firstTickPause=0;
        firstTickOver=0;
        ticksUntilNextSpike=1;
    }

    public void paintComponent(Graphics g) {
        if (pausePressed == 0 && ball.isAlive()) {
            super.paintComponent(g);
            backgroundHandler(g);
            obstacleHandler(g);
            movementHandler();
            ball.inGame(g);
            g.drawImage(pause, pausePositionX, pausePositionY, 50, 50, null);
            for (Bullet temp : bullets){
                temp.move();
                g.drawImage(temp.getImage(),temp.x,temp.y,100,50,null);
            }
        }
        else if (ball.isAlive() && pausePressed == 1){
            if (firstTickPause == 0) {
                firstTickPause = 1;
                super.paintComponent(g);
                backgroundHandler(g);
                obstacleHandler(g);
                movementHandler();
                ball.inGame(g);
                g.drawImage(play, playPositionX, playPositionY, 50, 50, null);
                g.drawImage(restart,restartPositionX,restartPositionY,50,50,null);
            }
        }
        else {
            if (firstTickOver == 0) {
                firstTickOver = 1;
                super.paintComponent(g);
                backgroundHandler(g);
                movementHandler();
                ball.inGame(g);
                g.setColor(Color.red);
                g.setFont(new Font("Arial", Font.BOLD, 100));
                g.drawString("Game Over", Main.WIDTH / 2 - 200, Main.HEIGHT / 2 - 200);
                g.drawImage(restart, restartPositionX, restartPositionY, 50, 50, null);
            }
        }
    }

    private void movementHandler(){
        if (this.delayDoubleJump > 0.0D) {
            --this.delayDoubleJump;
        }
        if (rightLongPressed || rightPressed)
            ball.moveRight(rightLongPressed);
        if (leftPressed || leftLongPressed)
            ball.moveLeft(leftLongPressed);
        if (jumpPressed || doubleJump) {
            ball.jump(doubleJump);
            doubleJump = false;
            jumpPressed = false;
        }
    }

    private void obstacleHandler(Graphics g) {
        if (this.ball.isAlive()) {
            ticksUntilNextSpike--;
            if (ticksUntilNextSpike == 0) {
                ticksUntilNextSpike= 50+rand.nextInt(500);
                spikes.add(new Obstacle("spike.png"));
            }
            toDeleteSpikes = new Vector<Integer>(1);
            for (Obstacle temp : spikes) {
                temp.move();
                if (temp.collide(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight())) {
                    ball.kill();
                }
                if (temp.getX() > -temp.width) {
                    g.drawImage(temp.getImage(), temp.getX(), temp.getY(), temp.width, temp.height, null);
                } else {

                    toDeleteSpikes.add(spikes.indexOf(temp));
                }
            }
            if (toDeleteSpikes.size() > 0)
                for (Integer temp : toDeleteSpikes) {
                    spikes.remove(spikes.get(temp));
                }
        }
    }

    private void backgroundHandler(Graphics g){
        backgroundX1-=backgroundSpeed;
        backgroundX2-=backgroundSpeed;
        if (backgroundX1<(-1920+Main.WIDTH) && currentBackground==1) {
            backgroundX2 = Main.WIDTH-1;
            currentBackground=2;
        }
        else if (backgroundX2<(-1920+Main.WIDTH) && currentBackground==2) {
            backgroundX1 = Main.WIDTH-1;
            currentBackground=1;
        }
        g.drawImage(background,backgroundX1,0,1920,Main.HEIGHT,null);
        g.drawImage(background,backgroundX2,0,1920,Main.HEIGHT,null);
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {
        key=e.getKeyCode();
        switch (key){
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                longPressed=0;
                rightPressed=false;
                rightLongPressed=false;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                leftLongPressed=false;
                longPressed=0;
                leftPressed=false;
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                jumpPressed=false;
                doubleJump=false;
        }
    }

    public void keyPressed(KeyEvent e) {
        key=e.getKeyCode();
        switch (key){
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                longPressed++;
                if (longPressed>5){
                    rightLongPressed=true;
                }
                else {
                    rightPressed = true;
                }
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                longPressed++;
                if (longPressed>5){
                    leftLongPressed=true;
                }
                else {
                    leftPressed = true;
                }
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if (delayDoubleJump>80.0D) { //Double Jump
                    doubleJump = true;
                    delayDoubleJump=80.0D;
                }
                else if (delayDoubleJump<=0) {
                    jumpPressed = true;
                    delayDoubleJump=130.0D;
                }
        }
    }

    private void mouseHandler(MouseEvent e){
        int mouseX, mouseY;
        mouseX = e.getX();
        mouseY = e.getY();
        if (mouseX >= pausePositionX && mouseX <= pausePositionX + 50 && mouseY >= pausePositionY && mouseY <= pausePositionY + 50 && pausePressed == 0) {
            pausePressed = 1;
        }else if (mouseX >= playPositionX && mouseX <= playPositionX + 50 && mouseY >= playPositionY && mouseY <= playPositionY + 50 && pausePressed == 1){
            firstTickPause=0;
            pausePressed=0;
        }else if (mouseX >= restartPositionX && mouseX <= restartPositionX + 50 && mouseY >= restartPositionY && mouseY <= restartPositionY + 50 && (pausePressed == 1 || firstTickOver==1)){
            firstTickPause=0;
            pausePressed=0;
            restart();
        }else{
            bullets.add(new Bullet(ball.getX(),ball.getY(),mouseX,mouseY));
        }
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        mouseHandler(e);
    }

    public void mouseClicked(MouseEvent e) {
        mouseHandler(e);
    }

    public void mousePressed(MouseEvent e) {
    }

}