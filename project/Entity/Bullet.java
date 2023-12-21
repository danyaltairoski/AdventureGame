package Entity;

import javafx.scene.canvas.GraphicsContext;

/*
* Bullet that the entity can shoot
 */
public class Bullet extends Entity {
    public Bullet(Entity entity) {
        setX(entity.getX());
        setY(entity.getY());
        if (entity.direction.equals("idle")) {
            direction = "up";
        } else {
            direction = entity.direction;
        }
    }

    /*
    * Update its location
     */
    public void update() {
        if (direction.equals("up") && !(getY() < -10)) {
            setY(getY() - 4);
        } else if (direction.equals("left") && !(getX() < -10)) {
            setX(getX() - 4);
        } else if (direction.equals("down") && !(getY() > 576)) {
            setY(getY() + 4);
        } else if (direction.equals("right") && !(getX() > 768)) {
            setX(getX() + 4);
        }
    }

    public void draw(GraphicsContext gc) {
        gc.fillRect(getX(), getY(), 10, 10);
    }
}
