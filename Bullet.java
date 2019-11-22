import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

class Bullet {
    public int x,y,incrementX,incrementY;
    private double speed=15;
    private double degrees;
    private BufferedImage bulletImage;

    public Bullet(int ballX,int ballY,int mouseX,int mouseY){
        x=ballX;
        y=ballY;
        ballY=Main.HEIGHT-ballY;
        mouseY=Main.HEIGHT-mouseY;
        double slope=(double)(mouseY-ballY)/(double)(mouseX-ballX);
        degrees=Math.atan(slope);
        //System.out.println(" BallX: "+ballX+" BallY: "+ballY+" MouseX: "+mouseX+" MouseY: "+mouseY+" Degrees: "+degrees);
        if (mouseX>ballX && mouseY>ballY){ //Panw deksia
            incrementX=(int)(Math.cos(degrees)*speed);
            incrementY=-(int)(Math.sin(degrees)*speed);
            degrees=10+Math.toDegrees(degrees);
        } else if (mouseX<ballX && mouseY>ballY){ //Panw aristera
            incrementX=-(int)(Math.cos(degrees)*speed);
            incrementY=(int)(Math.sin(degrees)*speed);
            degrees=160+Math.toDegrees(degrees);
        } else if (mouseX>ballX && mouseY<ballY){ //Katw deksia
            incrementX=(int)(Math.cos(degrees)*speed);
            incrementY=-(int)(Math.sin(degrees)*speed);
            degrees=-10+Math.toDegrees(degrees);
        } else if (mouseX<ballX && mouseY<ballY){ //Katw aristera
            incrementX=-(int)(Math.cos(degrees)*speed);
            incrementY=(int)(Math.sin(degrees)*speed);
            degrees=190+Math.toDegrees(degrees);
        }
        //System.out.println("Increment X: "+incrementX+" Increment Y: "+incrementY+" COS: "+Math.cos(degrees)+" SIN: "+Math.sin(degrees));
        File bulletFile = new File("bullet.png");
        try {
            bulletImage = ImageIO.read(bulletFile);
        }catch(Exception e){
            System.out.println("ERROR opening bullet image.");
        }
        bulletImage=rotate(bulletImage,-degrees);
    }

    public void move(){
        x+=incrementX;
        y+=incrementY;
    }

    public BufferedImage getImage(){
        return this.bulletImage;
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
