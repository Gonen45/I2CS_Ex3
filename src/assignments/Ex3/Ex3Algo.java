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
		_count++;
		int dir = randomDir();
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

    private Index2D moveToPoint(PacmanGame game) {
        Index2D p= null;
        String pos = game.getPos(0);
        int[] xy= posToInt(pos);
        p = new Index2D(xy[0],xy[1]);
        Map realmap= new Map(game.getGame(0));
        Map d_map = (Map) realmap.allDistance(p,Game.getIntColor(Color.BLUE, 0));
//

         return p;
    }

    private ArrayList<Index2D> findMinIntersection(Map real_map , Map dis_map, int color){
        ArrayList<Index2D> points = new ArrayList<>();
        int min= Integer.MAX_VALUE;
        for(int r = 0; r < real_map.getHeight(); ++r) {
            for(int c = 0; c < real_map.getWidth(); ++c) {
            if(real_map.getPixel(c,r)==color){
                int dis=dis_map.getPixel(c,r);
                if(dis<=min){
                    if(dis<min){
                        points.clear();
                    }
                    points.add(new Index2D(c,r));
                    min=dis;
                }
              }
            }
        }
        return points;
    }

    private static int moveToDir(Index2D new_point, PacmanGame game) {
        int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
        Map m= new Map(game.getGame(0));
        String pos = game.getPos(0);
        int[] xy= posToInt(pos);
        Index2D p = new Index2D(xy[0],xy[1]);
        Pixel2D[] arr = m.shortestPath(p,new_point,Game.getIntColor(Color.BLUE, 0));
        int ind = dirToPoint(p, (Index2D) arr[0]);
        return dirs[ind];
    }

    private static int dirToPoint(Index2D pos, Index2D destntion){
//        works based on {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT}
        int sum_p = pos.getX() + pos.getY();
        int sum_step = destntion.getX() + destntion.getY();
        if(sum_p<sum_step){
            if(pos.getX() == destntion.getX()){return 0;}
            else{return 3;}
        }
        else{
            if(pos.getX() == destntion.getX()){return 2;}
            else{return 1;}
        }
    }

}



