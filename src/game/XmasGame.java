package game;

import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

/**
 * @author Daniel J. Holland
 * @version 1.0
 * Created on 12/23/2016.
 */
public class XmasGame extends BasicGame {

    private TiledMap xmasMap;
    private Animation spritePlayer, upPlayer, downPlayer, leftPlayer, rightPlayer;
    private float xPlayer, yPlayer;
    /** The collision map indicating which tiles block movement – generated based on tile blocked property */
    private boolean[][] blocked;
    private static final int SIZE = 64; // Tile size

    public XmasGame() {
        super("Xmas Game");


    }

    public static void main(String[] arguments) {
        try
        {
            AppGameContainer app = new AppGameContainer(new XmasGame());
            app.setDisplayMode(640, 640, false);
            app.start();
        }
        catch (SlickException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        xmasMap = new TiledMap("assets/maps/xmas_map_64x64.tmx");

        // Defining player animations
        String pImgDown = "assets/players/generic_player/player_down_64x64.png";
        String pImgUp = "assets/players/generic_player/player_up_64x64.png";
        String pImgLeft = "assets/players/generic_player/player_left_64x64.png";
        String pImgRight = "assets/players/generic_player/player_right_64x64.png";
        Image[] movementUp = {
                new Image(pImgUp),
                new Image(pImgUp)};
        Image [] movementDown = {
                new Image(pImgDown),
                new Image(pImgDown)};
        Image [] movementLeft = {
                new Image(pImgLeft),
                new Image(pImgLeft)};
        Image [] movementRight = {
                new Image(pImgRight),
                new Image(pImgRight)};
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

        // build a collision map based on tile properties in the TileD map
        blocked = new boolean[xmasMap.getWidth()][xmasMap.getHeight()];
        for (int xAxis=0; xAxis < xmasMap.getWidth(); xAxis++) {
            for (int yAxis=0; yAxis < xmasMap.getHeight(); yAxis++) {
                for (int layer = 0; layer < xmasMap.getLayerCount(); layer++) {
                    int tileID = xmasMap.getTileId(xAxis, yAxis, layer);
                    String value = xmasMap.getTileProperty(tileID, "blocked", "false");
                    if ("true".equals(value)) {
                        blocked[xAxis][yAxis] = true;
                    }
                }
            }
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException
    {
        Input input = container.getInput();

        float playerSpeed = 0.3f; // Higher values = faster player speed
        if (input.isKeyDown(Input.KEY_UP))
        {
            spritePlayer = upPlayer;
            if (!isBlocked(xPlayer, yPlayer - delta * playerSpeed))
            {
                spritePlayer.update(delta);
                // The lower the delta the slowest the sprite will animate.
                yPlayer -= delta * playerSpeed;
            }
        }
        else if (input.isKeyDown(Input.KEY_DOWN))
        {
            spritePlayer = downPlayer;
            if (!isBlocked(xPlayer, yPlayer + SIZE + delta * playerSpeed))
            {
                spritePlayer.update(delta);
                yPlayer += delta * playerSpeed;
            }
        }
        else if (input.isKeyDown(Input.KEY_LEFT))
        {
            spritePlayer = leftPlayer;
            if (!isBlocked(xPlayer - delta * playerSpeed, yPlayer))
            {
                spritePlayer.update(delta);
                xPlayer -= delta * playerSpeed;
            }
        }
        else if (input.isKeyDown(Input.KEY_RIGHT))
        {
            spritePlayer = rightPlayer;
            if (!isBlocked(xPlayer + SIZE + delta * playerSpeed, yPlayer))
            {
                spritePlayer.update(delta);
                xPlayer += delta * playerSpeed;
            }
        }
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        // Rendering the ground layer (last parameter is layer index)
        xmasMap.render(0, 0, 0);
        // Rendering Player
        spritePlayer.draw((int)xPlayer, (int)yPlayer);
        //Rendering foliage layer overtop the player
        xmasMap.render(0,0, 1);
    }

    private boolean isBlocked(float x, float y)
    {
        int xBlock = (int)x / SIZE;
        int yBlock = (int)y / SIZE;
        return blocked[xBlock][yBlock];
    }

}