package AdventureModel;

import Entity.Entity;
import views.AdventureGameView;

/*
* Class to check any entity collision
 */
public class Collision {

    AdventureGameView view;
    public Collision(AdventureGameView view) {
        this.view = view;
    }

    /*
    * Check if the entity is colliding with a wall based on the direction they are walking in, if they are move them back so they can't walk through it
     */
    public void checkCollision(Entity entity) {
        if (entity.direction.equals("up")) {
            if ((boolean) (this.view.blockMap.blocks.get(String.valueOf(this.view.blockMap.blockLoc[entity.getX() / 48][(entity.getY()) / 48])).get(1))
                    || (boolean) (this.view.blockMap.blocks.get(String.valueOf(this.view.blockMap.blockLoc[(entity.getX() + 48) / 48][(entity.getY()) / 48])).get(1))) {
                entity.setY(entity.getY() + 4);
            }
        } else if (entity.direction.equals("down")) {
            if ((boolean) (this.view.blockMap.blocks.get(String.valueOf(this.view.blockMap.blockLoc[entity.getX() / 48][(entity.getY() + 48) / 48])).get(1))
                    || (boolean) (this.view.blockMap.blocks.get(String.valueOf(this.view.blockMap.blockLoc[(entity.getX() + 48) / 48][(entity.getY() + 48) / 48])).get(1))) {
                entity.setY(entity.getY() - 4);
            }
        } else if (entity.direction.equals("left")) {
            if ((boolean) (this.view.blockMap.blocks.get(String.valueOf(this.view.blockMap.blockLoc[entity.getX() / 48][(entity.getY()) / 48])).get(1))
                    || (boolean) (this.view.blockMap.blocks.get(String.valueOf(this.view.blockMap.blockLoc[(entity.getX()) / 48][(entity.getY() + 48) / 48])).get(1))) {
                entity.setX(entity.getX() + 4);
            }
        } else if (entity.direction.equals("right")) {
            if ((boolean) (this.view.blockMap.blocks.get(String.valueOf(this.view.blockMap.blockLoc[(entity.getX() + 48) / 48][(entity.getY()) / 48])).get(1))
                    || (boolean) (this.view.blockMap.blocks.get(String.valueOf(this.view.blockMap.blockLoc[(entity.getX() + 48) / 48][(entity.getY() + 48) / 48])).get(1))) {
                entity.setX(entity.getX() - 4);
            }
        }
    }

    /*
    * Check if two entities are colliding, return true if they are. Used to check if player and troll are colliding so they cant walk through each other
    * Also use to check if entity is hit with a bullet
     */
    public boolean checkEntityCollision(Entity entity1, Entity entity2) {
        if (entity1.direction.equals("up")) {
            if ((entity1.getY() < entity2.getY() + 48 && entity1.getY() > entity2.getY()) && ((entity1.getX() > entity2.getX() && entity1.getX() < entity2.getX()+48) || (entity1.getX() + 48 > entity2.getX() && entity1.getX() + 48 < entity2.getX() + 48))) {
                entity1.setY(entity1.getY() + 2);
                return true;
            }
        } else if (entity1.direction.equals("down")) {
            if ((entity1.getY() + 48 > entity2.getY() && entity1.getY() + 48 < entity2.getY() + 48) && ((entity1.getX() > entity2.getX() && entity1.getX() < entity2.getX()+48) || (entity1.getX() + 48 > entity2.getX() && entity1.getX() + 48 < entity2.getX() + 48))) {
                entity1.setY(entity1.getY() - 2);
                return true;
            }
        } else if (entity1.direction.equals("left")) {
            if ((entity1.getX() < entity2.getX() + 48 && entity1.getX() > entity2.getX()) && ((entity1.getY() > entity2.getY() && entity1.getY() < entity2.getY() + 48) || (entity1.getY() + 48 > entity2.getY() && entity1.getY() + 48 < entity2.getY() + 48))) {
                entity1.setX(entity1.getX() + 2);
                return true;
            }
        } else if (entity1.direction.equals("right")) {
            if ((entity1.getX() + 48 > entity2.getX() && entity1.getX() + 48 < entity2.getX() + 48) && ((entity1.getY() > entity2.getY() && entity1.getY() < entity2.getY() + 48) || (entity1.getY() + 48 > entity2.getY() && entity1.getY() + 48 < entity2.getY() + 48))) {
                entity1.setX(entity1.getX() - 2);
                return true;
            }
        }
        return false;
    }
}
