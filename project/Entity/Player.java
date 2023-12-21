package Entity;

import AdventureModel.AdventureObject;
import AdventureModel.Room;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class keeps track of the progress
 * of the player in the game.
 */
public class Player extends Entity implements Serializable {
    /**
     * The current room that the player is located in.
     */
    private Room currentRoom;
    private String name;
    public int health;

    public ArrayList<AdventureObject> inventory;

    /**
     * Adventure Game Player Constructor
     * ------------------------------------
     * @param currentRoom is the current room the player is in.
     */
    public Player(Room currentRoom) {
        this.inventory = new ArrayList<AdventureObject>();
        this.currentRoom = currentRoom;
        // Default position values
        x = 336;
        y = 336;
        direction = "idle";
        damage = 1;
        getPlayerImage();
        this.health = 3; //default player starting health
    }

    /**
     * takeObject
     * -----------
     * This method adds an object into players inventory if the object is present in
     * the room and returns true. If the object is not present in the room, the method
     * returns false.
     *
     * @param object name of the object to pick up
     * @return true if picked up, false otherwise
     */
    public boolean takeObject(String object){
        if(this.currentRoom.checkIfObjectInRoom(object)){
            AdventureObject object1 = this.currentRoom.getObject(object);
            this.currentRoom.removeGameObject(object1);
            this.addToInventory(object1);
            return true;
        } else {
            return false;
        }
    }


    /**
     * checkIfObjectInInventory
     * __________________________
     * This method checks to see if an object is in a player's inventory.
     *
     * @param s the name of the object
     * @return true if object is in inventory, false otherwise
     */
    public boolean checkIfObjectInInventory(String s) {
        for(int i = 0; i<this.inventory.size();i++){
            if(this.inventory.get(i).getName().equals(s)) return true;
        }
        return false;
    }


    /**
     * dropObject
     * -----------
     * This method drops an object in the players inventory and adds it to the room.
     * If the object is not in the inventory, the method does nothing.
     *
     * @param s name of the object to drop
     */
    public void dropObject(String s) {
        for(int i = 0; i<this.inventory.size();i++){
            if(this.inventory.get(i).getName().equals(s)) {
                this.currentRoom.addGameObject(this.inventory.get(i));
                this.inventory.remove(i);
            }
        }
    }

