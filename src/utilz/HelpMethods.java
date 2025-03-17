package utilz;

import main.Game;

import java.awt.geom.Rectangle2D;

public class HelpMethods {

    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvldata) {
        if (!IsSolid(x, y, lvldata)) {
            if (!IsSolid(x + width, y + height, lvldata)) {
                if (!IsSolid(x + width, y, lvldata)) {
                    if (!IsSolid(x, y + height, lvldata)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean IsSolid(float x, float y, int[][] lvldata) {

        int maxWidth = lvldata[0].length * Game.TILE_SIZE;
        if (x < 0 || x >= maxWidth || y < 0 || y >= Game.GAME_HEIGHT) {
            return true;
        }

        float xIndex = x / Game.TILE_SIZE;
        float yIndex = y / Game.TILE_SIZE;

        int value = lvldata[(int) yIndex][(int) xIndex];
        if (value != 11) {
            return true;
        }

        return false;
    }

    public static boolean IsFLoor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvLData) {
        return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 2, lvLData);
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {

        int currTile = (int) hitbox.x / Game.TILE_SIZE;
        //Right
        if (xSpeed > 0) {
            int tileXPOS = currTile * Game.TILE_SIZE;
            int xOffset = (int) (Game.TILE_SIZE - hitbox.width);
            return tileXPOS + xOffset - 1;
        } else {//Left
            return currTile * Game.TILE_SIZE;
        }


    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {

        int currTile = (int) hitbox.y / Game.TILE_SIZE;
        //falling
        if (airSpeed > 0) {
            int tileYPOS = currTile * Game.TILE_SIZE;
            int yOffset = (int) (Game.TILE_SIZE - hitbox.height);
            return tileYPOS + yOffset - 1;
        } else {//jumping
            return currTile * Game.TILE_SIZE;
        }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvldata) {

        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 2, lvldata)) {
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 2, lvldata)) {
                return false;
            }
        }
        return true;
    }

    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int tileY) {

        int firstXTile = (int) firstHitbox.x / Game.TILE_SIZE;
        int secondXTile = (int) secondHitbox.x / Game.TILE_SIZE;

        if (firstXTile > secondXTile) {
            return IsAllTileWalkable(firstXTile, secondXTile, tileY, lvlData) && IsAllTileWalkable_(firstXTile, secondXTile, tileY + 1, lvlData);
        } else {
            return IsAllTileWalkable(secondXTile, firstXTile, tileY, lvlData) && IsAllTileWalkable_(secondXTile, firstXTile, tileY + 1, lvlData);
        }


    }

    public static boolean IsAllTileWalkable(int xEnd, int xStart, int y, int[][] lvlData) {

        for (int i = 0; i < xEnd - xStart; i++) {
            if (IsTileSolid(xStart + i, y, lvlData)) {
                return false;
            }
        }
        return true;
    }

    public static boolean IsAllTileWalkable_(int xEnd, int xStart, int y, int[][] lvlData) {

        for (int i = 0; i < xEnd - xStart; i++) {
            if (!IsTileSolid(xStart + i, y, lvlData)) {
                return false;
            }

        }

        return true;
    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvldata) {
        int value = lvldata[yTile][xTile];


        if (value != 11) {
            return true;
        }

        return false;
    }
}
