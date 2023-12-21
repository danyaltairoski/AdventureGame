package Entity.Monsters;

import Entity.Entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Random;

public abstract class Troll extends Entity {
    public int health;
    protected int imgCtr = 0;
    protected int imageNum = 1;
    public int actionLockCounter = 0;

    public boolean alive = true;

    /*
    * Get the images for the troll from the files that need to be drawn onto the screen
     */
    public abstract void getTrollImage();

    /*
    * Set how much damage this troll can do in one hit
     */
    public abstract void setDamage();

    /*
    * Set the health of the troll, higher trolls have more health
     */
    public abstract void setHealth();

    /*
    * Troll is basically an AI, so randomize its movements
     */
    public abstract void setAction();

    public Troll() {
        setDamage();
        setHealth();
        getTrollImage();
        healthBar.setFill(Color.RED);
    }

    /*
    * Update the trolls position and image to show based on the direction its walking
     */
    public void update(GraphicsContext gc) {
        setAction();
        gc.clearRect(
                getX(),
                getY(),
                48,
                48
        );
        gc.clearRect(
                getX(),
                getY() + 10,
                10,
                48
        );
        if (direction.equals("up")) {
            setY(getY() - 1);
        } else if (direction.equals("left")) {
            setX(getX() - 1);
        } else if (direction.equals("down")) {
            setY(getY() + 1);
        } else if (direction.equals("right")) {
            setX(getX() + 1);
        }
        imgCtr++;
        if (imgCtr > 15) {
            if (imageNum == 1) {
                imageNum = 2;
            } else if (imageNum == 2) {
                imageNum = 1;
            }
            imgCtr = 0;
        }
        cooldown++;
    }

    /*
    * Draw the troll and health bar updates onto the screen and changes the images. Don't draw if troll is dead
     */
    public void draw(GraphicsContext gc) {
        Image image;
        if (direction.equals("up")) {
            if (imageNum == 1) {
                image = right_1;
            } else {
                image = right_2;
            }
        } else if (direction.equals("left")) {
            if (imageNum == 1) {
                image = left_1;
            } else {
                image = left_2;
            }
        } else if (direction.equals("down")) {
            if (imageNum == 1) {
                image = left_1;
            } else {
                image = left_2;
            }
        } else if (direction.equals("right")) {
            if (imageNum == 1) {
                image = right_1;
            } else {
                image = right_2;
            }
        } else {
            if (imageNum == 1) {
                image = idle_1;
            } else {
                image = idle_2;
            }
        }
        if (health > 0) {
            gc.fillRect(getX(), getY() - 10, 48 / ((double) 4 / health), 5);
            gc.setFill(Color.RED);
            gc.drawImage(image, getX(), getY(), 48, 48);
        } else {
            alive = false;
            setX(0);
            setY(0);
            image = up_1;
        }
    }
}
