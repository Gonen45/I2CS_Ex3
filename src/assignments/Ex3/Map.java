package assignments.Ex3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D {
	private int[][] _map;
	private boolean _cyclicFlag = true;
	
	/**
	 * Constructs a w*h 2D raster map with an init value v.
	 * @param w
	 * @param h
	 * @param v
	 */
	public Map(int w, int h, int v) {init(w,h, v);}
	/**
	 * Constructs a square map (size*size).
	 * @param size
	 */
	public Map(int size) {this(size,size, 0);}
	
	/**
	 * Constructs a map from a given 2D array.
	 * @param data
	 */
	public Map(int[][] data) {
		init(data);
	}
	@Override
	public void init(int w, int h, int v) {
		/////// add your code below ///////
        this._map = new int[h][w];

        for(int i = 0; i < h; ++i) {
            for(int j = 0; j < w; ++j) {
                this._map[i][j] = v;
            }
        }
		///////////////////////////////////
	}
	@Override
	public void init(int[][] arr) {
		/////// add your code below ///////
        if (arr != null && arr.length != 0) {
            int h = arr.length;
            if (arr[0].length <= 0) {
                throw new RuntimeException("w most be larger then 0");
            } else {
                int w = arr[0].length;
                this._map = new int[h][w];

                for(int i = 0; i < h; ++i) {
                    if (arr[i].length != w) {
                        throw new RuntimeException("Array must have same length");
                    }

                    for(int j = 0; j < w; ++j) {
                        this._map[i][j] = arr[i][j];
                    }
                }

            }
        } else {
            throw new RuntimeException("array cannot be null or with h=0 ");
        }
		///////////////////////////////////
	}
	@Override
	public int[][] getMap() {
		/////// add your code below ///////
        int h = this._map.length;
        int w = this._map[0].length;
        int[][] ans = new int[h][w];

        for(int r = 0; r < h; ++r) {
            for(int c = 0; c < w; ++c) {
                ans[r][c] = this._map[r][c];
            }
        }
		///////////////////////////////////
		return ans;
	}
	@Override
	/////// add your code below ///////
	public int getWidth() {
        int ans = this._map[0].length;
        return ans;
    }
	@Override
	/////// add your code below ///////
	public int getHeight() {
        int ans = this._map.length;
        return ans;
    }
	@Override
	/////// add your code below ///////
	public int getPixel(int x, int y) {
        Index2D p = new Index2D(x, y);
        if (this.isInside(p)) {
            int ans = this._map[y][x];
            return ans;
        } else {
            throw new RuntimeException("x or y out of dim");
        }}
	@Override
	/////// add your code below ///////
	public int getPixel(Pixel2D p) {
		return this.getPixel(p.getX(),p.getY());
	}
	@Override
	/////// add your code below ///////
	public void setPixel(int x, int y, int v) {
        Index2D p = new Index2D(x, y);
        if (this.isInside(p)) {
            this._map[y][x] = v;
        } else {
            throw new RuntimeException("x or y out of dim");
        }
    }
	@Override
	/////// add your code below ///////
	public void setPixel(Pixel2D p, int v) {
        int x = p.getX();
        int y = p.getY();
        if (this.isInside(p)) {
            this._map[y][x] = v;
        } else {
            throw new RuntimeException("x or y out of dim");
        }
	}
	@Override
	/** 
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 */
	public int fill(Pixel2D xy, int new_v) {
		int ans=0;
        int counter=0;
        boolean cyclic= _cyclicFlag;
		/////// add your code below ///////

        if (!this.isInside(xy)) {
            throw new RuntimeException("xy out of bounds");
        } else {
            int color = this.getPixel(xy);
            if (color == new_v) {
                return 0;
            } else {
                int r = this.getHeight();
                int c = this.getWidth();
                int[][] dirct = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
                ArrayDeque<Index2D> q = new ArrayDeque();
                q.add((Index2D)xy);

                while(!q.isEmpty()) {
                    Index2D curr = (Index2D)q.removeFirst();

                    for(int[] d : dirct) {
                        int nx = curr.getX() + d[0];
                        int ny = curr.getY() + d[1];
                        Index2D n_curr = new Index2D(nx, ny);
                        if (cyclic) {
                            if (nx == c) {
                                nx = 0;
                            } else if (nx == -1) {
                                nx = c - 1;
                            }

                            if (ny == r) {
                                ny = 0;
                            } else if (ny == -1) {
                                ny = r - 1;
                            }

                            n_curr.change(nx, ny);
                        } else if (!this.isInside(n_curr)) {
                            continue;
                        }

                        if (this.getPixel(n_curr) == color) {
                            this.setPixel(n_curr, new_v);
                            q.add(n_curr);
                            counter++;
                        }
                    }
                }

                return counter;
            }
        }

		///////////////////////////////////
    }

	@Override
	/**
	 * BFS like shortest the computation based on iterative raster implementation of BFS, see:
	 * https://en.wikipedia.org/wiki/Breadth-first_search
	 */
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
		Pixel2D[] ans = null;  // the result.
		/////// add your code below ///////
        if (this.isInside(p1) && this.isInside(p2)) {
            if (p1.equals(p2)) {
                return new Pixel2D[]{p1};
            } else {
                int r = this.getHeight();
                int c = this.getWidth();
                int ex = p2.getX();
                int ey = p2.getY();
                Pixel2D[][] parents = new Pixel2D[r][c];
                boolean[][] visited = new boolean[r][c];
                int[][] dirct = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
                ArrayDeque<Index2D> q = new ArrayDeque();
                boolean found = false;
                q.add(new Index2D(p1));
                visited[p1.getY()][p1.getX()] = true;
                parents[p1.getY()][p1.getX()] = null;

                while(!q.isEmpty()) {
                    Index2D curr = (Index2D)q.removeFirst();
                    if (curr.equals(p2)) {
                        found = true;
                        break;
                    }

                    for(int[] d : dirct) {
                        int nx = curr.getX() + d[0];
                        int ny = curr.getY() + d[1];
                        Index2D n_curr = new Index2D(nx, ny);
                        if (_cyclicFlag) {
                            if (nx == c) {
                                nx = 0;
                            } else if (nx == -1) {
                                nx = c - 1;
                            }

                            if (ny == r) {
                                ny = 0;
                            } else if (ny == -1) {
                                ny = r - 1;
                            }

                            n_curr.change(nx, ny);
                        } else if (nx < 0 || nx >= c || ny < 0 || ny >= r) {
                            continue;
                        }

                        if (!visited[ny][nx] && this.getPixel(n_curr) != obsColor) {
                            visited[ny][nx] = true;
                            parents[ny][nx] = curr;
                            q.add(n_curr);
                        }
                    }
                }

                if (!found) {
                    return null;
                } else {
                    ArrayList<Index2D> paths = new ArrayList();

                    for(Index2D current = new Index2D(p2); !current.equals(p1); current = new Index2D(parents[current.getY()][current.getX()])) {
                        paths.add(current);
                    }

                    paths.add((Index2D)p1);
                    Collections.reverse(paths);
                    ans = (Pixel2D[])paths.toArray(new Pixel2D[0]);
                    return ans;
                }
            }
        } else {
            throw new RuntimeException("p1 or p2 out of bounds");
        }
        ///////////////////////////////////
	}
	@Override
	/////// add your code below ///////
	public boolean isInside(Pixel2D p) {
        boolean c1 = p.getY() < this.getHeight() && p.getX() < this.getWidth();
        boolean c2 = 0 <= p.getY() && 0 <= p.getX();
        return c1 && c2;	}

	@Override
	/////// add your code below ///////
	public boolean isCyclic() {
		return _cyclicFlag;
	}
	@Override
	/////// add your code below ///////
	public void setCyclic(boolean cy) {
        _cyclicFlag=cy;
    }
	@Override
	/////// add your code below ///////
	public Map2D allDistance(Pixel2D start, int obsColor) {
		/////// add your code below ///////
        Map ans = new Map(this.getMap());
        ans.paddMap2D(obsColor);
        if (!this.isInside(start)) {
            throw new RuntimeException("start point out of bounds");
        } else {
            int r = ans.getHeight();
            int c = ans.getWidth();
            int[][] dirct = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            ArrayDeque<Index2D> q = new ArrayDeque();
            q.add((Index2D)start);
            ans.setPixel(start, 0);

            while(!q.isEmpty()) {
                Index2D curr = (Index2D)q.removeFirst();
                int curr_color = ans.getPixel(curr);

                for(int[] d : dirct) {
                    int nx = curr.getX() + d[0];
                    int ny = curr.getY() + d[1];
                    Index2D n_curr = new Index2D(nx, ny);
                    if (_cyclicFlag) {
                        if (nx == c) {
                            nx = 0;
                        } else if (nx == -1) {
                            nx = c - 1;
                        }

                        if (ny == r) {
                            ny = 0;
                        } else if (ny == -1) {
                            ny = r - 1;
                        }

                        n_curr.change(nx, ny);
                    } else if (!ans.isInside(n_curr)) {
                        continue;
                    }

                    if (ans.getPixel(n_curr) == -1) {
                        q.add(n_curr);
                        ans.setPixel(n_curr, curr_color + 1);
                    }
                }
            }

            ///////////////////////////////////
		return ans;
	}

}


private void paddMap2D(int v) {
    for(int r = 0; r < this.getHeight(); ++r) {
        for(int c = 0; c < this.getWidth(); ++c) {
            if (this.getPixel(c, r) != v) {
                this.setPixel(c, r, -1);
            }
        }
    }

}
}