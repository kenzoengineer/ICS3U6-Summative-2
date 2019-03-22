package Battle_royale;
import java.util.Scanner;
import java.io.File;
import java.util.Arrays;
public class Battle_royale {
    static String[] paths = new String[1000];
    
    /**
     * prints the entire array
     * @param map the 2d array of the map
     * @param size the size of the 2d array
     */
    public static void printA(String[][] map,int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    /**
     * helper method to find the location of "P"
     * @param map the 2d array of the map
     * @param size the size of the 2d array
     * @return the x and y coordinates of "P"
     */
    public static int[] findP(String[][] map, int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (map[i][j].equals("P")) {
                    int[] a = {i,j};
                    return a;
                }
            }
        }
        return null;
    }
    
    /**
     * shortest distance from the center to "P"
     * @param map the size of the map
     * @param x the current x value of the path
     * @param y the current y value of the path
     * @param size the size of the array
     * @return the number of loot tiles collected
     */
    public static int path(String[][] map, int x, int y, int size) {
        int pX = findP(map,size)[0];
        int pY = findP(map,size)[1];
        if (map[x][y].equals("P")) {
            return 0;
        } else {
            //map[x][y] = "V";
            if (x > pX) {
                if (map[x-1][y].equals("1")) {
                    return 1 + path(map,x-1,y,size);
                } else {
                    return path(map,x-1,y,size);
                }
            } else if (x < pX) {
                if (map[x+1][y].equals("1")) {
                    return 1 + path(map,x+1,y,size);
                } else {
                    return path(map,x+1,y,size);
                }
            } else if (y > pY) {
                if (map[x][y-1].equals("1")) {
                    return 1 + path(map,x,y-1,size);
                } else {
                    return path(map,x,y-1,size);
                }
            } else {
                if (map[x][y+1].equals("1")) {
                    return 1 + path(map,x,y+1,size);
                } else {
                    return path(map,x,y+1,size);
                }
            }
        }
    }
    
    /**
     * checks if the index has already been visited
     * @param x [x][] index
     * @param y [][y] index
     * @param isVisited 2d boolean array the size of the map, indicating whether the index was visited
     * @return true or false depending on if it is visited
     */
    public static boolean checkVisit(int x, int y, boolean[][] isVisited) {
        return isVisited[x][y];
    }
    
    /**
     * sets the path to a series of "V"s
     * @param map the 2d array of the map
     * @param path the string containing the coordinates of the path
     */
    public static void setV(String[][] map, String path) {
        for (int i = 0; i < path.length() - 1; i += 4) {
            int x = Integer.parseInt(path.substring(i,i+1));
            int y = Integer.parseInt(path.substring(i+2,i+3));
            map[x][y] = "V";
        }
    }
    
    /**
     * recursively finds all legal paths from center to the player without taking into account loot
     * @param x the current x index
     * @param y the current y index
     * @param map the 2d array of the map
     * @param isVisited a 2d boolean array which holds values whether the index has been visited
     * @param size the lengthwise size of the map
     * @param path a string which holds the path taken
     * @param max the maximum number of moves possible for the player to make, before the storm makes it impossible (floor(size/2))
     */
    public static void opPath(int x, int y, String[][] map, boolean[][] isVisited, int size, String path, int max) {
        if (map[x][y].equals("P")) {
            paths[nextEmpty(paths)] = path + Integer.toString(findP(map, size)[0]) + "." + Integer.toString(findP(map, size)[1]);
        } else if (max > -1) {
            path += x + "." + y + " ";
            max--;
            isVisited[x][y] = true;
            if (x + 1 < size && !isVisited[x+1][y]) {
                opPath(x + 1, y, map, copyArrayB(isVisited, size), size, path, max);
            }
            if (x - 1 >= 0 && !isVisited[x-1][y]) {
                opPath(x - 1, y, map, copyArrayB(isVisited, size), size, path, max);
            }
            if (y + 1 < size && !isVisited[x][y+1]) {
                opPath(x, y + 1, map, copyArrayB(isVisited, size), size, path, max);
            }
            if (y - 1 >= 0 && !isVisited[x][y-1]) {
                opPath(x, y - 1, map, copyArrayB(isVisited, size), size, path, max);
            }
        }
    }
    
    /**
     * copy helper method for booleans
     * @param old parent array to be copied
     * @param size size of the array
     * @return child, copied array
     */
    public static boolean[][] copyArrayB(boolean[][] old, int size) {
        boolean[][] copy = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                copy[i][j] = old[i][j];
            }
        }
        return copy;
    }
    
    /**
     * copy helper method for strings
     * @param old parent array to be copied
     * @param size size of the array
     * @return child, copied array
     */
    public static String[][] copyArrayS(String[][] old, int size) {
        String[][] copy = new String[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                copy[i][j] = old[i][j];
            }
        }
        return copy;
    }
    
    public static String findOptimalPath(String[][] map, String[] arr) {
        int loot = 0;
        String[] coordinates = new String[map[0].length + 1];
        String optimal = "";
        for (int i = 0; i < arr.length && arr[i] != null; i++) {
            int temp = 0;
            coordinates = arr[i].split(" ");
            for (int j = 0; j < coordinates.length; j++) {
                temp += value(map,coordinates[j]);
            }
            if (temp > loot) {
                loot = temp;
                optimal = arr[i] + " " + loot;
            }
        }
        return optimal;
    }                                                
    
    public static int value(String[][] map, String coord) {
        int x = Integer.parseInt(coord.substring(0,1));
        int y = Integer.parseInt(coord.substring(2));
        try {
            return Integer.parseInt(map[x][y]);
        } catch (Exception e) { //if its not a number
            return 0;
        }
    }
    
    public static int nextEmpty(String[] list) {
        for (int i = 0; i < list.length; i++) {
            if (list[i] == null) {
                return i;
            }
        }
        return -1;
    }
    public static void main(String[] args) throws Exception{
        int size = 11;
        int max = size/2;
        boolean[][] isVisited = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                isVisited[i][j] = false;
            }
        }
        
        File file = new File("map.txt");
        Scanner fileIn = new Scanner(file);
        String[][] map = new String[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = fileIn.next();
            }
        }
        
        printA(map, size);
        System.out.println("\n");
        System.out.println("P is at " + Arrays.toString(findP(map,size)));
        opPath(max,max,map,isVisited,size,"",max);
        String optimalPath = findOptimalPath(map, paths);
        System.out.println(optimalPath);
        //TODO:
        //Set 1000 to something math
    }
}
