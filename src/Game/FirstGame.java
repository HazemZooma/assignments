package Game;

import Texture.TextureReader;
import Texture.AnimListener;

import java.awt.event.*;
import java.io.IOException;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import java.util.ArrayList;

public class FirstGame extends AnimListener {
    ArrayList<Letter> letters = new ArrayList<>();
    ArrayList<Letter> score = new ArrayList<>();
    double posY = 90;
    double speed = 0.5;
    int basketX = 50;
    boolean check = false;
    int scoreOfPlayer = 0;
    String printScore;
    int maxWidth = 100;
    int maxHeight = 100;

    public FirstGame() {
    }

    String[] textureNames = {
            "Man1.png", "Man2.png", "Man3.png", "Man4.png",
            "a.png", "b.png", "c.png", "d.png", "e.png", "f.png", "g.png", "h.png", "i.png", "j.png", "k.png",
            "l.png", "m.png", "n.png", "o.png", "p.png", "q.png", "r.png", "s.png", "t.png", "u.png", "v.png",
            "w.png", "x.png", "y.png", "z.png", "0.png", "1.png", "2.png", "3.png", "4.png", "5.png", "6.png",
            "7.png", "8.png", "9.png", "..png", "HealthB.png", "Health.png", "Back.png", "NinjaStar.png", "Basket.png"
    };
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    public int[] textures = new int[textureNames.length];

    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA,
                        texture[i].getWidth(),
                        texture[i].getHeight(),
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels()
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
        score.add(new Letter(0, 90, textures.length - 16, 0.8f, 0.8f));
        randomLetter();
    }

    @Override
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        DrawBackground(gl);
        DrawSprite(gl, basketX, 5, textureNames.length - 1, 1.6f, 1.6f);
        letterFall();
        check();
        printScore();


        letters.forEach(letter -> letter.drawSprite(gl, textures));
        score.forEach(score -> score.drawSprite(gl, textures));

    }

    public void randomLetter() {
        int randomLetter = (int) (Math.random() * 26) + 4;
        int posX = (int) (Math.random() * 90);
        letters.add(new Letter(posX, posY, randomLetter, 1, 1));
    }

    public void letterFall() {
        posY -= speed;
        letters.get(letters.size() - 1).setY(posY);
        if (letters.get(letters.size() - 1).getY() <= 0 || check) {
            letters.remove(0);
            posY = 90;
            speed *= 1.05;
            randomLetter();
            printScore();
            check = false;
        }
    }

    public void printScore() {
        score.clear();
        int width = 0;
        printScore = Integer.toString(scoreOfPlayer);
        for (int i = printScore.length() - 1; i >= 0; i--) {
            int curr = Integer.parseInt(String.valueOf(printScore.charAt(i)));
            score.add(new Letter(width + (10 * i), 90, textureNames.length - 16 + curr, 0.8f, 0.8f));
        }
    }

    public void check() {
        double letterPosX = letters.get(letters.size() - 1).getX();
        double letterPosY = letters.get(letters.size() - 1).getY();
        if (Math.abs(letterPosX - basketX) <= 3 && Math.abs(letterPosY - 9) <= 3) {
            check = true;
            scoreOfPlayer+=10;
        }
    }


    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void DrawSprite(GL gl, float x, float y, int index, float scaleX, float scaleY) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scaleX, 0.1 * scaleY, 1);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textures.length - 3]);

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) basketX += 3;
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) basketX -= 3;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}