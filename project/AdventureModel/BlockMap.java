package AdventureModel;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/*
* Responsible for holding infmoration about the visuals of the map and rooms
 */
public class BlockMap {
    Canvas canvas; //canvas that the blocks should be drawn onto
    public int[][] blockLoc = new int[17][13]; // 16x12 grid with x and y coordinates of where to draw the block

    HashMap<String, ArrayList<Object>> blocks = new HashMap<>(); // name of block : list containing its image and whether collision is on

    public BlockMap(Canvas canvas) {
        this.canvas = canvas;
        setupBlocks();
        readMap("Resources/Rooms/StartingRoom.txt");
    }

    /*
    * Populate blocks with all the information about the blocks that need to be drawn onto the screen
     */
    public void setupBlocks() {
        // Each list contains an image of the block and if collision is enabled
        ArrayList<Object> nullBlock = new ArrayList<>(); // null block
        nullBlock.add(1);
        nullBlock.add(true);
        ArrayList<Object> floor = new ArrayList<>();
        floor.add(new Image("file:Resources/Blocks/Floor.png"));
        floor.add(false);
        ArrayList<Object> bottomWall = new ArrayList<>();
        bottomWall.add(new Image("file:Resources/Blocks/Bottom_Wall.png"));
        bottomWall.add(true);
        ArrayList<Object> topWall = new ArrayList<>();
        topWall.add(new Image("file:Resources/Blocks/Top_Wall.png"));
        topWall.add(true);
        blocks.put("0", nullBlock);
        blocks.put("1", floor);
        blocks.put("2", bottomWall);
        blocks.put("3", topWall);
    }

    /*
    * Read the map file and populate blockLoc with information about what block needs to be drawn at what x and y
    * coordinate on screen
     */
    public void readMap(String mapFile) {
        try {
            InputStream stream = new FileInputStream(mapFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            int col = 0;
            int row = 0;
            while (col < 17 && row < 13) {
                String line = bufferedReader.readLine();
                while (col < 17) {
                    String[] numbers = line.split(" ");
                    blockLoc[col][row] = Integer.parseInt(numbers[col]);
                    col++;
                }
                if (col == 17) {
                    col = 0;
                    row++;
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * Draw the blocks onto the screen using information from blocksLoc
     */
    public void draw(GraphicsContext gc, String roomName) {
        gc.clearRect(0,0, 768, 576);
        readMap("Resources/Rooms/" + roomName.replaceAll(" ", "") + ".txt");
        int col = 0,x = 0;
        int row = 0, y = 0;
        while (col < 16 && row < 12) {
            int tileNum = blockLoc[col][row];
            gc.drawImage((Image) blocks.get(Integer.toString(tileNum)).get(0), x, y, 48, 48);
            col++;
            x += 48;
            if (col == 16) {
                col = 0;
                x = 0;
                row++;
                y += 48;
            }
        }
    }
}
