import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class GamePannel extends JPanel implements ActionListener, KeyListener {

    private int[] snakeXLength=new int[750];
    private int[] snakeYLength=new int[750];
    private int lengthOfSnake=3;
    private int Score=0;
    private int[] xPos={25,50,75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500,525,550,575,600,625,650,675,700,725,750,775,800,825,850};
    private int[] yPos={75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500,525,550,575,600,625};
    private Random random=new Random();
    private int enemyX;
    private int enemyY;
    private int move=0;


    private boolean left=false;
    private boolean right=true;
    private boolean up=false;
    private boolean down=false;
    private boolean gameOver=false;


    private ImageIcon snaketitle=new ImageIcon(getClass().getResource("snaketitle.jpg"));
    private ImageIcon leftMouth=new ImageIcon(getClass().getResource("leftmouth.png"));
    private ImageIcon rightMouth=new ImageIcon(getClass().getResource("rightmouth.png"));
    private ImageIcon upMouth=new ImageIcon(getClass().getResource("upmouth.png"));
    private ImageIcon downMouth=new ImageIcon(getClass().getResource("downmouth.png"));
    private ImageIcon snakeImage=new ImageIcon(getClass().getResource("snakeimage.png"));
    private ImageIcon enemyImage=new ImageIcon(getClass().getResource("enemy.png"));
    private Timer timer;
    private int delay=100;

    GamePannel(){
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);

        timer=new Timer(delay,this);
        try{
            gameStartSound();
        }catch (IOException ee){

        } catch (UnsupportedAudioFileException ee) {
            throw new RuntimeException(ee);
        } catch (LineUnavailableException ee) {
            throw new RuntimeException(ee);
        }
        timer.start();

         newEnemy();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.white);
        g.drawRect(24,10,851,55);
        g.drawRect(24,74,851,576);

        snaketitle.paintIcon(this,g,25,11);
        g.setColor(Color.white);
        g.fillRect(25,75,850,575);

        if(move==0){
            snakeXLength[0]=100;
            snakeXLength[1]=75;
            snakeXLength[2]=50;
            snakeYLength[0]=100;
            snakeYLength[1]=100;
            snakeYLength[2]=100;

        }
        if(left){
           leftMouth.paintIcon(this,g,snakeXLength[0],snakeYLength[0]);
        }
        if(right){
            rightMouth.paintIcon(this,g,snakeXLength[0],snakeYLength[0]);
        }
        if(up){
            upMouth.paintIcon(this,g,snakeXLength[0],snakeYLength[0]);
        }
        if(down){
            downMouth.paintIcon(this,g,snakeXLength[0],snakeYLength[0]);
        }
        for (int i=0;i<lengthOfSnake;i++){
            snakeImage.paintIcon(this,g,snakeXLength[i],snakeYLength[i]);
        }

        enemyImage.paintIcon(this,g,enemyX,enemyY);

        if (gameOver){
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial",Font.BOLD,50));
            g.drawString("Game Over",300,300);

            g.setFont(new Font("Arial",Font.PLAIN,20));
            g.drawString("Press SPACE to restart",320,350);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial",Font.PLAIN,14));
        g.drawString("Score = "+Score,750,30);
        g.drawString("Length = "+lengthOfSnake,750,50);

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        for (int i=lengthOfSnake-1;i>0;i--){
            snakeXLength[i]=snakeXLength[i-1];
            snakeYLength[i]=snakeYLength[i-1];
        }

        if(left){
            snakeXLength[0]=snakeXLength[0]-25;
        }
        if(right){
            snakeXLength[0]=snakeXLength[0]+25;
        }
        if(up){
            snakeYLength[0]=snakeYLength[0]-25;
        }
        if(down){
            snakeYLength[0]=snakeYLength[0]+25;
        }

        if(snakeXLength[0]>850){
            snakeXLength[0]=25;
        }
        if(snakeXLength[0]<25){
            snakeXLength[0]=850;
        } 
        if (snakeYLength[0]>625) {
            snakeYLength[0]=75;
        }
        if (snakeYLength[0]<75) {
            snakeYLength[0]=625;
        }

        collidesWithEnemy();
        collidesWithBody();

        repaint();
    }
    private void gameStartSound() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        File file=new File("gameStart.wav");
        AudioInputStream audioInputStream= AudioSystem.getAudioInputStream(file);
        Clip clip=AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            try{
                gameStartSound();
            }catch (IOException ee){

            } catch (UnsupportedAudioFileException ee) {
                throw new RuntimeException(ee);
            } catch (LineUnavailableException ee) {
                throw new RuntimeException(ee);
            }
            reStart();

        }


        if (e.getKeyCode() == KeyEvent.VK_LEFT && (!right)) {
            left=true;
            right=false;
            up=false;
            down=false;
            move++;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && (!left)) {
            left=false;
            right=true;
            up=false;
            down=false;
            move++;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP && (!down)) {
            left=false;
            right=false;
            up=true;
            down=false;
            move++;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && (!up)) {
            left=false;
            right=false;
            up=false;
            down=true;
            move++;
        }

    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    void newEnemy() {
        enemyX = xPos[random.nextInt(34)];
        enemyY = yPos[random.nextInt(23)];

        for (int i = lengthOfSnake - 1; i >= 0; i--) {
            if (snakeXLength[i] == enemyX && snakeYLength[i] == enemyY) {
                newEnemy();
            }
        }
    }
    private void eatingSound() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        File file=new File("audio.wav");
        AudioInputStream audioInputStream= AudioSystem.getAudioInputStream(file);
        Clip clip=AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    }
    private void collidesWithEnemy()  {
   if(snakeXLength[0]==enemyX && snakeYLength[0]==enemyY){
       try{
           eatingSound();
       }catch (IOException e){

       } catch (UnsupportedAudioFileException e) {
           throw new RuntimeException(e);
       } catch (LineUnavailableException e) {
           throw new RuntimeException(e);
       }

       newEnemy();
       lengthOfSnake++;
       Score++;
   }
    }
    private void gameOverSound() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        File file=new File("gameOver.wav");
        AudioInputStream audioInputStream= AudioSystem.getAudioInputStream(file);
        Clip clip=AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    }
    private void collidesWithBody() {
        for (int i=lengthOfSnake-1;i>0;i--){
            if(snakeXLength[i]==snakeXLength[0] && snakeYLength[i]==snakeYLength[0]){
                try{
                    gameOverSound();
                }catch (IOException e){

                } catch (UnsupportedAudioFileException e) {
                    throw new RuntimeException(e);
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
                timer.stop();
                gameOver=true;
            }
        }
    }
    private void reStart(){
        gameOver=false;
        move=0;
        Score=0;
        lengthOfSnake=3;
        left=false;
        right=true;
        up=false;
        down=false;
        timer.start();
        repaint();
    }
}
