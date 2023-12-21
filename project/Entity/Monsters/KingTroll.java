package Entity.Monsters;

import Entity.Bullet;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Random;

/*
* Strongest troll in the game. Final boss
 */
public class KingTroll extends Troll {
    public KingTroll() {
        super();
    }

    public void getTrollImage() {
        idle_1 = new Image("file:Resources/Trolls/SuperTroll_idle_1.png");
        idle_2 = new Image("file:Resources/Trolls/SuperTroll_idle_2.png");
        right_1 = new Image("file:Resources/Trolls/SuperTroll_right_1.png");
        right_2 = new Image("file:Resources/Trolls/SuperTroll_right_2.png");
        left_1 = new Image("file:Resources/Trolls/SuperTroll_left_1.png");
        left_2 = new Image("file:Resources/Trolls/SuperTroll_left_2.png");
        up_2 = new Image("file:Resources/Blocks/Top_Wall.png");
    }

    public void draw(GraphicsContext gc) {
        super.draw(gc);
        int num = (int) Math.floor(Math.random() *(30 - 1 + 1) + 1);
        if (num == 8 && alive) {
            this.bullets.add(new Bullet(this));
        }
    }

    public void setAction() {
        actionLockCounter++;
        if (actionLockCounter == 40) {
            Random randInt = new Random();
            int i = randInt.nextInt(100) + 1;
            if (i <= 20) {
                direction = "up";
            }
            if (i > 20 && i <= 40) {
                direction = "down";
            }
            if (i > 40 && i <= 60) {
                direction = "left";
            }
            if (i > 60 && i <= 80) {
                direction = "right";
            }
            if (i > 80) {
                direction = "idle";
            }
            actionLockCounter = 0;
        }
    }

    @Override
    public void setDamage() {
        damage = 2;
    }

    @Override
    public void setHealth() {
        health = 8;
    }
}
