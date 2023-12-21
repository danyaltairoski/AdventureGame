package Entity.Monsters;

import Entity.Bullet;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Random;

/*
* Medium difficulty troll. Shoots bullets
 */
public class SuperTroll extends Troll {

    public SuperTroll() {
        super();
    }

    @Override
    public void draw(GraphicsContext gc) {
        super.draw(gc);
        int num = (int) Math.floor(Math.random() *(50 - 1 + 1) + 1);
        if (num == 8 && alive) {
            this.bullets.add(new Bullet(this));
        }
    }

    @Override
    public void setDamage() {
        damage = 1;
    }

    @Override
    public void setHealth() {
        health = 5;
    }

    @Override
    public void setAction() {
        actionLockCounter++;
        if (actionLockCounter == 120) {
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

    public void getTrollImage() {
        idle_1 = new Image("file:Resources/Trolls/KingTroll_idle_1.png");
        idle_2 = new Image("file:Resources/Trolls/KingTroll_idle_2.png");
        right_1 = new Image("file:Resources/Trolls/KingTroll_right_1.png");
        right_2 = new Image("file:Resources/Trolls/KingTroll_right_2.png");
        left_1 = new Image("file:Resources/Trolls/KingTroll_left_1.png");
        left_2 = new Image("file:Resources/Trolls/KingTroll_left_2.png");
        up_2 = new Image("file:Resources/Blocks/Top_Wall.png");
    }
}
