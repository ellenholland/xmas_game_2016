package game;

import com.sun.org.apache.xerces.internal.impl.dv.xs.AnyURIDV;
import objects.Character;
import objects.ChasingCat;
import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.Map;

import static java.lang.Math.abs;

/**
 * @author Daniel J. Holland
 * @version 1.0
 * Created on 12/23/2016.
 */
public class XmasGame extends BasicGame {

    private TiledMap xmasMap;
    private Animation spritePlayer, upPlayer, downPlayer, leftPlayer, rightPlayer;
    private Animation spriteCat, upCat, downCat, leftCat, rightCat;
    private ArrayList<ChasingCat> catSwarm;
    private float xPlayer, yPlayer;
    private float xCat, yCat;
    /** The collision map indicating which tiles block movement – generated based on tile blocked property */
    private boolean[][] blocked, playerSpawn, catSpawn, goalBlock;
    private static final int SIZE = 64; // Tile size

    public XmasGame() {
        super("Xmas Game");
    }

    public static void main(String[] arguments) {
        try {
            AppGameContainer app = new AppGameContainer(new XmasGame());
            app.setDisplayMode(640, 640, false);
            app.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        // Defining game map asset
        xmasMap = new TiledMap("assets/maps/xmas_map_64x64.tmx");
        //xmasMap = new TiledMap("assets/maps/xmas_house_map_64x64.tmx");

        /* Defining player animations */
        String pImgDown = "assets/characters/generic_player/player_down_64x64.png";
        String pImgUp = "assets/characters/generic_player/player_up_64x64.png";
        String pImgLeft = "assets/characters/generic_player/player_left_64x64.png";
        String pImgRight = "assets/characters/generic_player/player_right_64x64.png";
        Image[] movementUp = {new Image(pImgUp), new Image(pImgUp)};
        Image [] movementDown = {new Image(pImgDown), new Image(pImgDown)};
        Image [] movementLeft = {new Image(pImgLeft), new Image(pImgLeft)};
        Image [] movementRight = {new Image(pImgRight), new Image(pImgRight)};
        int [] duration = {300, 300};

        upPlayer = new Animation(movementUp, duration, false);
        downPlayer = new Animation(movementDown, duration, false);
        leftPlayer = new Animation(movementLeft, duration, false);
        rightPlayer = new Animation(movementRight, duration, false);

        // Setting initial player position to right
        spritePlayer = rightPlayer;

        //Initializing player location
        xPlayer = 128f;
        yPlayer = 128f;
        //spawnPlayer

        /* Defining Cat Animations */
        Image[] movementUpCat = {
                new Image("assets/characters/zelda/zelda_up_1.png"),
                new Image("assets/characters/zelda/zelda_up_2.png"),
                new Image("assets/characters/zelda/zelda_up_3.png")};
        Image [] movementDownCat = {
                new Image("assets/characters/zelda/zelda_down_1.png"),
                new Image("assets/characters/zelda/zelda_down_2.png"),
                new Image("assets/characters/zelda/zelda_down_3.png")};
        Image [] movementLeftCat = {
                new Image("assets/characters/zelda/zelda_left_1.png"),
                new Image("assets/characters/zelda/zelda_left_2.png"),
                new Image("assets/characters/zelda/zelda_left_3.png")};
        Image [] movementRightCat = {
                new Image("assets/characters/zelda/zelda_right_1.png"),
                new Image("assets/characters/zelda/zelda_right_2.png"),
                new Image("assets/characters/zelda/zelda_right_3.png")};
        int [] durationCat = {100, 100, 100};

        upCat = new Animation(movementUpCat, durationCat, false);
        downCat = new Animation(movementDownCat, durationCat, false);
        leftCat = new Animation(movementLeftCat, durationCat, false);
        rightCat= new Animation(movementRightCat, durationCat, false);

        // Generating cat swarm
        catSwarm = new ArrayList<>();
        int totalCats = 4; // Total number of cats on screen a the same time
        for (int i = 0; i < totalCats; i++){
            catSwarm.add(new ChasingCat(upCat, downCat, leftCat, rightCat));
        }

        // building collision and game maps based on tile properties in the TileD map
        blocked = new boolean[xmasMap.getWidth()][xmasMap.getHeight()];
        catSpawn = new boolean[xmasMap.getWidth()][xmasMap.getHeight()];
        playerSpawn = new boolean[xmasMap.getWidth()][xmasMap.getHeight()];
        goalBlock = new boolean[xmasMap.getWidth()][xmasMap.getHeight()];
        for (int xAxis=0; xAxis < xmasMap.getWidth(); xAxis++) {
            for (int yAxis=0; yAxis < xmasMap.getHeight(); yAxis++) {
                // Getting spawn points and goal blocks
                int tileID = xmasMap.getTileId(xAxis, yAxis, MapLayers.GAME.getValue());
                String value = xmasMap.getTileProperty(tileID, "playerSpawn", "false");
                if ("true".equals(value)) {
                    playerSpawn[xAxis][yAxis] = true;
                }
                value = xmasMap.getTileProperty(tileID, "catSpawn", "false");
                if ("true".equals(value)) {
                    catSpawn[xAxis][yAxis] = true;
                }
                value = xmasMap.getTileProperty(tileID, "goal", "false");
                if ("true".equals(value)) {
                    goalBlock[xAxis][yAxis] = true;
                }
                // Building collision map
                for (MapLayers layer : MapLayers.values()){
                    tileID = xmasMap.getTileId(xAxis, yAxis, layer.getValue());
                    value = xmasMap.getTileProperty(tileID, "blocked", "false");
                    if ("true".equals(value)) {
                        blocked[xAxis][yAxis] = true;
                    }
                }
            }
        }

    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        Input input = container.getInput();
        spawnCats();

        /* Defining Player Motion */
        float playerSpeed = 0.3f; // Higher values = faster player speed
        if (input.isKeyDown(Input.KEY_UP)) {
            spritePlayer = upPlayer;
            if (!isBlocked(xPlayer, yPlayer - delta * playerSpeed)) {
                spritePlayer.update(delta);
                // The lower the delta the slowest the sprite will animate.
                yPlayer -= delta * playerSpeed;
            }
        }
        else if (input.isKeyDown(Input.KEY_DOWN)) {
            spritePlayer = downPlayer;
            if (!isBlocked(xPlayer, yPlayer + SIZE + delta * playerSpeed)) {
                spritePlayer.update(delta);
                yPlayer += delta * playerSpeed;
            }
        }
        else if (input.isKeyDown(Input.KEY_LEFT)) {
            spritePlayer = leftPlayer;
            if (!isBlocked(xPlayer - delta * playerSpeed, yPlayer)) {
                spritePlayer.update(delta);
                xPlayer -= delta * playerSpeed;
            }
        }
        else if (input.isKeyDown(Input.KEY_RIGHT)) {
            spritePlayer = rightPlayer;
            if (!isBlocked(xPlayer + SIZE + delta * playerSpeed, yPlayer)) {
                spritePlayer.update(delta);
                xPlayer += delta * playerSpeed;
            }
        }

        /* Updating CAT SWARM */
        for (ChasingCat cat : catSwarm){
            if (cat.isAlive()){
                chasingCatUpdate(cat, xPlayer, yPlayer, delta);
            }
        }

    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        // Rendering the ground layer (last parameter is layer index)
        xmasMap.render(0, 0, MapLayers.BACKGROUND.getValue());

        // Rendering Characters
        for (ChasingCat cat : catSwarm){
            if (cat.isAlive()){
                cat.draw();
            }
        }
        spritePlayer.draw(xPlayer, yPlayer);

        //Rendering foliage layer over-top of Characters
        xmasMap.render(0,0, MapLayers.FOREGROUND.getValue());
    }

    private boolean isBlocked(float x, float y)
    {
        int xBlock = (int)x / SIZE;
        int yBlock = (int)y / SIZE;
        return blocked[xBlock][yBlock];
    }

    private void spawnCats(){
        // Spawning Cats
        int catCount =  -1;
        for (ChasingCat cat : catSwarm){
            if (!cat.isAlive()){
                catCount ++;
            }
        }
        for (int xAxis = 0; xAxis < catSpawn[0].length ; xAxis++) {
            if (catCount < 0){
                break;
            }
            for (int yAxis = 0; yAxis < catSpawn.length; yAxis++) {
                if (catCount < 0){
                    break;
                }
                if (catSpawn[xAxis][yAxis]) {
                    if (!catSwarm.get(catCount).isAlive()) {
                        catSwarm.get(catCount).setAlive();
                        catSwarm.get(catCount).setPosition((xAxis) * 64, (yAxis) * 64);
                        catCount --;
                    }
                }
            }
        }

    }

    private void chasingCatUpdate(ChasingCat cat, float targetX, float targetY, int delta){
        /* Defining Cat Motion - Follows after target*/
        float catSpeed = 0.08f;
        // If cat has reached its target... kill it!
        if ((targetX + 64 > cat.getX() && targetX - 1 < cat.getX()) &&
            (targetY + 64 > cat.getY() && targetY - 1 < cat.getY()) ){
            cat.kill();
        }
        else {
            cat.update(delta);
            // When traveling diagonally maintain last animation direction
            if (abs(targetX - cat.getX()) + 1 > abs(targetY - cat.getY()) &&
                    abs(targetX - cat.getX()) - 1 < abs(targetY - cat.getY())) {
                if (abs(targetX - cat.getX()) > abs(targetY - cat.getY())) {
                    if (targetX > cat.getX() + 1) {
                        cat.setX(cat.getX() + delta * catSpeed);
                    } else {
                        cat.setX(cat.getX() - delta * catSpeed);
                    }
                }
                else {
                    if (targetY > cat.getY() + 1) {
                        cat.setY(cat.getY() + delta * catSpeed);
                    } else {
                        cat.setY(cat.getY() - delta * catSpeed);
                    }
                }
            }
            // Left right travel
            else if (abs(targetX - cat.getX()) > abs(targetY - cat.getY())) {
                if (targetX > cat.getX() + 1) {
                    cat.setAnimation(Character.AnimationDirection.RIGHT);
                    cat.setX(cat.getX() + delta * catSpeed);
                }
                else {
                    cat.setAnimation(Character.AnimationDirection.LEFT);
                    cat.setX(cat.getX() - delta * catSpeed);
                }
            }
            // Up down travel
            else {//if (abs(targetX-cat.getX()) < abs(targetY-yCat)) {
                if (targetY > cat.getY() + 1) {
                    cat.setAnimation(Character.AnimationDirection.DOWN);
                    cat.setY(cat.getY() + delta * catSpeed);
                }
                else {
                    cat.setAnimation(Character.AnimationDirection.UP);
                    cat.setY(cat.getY() - delta * catSpeed);
                }
            }
        }
    }

}