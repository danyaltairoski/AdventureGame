package Entity.Monsters;

import javafx.scene.image.Image;

import java.util.Random;

/*
* Easiest troll in the game, does no damage and has basic movement
 */
public class MiniTroll extends Troll {

    public MiniTroll() {
        super();
    }

    public void getTrollImage() {
        idle_1 = new Image("file:Resources/Trolls/MiniTroll_idle_1.png");
        idle_2 = new Image("file:Resources/Trolls/MiniTroll_idle_2.png");
        right_1 = new Image("file:Resources/Trolls/MiniTroll_right_1.png");
        right_2 = new Image("file:Resources/Trolls/MiniTroll_right_2.png");
        left_1 = new Image("file:Resources/Trolls/MiniTroll_left_1.png");
        left_2 = new Image("file:Resources/Trolls/MiniTroll_left_2.png");
        up_2 = new Image("file:Resources/Blocks/Top_Wall.png");
    }

    @Override
    public void setDamage() {
        damage = 0;
    }

    @Override
    public void setHealth() {
        health = 4;
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
}
