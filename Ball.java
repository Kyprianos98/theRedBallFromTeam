import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;

class Ball {
    private int x;
    private int y;
    private final int ballWidth=50;
    private final int ballHeight=50;
    private double velocity = 0.0D;
    private double velocityH = 0.0D;
    private double gravity = 0.41D;
    private double gravityH = 0.1D;
    private double delayJump = 0.0D;
    private int angle;
    private boolean alive;
    private BufferedImage ballImage;


    Ball(int initialX, int initialY){
        x=initialX;
        y=initialY;
        alive=true;
        angle=0;
        File ballFile = new File("redball.png");
        try {
            ballImage = ImageIO.read(ballFile);
        }catch(Exception e){
            System.out.println("Cant open Image");
        }
    }

    public void moveRight(boolean longPressed){
        if (longPressed==false)
            velocityH = 4.0D;
        else
            velocityH = 6.0D;
    }

    public void inGame(Graphics g) {
        //Velocity
        this.velocity += this.gravity;
        if (this.delayJump > 0.0D) {
            --this.delayJump;
        }
        int newY = this.y + (int) this.velocity;
        if (newY <= (Main.HEIGHT - (2 * ballHeight))) {
            this.y = newY;
        } else {
            this.y = (Main.HEIGHT - (2 * ballHeight));
        }

        //Movement
        if (velocityH < 0) {
            velocityH += this.gravityH;
        }
        else if (velocityH>0){
            velocityH -= this.gravityH;
        }
        int newX = this.x + (int) this.velocityH;
        if (newX >= 0 && newX<=(Main.WIDTH-(2*this.ballWidth))) {
            this.x = newX;
        }

        //Angle Handler
        if (velocityH < 0)
            angle += velocityH;
        else if (velocityH > 0)
            angle += velocityH;
        if (angle > 360)
            angle -= 360;
        else if (angle < -360)
            angle += 360;

        //Draw
        g.drawImage(rotate(ballImage, angle), getX(), getY(), ballWidth, ballHeight, null);
    }

    public void moveLeft(boolean longPressed) {
        if (longPressed==false)
            velocityH = -6.0D;
        else
            velocityH = -8.0D;
    }

    public void jump(boolean doubleJump) {
        if (doubleJump==true) {
            this.velocity = -15.0D;
            this.delayJump=60.0D;
        }
        else if (this.delayJump < 1.0D) {
            this.velocity = -10.0D;
            this.delayJump = 60.0D;
        }
    }

    public boolean isAlive(){
        return this.alive;
    }

    public void kill() {
        this.alive = false;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    int getWidth(){
        return this.ballWidth;
    }

    int getHeight(){
        return this.ballHeight;
    }

    private BufferedImage rotate(BufferedImage bimg, double angle) {
        int w = bimg.getWidth();
        int h = bimg.getHeight();
        BufferedImage rotated = new BufferedImage(w, h, bimg.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.rotate(Math.toRadians(angle), w/2, h/2);
        graphic.drawImage(bimg, null, 0, 0);
        graphic.dispose();
        return rotated;
    }

}
