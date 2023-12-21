package Entity;

import AdventureModel.AdventureObject;
import AdventureModel.Collision;
import AdventureModel.Room;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import views.AdventureGameView;

import java.util.ArrayList;

/*
* This is an abstract class that controls all entities in the game (player, trolls)
 */
public class Entity {
    public int x = 336, y = 336; // x and y coordinates of the entity

    // Since there are multiple images that are drawn onto the screen to give the effect of the animation, this
    // attribute controls which number of the animation image needs to be drawn
    public int imageNum = 1;

    public int imgCtr; // Counter that controls the frame rate and speed at which the different images are drawn

    public Image idle_1, idle_2, up_1, up_2, down_1, down_2, left_1, left_2, right_1, right_2, attack_right_1, attack_right_2, attack_left_1, attack_left_2, attack_down_1, attack_down_2, attack_up_1, attack_up_2; // Images of the animations

    public String direction = "idle"; // Which way the character is facing

    public int health; // Health of entity

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x){this.x=x;}

    public void setY(int y){this.y=y;}

    public int actionLockCounter = 0;

    public boolean alive = true; // Is entity alive and should it be shown on screen?

    public boolean attacking = false; // Check if entity is attacking to determine when to do damage

    public Rectangle healthBar = new Rectangle(getX(), getY() + 10, 40, 10); // Draw healthbar on top on troll

    public int cooldown = 0; // Cool player or troll can't hit each other infinitely and kill in one hit

    public ArrayList<Bullet> bullets = new ArrayList<>(); // Bullets that this entity has shot, these are all updated in the loop

    public int damage; // How much damage does entity inflict when attacking
}