    /**
     * update
     * --------------
     * Update the x and y coordinates of the player based on which key is being pressed.
     * Update the health image that is to be displayed based on health variable.
     */
    public void update(boolean wPressed, boolean aPressed, boolean sPressed, boolean dPressed, GraphicsContext gc, int tileSize) {
        gc.clearRect(
                getX(),
                getY(),
                tileSize,
                tileSize
        );
        if (wPressed) {
            setY(getY() - 2);
            direction = "up";
        } else if (aPressed) {
            setX(getX() - 2);
            direction = "left";
        } else if (sPressed) {
            setY(getY() + 2);
            direction = "down";
        } else if (dPressed) {
            setX(getX() + 2);
            direction = "right";
        } else {
            direction = "idle";
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
    * Draw the player on the updated location
     */
    public void draw(GraphicsContext gc) {
        Image image = null;
        if (direction.equals("up")) {
            if (imageNum == 1) {
                if (attacking) {
                    image = attack_up_1;
                } else {
                    image = up_1;
                }
            } else {
                if (attacking) {
                    image = attack_up_2;
                } else {
                    image = up_2;
                }
            }
        } else if (direction.equals("left")) {
            if (imageNum == 1) {
                if (attacking) {
                    image = attack_left_1;
                } else {
                    image = left_1;
                }
            } else {
                if (attacking) {
                    image = attack_left_2;
                } else {
                    image = left_2;
                }
            }
        } else if (direction.equals("down")) {
            if (imageNum == 1) {
                if (attacking) {
                    image = attack_down_1;
                } else {
                    image = down_1;
                }
            } else {
                if (attacking) {
                    image = attack_down_2;
                } else {
                    image = down_2;
                }
            }
        } else if (direction.equals("right")) {
            if (imageNum == 1) {
                if (attacking) {
                    image = attack_right_1;
                } else {
                    image = right_1;
                }
            } else {
                if (attacking) {
                    image = attack_right_2;
                } else {
                    image = right_2;
                }
            }
        } else {
            if (imageNum == 1) {
                if (attacking) {
                    image = attack_right_1;
                } else {
                    image = idle_1;
                }
            } else {
                if (attacking) {
                    image = attack_right_2;
                } else {
                    image = idle_2;
                }
            }
        }
        gc.drawImage(image, this.x, this.y, 48, 48);

        /*
        draw a specific health image based on this.health to canvas.
         */
        if (this.health == 3){
            Image full_health = new Image("file:Resources/Hearts/fullHearts.jpg");
            gc.drawImage(full_health, 1, 1, 100, 47);
        } else if (this.health == 2){
            Image two_hearts = new Image("file:Resources/Hearts/twoHearts.jpg");
            gc.drawImage(two_hearts, 1, 1, 100, 47);
        } else if (this.health == 1){
            Image one_heart = new Image("file:Resources/Hearts/oneHeart.jpg");
            gc.drawImage(one_heart, 1, 1, 100, 47);
        } else if (this.health == 0){
            Image no_health = new Image("file:Resources/Hearts/noHearts.jpg");
            gc.drawImage(no_health, 1, 1, 100, 47);
        }
    }

    /**
     * getPlayerImage
     * -----------------
     * Set up the players images for the different movements
     */
    public void getPlayerImage() {
        idle_1 = new Image("file:Resources/Player/idle_1.png");
        idle_2 = new Image("file:Resources/Player/idle_2.png");
        up_1 = new Image("file:Resources/Player/up_1.png");
        up_2 = new Image("file:Resources/Player/up_2.png");
        left_1 = new Image("file:Resources/Player/left_1.png");
        left_2 = new Image("file:Resources/Player/left_2.png");
        down_1 = new Image("file:Resources/Player/down_1.png");
        down_2 = new Image("file:Resources/Player/down_2.png");
        right_1 = new Image("file:Resources/Player/right_1.png");
        right_2 = new Image("file:Resources/Player/right_2.png");
        attack_right_1 = new Image("file:Resources/Player/attack_right_1.png");
        attack_right_2 = new Image("file:Resources/Player/attack_right_2.png");
        attack_left_1 = new Image("file:Resources/Player/attack_left_1.png");
        attack_left_2 = new Image("file:Resources/Player/attack_left_2.png");
        attack_down_1 = new Image("file:Resources/Player/attack_down_1.png");
        attack_down_2 = new Image("file:Resources/Player/attack_down_2.png");
        attack_up_1 = new Image("file:Resources/Player/attack_up_1.png");
        attack_up_2 = new Image("file:Resources/Player/attack_up_2.png");
    }

    /**
     * setCurrentRoom
     * -----------------
     * Setter method for the current room attribute.
     *
     * @param currentRoom The location of the player in the game.
     */
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    /**
     * addToInventory
     * -----------------
     * This method adds an object to the inventory of the player.
     *
     * @param object Prop or object to be added to the inventory.
     */
    public void addToInventory(AdventureObject object) {
        this.inventory.add(object);
    }


    /**
     * getCurrentRoom
     * -------------------
     * Getter method for the current room attribute.
     *
     * @return current room of the player.
     */
    public Room getCurrentRoom() {
        return this.currentRoom;
    }

    /**
     * getInventory
     * ---------------
     * Getter method for string representation of inventory.
     *
     * @return ArrayList of names of items that the player has.
     */
    public ArrayList<String> getInventory() {
        ArrayList<String> objects = new ArrayList<>();
        for(int i=0;i<this.inventory.size();i++){
            objects.add(this.inventory.get(i).getName());
        }
        return objects;
    }

    public void setName(String name){ this.name = name;}
    public String getName(){ return this.name;}
}
