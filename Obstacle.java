import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class Obstacle {
    public int x;
    public int y;
    private BufferedImage obstacle;
    public int width;
    public int height;
    Random rand = new Random();


    Obstacle(String name) {
        width = 25 + rand.nextInt(100);
        this.x = Main.WIDTH;
        height = width * 2;
        this.y = Main.HEIGHT - height - 50;
        File obstacleFile = new File(name);
        try {
            this.obstacle = ImageIO.read(obstacleFile);
        } catch (Exception ex) {
            System.out.println("Error with opening image at class obstacle");
        }
    }

    public BufferedImage getImage() {
        return this.obstacle;
    }

    public  void move(){
        this.x -=GamePanel.backgroundSpeed;
    }

    public boolean collide(int nX,int nY,int nW,int nH){
        if(nX>this.x&&nY<0){
            return false;
        }else{
            return nX < this.x + this.width && nX + nW > this.x && nY < this.y + this.height && nY + nH > this.y;
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

}

