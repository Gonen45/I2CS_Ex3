package assignments.Ex3;

import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

import java.awt.*;
import java.util.ArrayList;

/**
 * This is the major algorithmic class for Ex3 - the PacMan game:
 *
 * This code is a very simple example (random-walk algorithm).
 * Your task is to implement (here) your PacMan algorithm.
 */
public class Ex3Algo implements PacManAlgo{
	private int _count;
	public Ex3Algo() {_count=0;}
	@Override
	/**
	 *  Add a short description for the algorithm as a String.
	 */
	public String getInfo() {
		return null;
	}
	@Override
	/**
	 * This ia the main method - that you should design, implement and test.
	 */
	public int move(PacmanGame game) {
		if(_count==0 || _count==300) {
			int code = 0;
			int[][] board = game.getGame(0);
			printBoard(board);
			int blue = Game.getIntColor(Color.BLUE, code);
			int pink = Game.getIntColor(Color.PINK, code);
			int black = Game.getIntColor(Color.BLACK, code);
			int green = Game.getIntColor(Color.GREEN, code);
			System.out.println("Blue=" + blue + ", Pink=" + pink + ", Black=" + black + ", Green=" + green);
			String pos = game.getPos(code).toString();
			System.out.println("Pacman coordinate: "+pos);
			GhostCL[] ghosts = game.getGhosts(code);
			printGhosts(ghosts);
			int up = Game.UP, left = Game.LEFT, down = Game.DOWN, right = Game.RIGHT;
		}
		_count++;//
		int dir = moveToPoint(game,Game.getIntColor(Color.PINK, 0));//randomDir();
		return dir;
	}
	private static void printBoard(int[][] b) {
		for(int y =0;y<b[0].length;y++){
			for(int x =0;x<b.length;x++){
				int v = b[x][y];
				System.out.print(v+"\t");
			}
			System.out.println();
		}
	}
	private static void printGhosts(GhostCL[] gs) {
		for(int i=0;i<gs.length;i++){
			GhostCL g = gs[i];
			System.out.println(i+") status: "+g.getStatus()+",  type: "+g.getType()+",  pos: "+g.getPos(0)+",  time: "+g.remainTimeAsEatable(0));
		}
	}
	private static int randomDir() {
		int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
		int ind = (int)(Math.random()*dirs.length);
		return dirs[ind];
	}


    private Map ghostsMap(PacmanGame game) {
        Map m= new Map(game.getGame(0));
        GhostCL[] gs = game.getGhosts(0);
        Map2D[] maps = new Map2D[gs.length];
        for (int i = 0; i < gs.length; i++) {
            GhostCL g = gs[i];
            int[] xy= posToInt(g.getPos(0));
            Index2D p = new Index2D(xy[0],xy[1]);
            maps[i] = m.allDistance(p,Game.getIntColor(Color.BLUE, 0));
        }
        Map map= new Map(m.getMap());
        int[] dis =new int[gs.length];
        for(int r = 0; r < map.getHeight(); ++r) {
            for(int c = 0; c < map.getWidth(); ++c) {

                for(int i = 0; i < gs.length; i++){
                    dis[i]=maps[i].getPixel(c,r);
                }
                int v = Integer.MAX_VALUE;
                for (int d : dis) {
                    if (d < v) {
                        v = d;
                    }
                }
                map.setPixel(c,r,v);

            }
        }
        return map;
    }




    // Helper to convert coordinate string to Point object or int array
    private static int[] posToInt(String pos) {
        String[] parts = pos.replace("(", "")
                .replace(")", "")
                .split(",");
        int x = Integer.parseInt(parts[0].trim());
        int y = Integer.parseInt(parts[1].trim());
        return new int[] {x, y};
    }

//    private Index2D currentTarget = null;


    private int moveToPoint(PacmanGame game,int color) {
//

        Index2D p = null;
        String pos = game.getPos(0);
        int[] xy = posToInt(pos);
        p = new Index2D(xy[0], xy[1]);

        Map realmap = new Map(game.getGame(0));
        Map d_map = (Map) realmap.allDistance(p, Game.getIntColor(Color.BLUE, 0));

        ArrayList<Index2D> points = findMinIntersection( d_map, realmap, color);

        // Calculate best point
        Index2D n_p = choseBestPoint(points, game);

        if (n_p == null) {
            System.out.println("randooooom "+ points.isEmpty());
            return randomDir();
        }

        System.out.println("NOT RANDOM " + p.toString() + " " + n_p.toString() );
        return moveToDir(n_p, game);
    }

//
private ArrayList<Index2D> findMinIntersection(Map2D distMap, Map2D colorMap, int targetColor) {
    ArrayList<Index2D> closestPoints = new ArrayList<>();
    int minDistance = Integer.MAX_VALUE;
    for (int x = 0; x < colorMap.getWidth(); x++) {
        for (int y = 0; y < colorMap.getHeight(); y++) {
            if (colorMap.getPixel(x, y) == targetColor) {
                int dist = distMap.getPixel(x, y);
                if (dist >= 0) {
                    if (dist < minDistance) {
                        minDistance = dist;
                        closestPoints.clear();
                        closestPoints.add(new Index2D(x, y));
                    } else if (dist == minDistance) {
                        closestPoints.add(new Index2D(x, y));
                    }
                }
            }
        }
    }
//
//
    return closestPoints;
}


//
private static int moveToDir(Index2D target, PacmanGame game) {
    Map m = new Map(game.getGame(0));
    String pos = game.getPos(0);
    int[] xy = posToInt(pos);
    Index2D current = new Index2D(xy[0], xy[1]);

    int wallColor = Game.getIntColor(Color.BLUE, 0);
    Pixel2D[] path = m.shortestPath(current, target, wallColor);

    if (path == null || path.length < 2) {
        return randomDir();
    }

    Index2D next = (Index2D) path[1];
    int directionIndex = dirToPoint(current, next);

    int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
    return dirs[directionIndex];
}



    private static int dirToPoint(Index2D pos, Index2D dest){
//        works based on {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT}
        if (dest.getY() < pos.getY()) return 0;
        if (dest.getY() > pos.getY()) return 2;
        if (dest.getX() < pos.getX()) return 1;
        else return 3;
    }


    private Index2D choseBestPoint(ArrayList<Index2D> candidates, PacmanGame game) {
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }

        Map ghostDanger = ghostsMap(game);
        Index2D best = candidates.get(0);
        int bestSafety = ghostDanger.getPixel(best.getX(), best.getY());

        for (Index2D p : candidates) {
            int safety = ghostDanger.getPixel(p.getX(), p.getY());
            if ((safety > bestSafety) && bestSafety<8) { // higher = safer
                bestSafety = safety;
                best = p;
            }
        }
        return best;
    }

//

}



