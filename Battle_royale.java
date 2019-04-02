import java.util.Scanner;
import java.io.File;

/**
 * BattleRoyale.java
 * @version 2.0
 * @author Ken Jiang
 * @since March 21, 2019
 * Finds an optimal location to "drop" in order to maximize loot collection, similar to a battle royale type game.
 */

public class BattleRoyale {
    //size of the map
    static int size;
    //current path 
    static String currentPath = "#-1";
    //holds all possible paths for 1 position of P
    static String[][] map;
    
    /**
     * printA
     * prints a 2d array
     * @param map the 2d array
     * @param size the size of the 2d array
     */
    public static void printA(String[][] arr,int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    /**
     * findP
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
        //returns null if no "P" is found
        return null;
    }
    
    /**
     * setV
     * sets the path to a series of "V"s
     * @param map the 2d array of the map
     * @param path the string containing the coordinates of the path
     * @param start is the starting coordinate
     */
    public static void setV(String[][] map, String path, String start) {
        //splits path into separate coordinates
        String[] arr = path.split(" ");
        //sets start as the starting coordinate, changing it to "P"
        String xCoord = start.substring(0,start.indexOf(","));
        String yCoord = start.substring(start.indexOf(",") + 1);
        map[Integer.parseInt(xCoord)][Integer.parseInt(yCoord)] = "P";
        for (int i = 0; i < arr.length - 1; i++) {
            //parses coordinates from the path string
            int x = Integer.parseInt(arr[i].substring(0,arr[i].indexOf(",")));
            int y = Integer.parseInt(arr[i].substring(arr[i].indexOf(",") + 1));
            //if the coordinate is the center, set it as "F" for finish
            if (x == size/2 && y == size/2) {
                map[x][y] = "F";
            } else {
                map[x][y] = "V";
            }
        }
    }
    
    /**
     * opPath (optimal path)
     * recursively finds all legal paths from center to the player without taking into account loot
     * @param x the current x index
     * @param y the current y index
     * @param map the 2d array of the map
     * @param looted a 2d boolean array which holds whether the position is looted
     * @param size the lengthwise size of the map
     * @param path a string which holds the path taken
     * @param max initialized the max number of moves possible, decrements every run
     */
    public static void opPath(int x, int y, String[][] map, boolean[][] looted, int size, String path, int max, int loot) {
        int time;
        if (map[x][y].equals("P")) { //path is legal
            //adds path to an array of all possible paths
            if (moreOptimal("#" + loot,currentPath)) {
                currentPath = path + x + "," + y + " #" + loot;
            }
        } else {
            //if from the current position, a path to the player is unreachable, end the program (optimization)
            if (!isPossible(x, y, max)) {
                return;
            }
            //add current coordinate to the path
            path += x + "," + y + " ";
            //get the number at the location, corresponding to time taken AND value of loot
            if (map[x][y].equals("P") || map[x][y].equals(".") || map[x][y].equals("F")) {
                time = 1;
            } else {
                time = Integer.parseInt(map[x][y]);
                if (!looted[x][y]) {
                    loot += time;
                }
            }
            //decrement amount of moves left depending on the time taken
            max -= time;
            //if the time limit was exceeded earlier, end the program (optimization)
            if (max < 0) {
                return;
            }
            //set current coordinate to visited
            looted[x][y] = true;
            //recursively go every possible direction (legal and not visited already)
            //down
            if (x + 1 < size) {
                opPath(x + 1, y, map, copyArrayB(looted, size), size, path, max,loot);
            }
            //up
            if (x - 1 >= 0) {
                opPath(x - 1, y, map, copyArrayB(looted, size), size, path, max,loot);
            }
            //right
            if (y + 1 < size) {
                opPath(x, y + 1, map, copyArrayB(looted, size), size, path, max,loot);
            }
            //left
            if (y - 1 >= 0) {
                opPath(x, y - 1, map, copyArrayB(looted, size), size, path, max,loot);
            }
        }
    }
    
    /**
     * copyArrayB
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
     * copyArrayS
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
    
     /**
     * isPossible
     * Checks whether a path is still possible from the current position. Mainly for optimization
     * @param x is the x coordinate of the current position 
     * @param y is the y coordinate of the current position
     * @param max is the maximum amount of moves left
     * @return true is a path is still possible and false if it is not
     */
    public static boolean isPossible(int x, int y, int max) {
        //find location of player
        int pX = findP(map,size)[0];
        int pY = findP(map,size)[1];
        return Math.abs(pX-x) + Math.abs(pY-y) <= max;
    }
    
    /**
     * moreOptimal
     * Returns whether a new path collects more loot than a previous
     * @param str1 next legal path
     * @param str2 current best path
     * @return true if string 1 is better than string 2
     */
    public static boolean moreOptimal(String str1, String str2) {
        int loot1 = Integer.parseInt(str1.substring(str1.indexOf("#") + 1));
        int loot2 = Integer.parseInt(str2.substring(str2.indexOf("#") + 1));
        if (loot1 >= loot2) {
            return true;
        }
        return false;
    }                                                
    
    public static void main(String[] args) {
        //initialize variables
        int max;
        Scanner sc = new Scanner(System.in);
        //ask for file name
        System.out.println("What is the name of the file?");
        File file = new File(sc.next());
        
        //get size of map from file
        try {
            Scanner fileSize = new Scanner(file);
            Scanner fileIn = new Scanner(file);
            //size of the map
            size = fileSize.nextLine().length() / 2 + 1;
            map = new String[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    map[i][j] = fileIn.next();
                }
            }
            fileIn.close();
            fileSize.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        //maximum amount of moves before the map closes
        max = size/2;
        //holds the current best path
        String currentBest = "#-1";
        //for debugging, measures how long the program takes to run
        long startT = System.nanoTime();
        
        //begin program to run through all locations [k,l], in order to find the best placement for P
        for (int k = 0; k < size; k++) {
            for (int l = 0; l < size; l++) {
                //if the current point is not a number, set the point to the player (cannot drop on loot)
                if ((map[k][l].equals(".") || map[k][l].equals("F")) && (Math.abs(k-max)+Math.abs(l-max)) <= max) {
                    //create an array to hold all looted values
                    boolean[][] looted = new boolean[size][size];
                    //fill boolean array with false
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            looted[i][j] = false;
                        }
                    }
                    //set previous "P" to "."
                    if (findP(map, size) != null) {
                        map[findP(map, size)[0]][findP(map, size)[1]] = ".";
                    }
                    //set current position to "P"
                    map[k][l] = "P";
                    //run pathfinding method
                    opPath(max,max,map,looted,size,"",max,0);
                    //if the current path is more efficient, set currentBest to the current path
                    if (moreOptimal(currentPath, currentBest)) {
                        currentBest = currentPath;
                    }
                }
            }
        }
        //check if there is a valid path
        if (currentBest.length() > 1) {
            //set the final position of "P" to "."
            if (findP(map, size) != null) {
                map[findP(map, size)[0]][findP(map, size)[1]] = ".";
            }
            //split the best path into an array in order to find the starting location
            String[] arr = currentBest.split(" ");
            System.out.println("The best starting location is " + arr[arr.length - 2]);
            System.out.println("The maximum amount of loot obtainable is " + currentBest.substring(currentBest.indexOf("#") + 1));
            long endT = System.nanoTime();
            //convert nanoseconds to seconds
            System.out.println("This program took " + (endT - startT) / 1000000000.0 + " s");
            //display the path with "P", "V" and "F"
            setV(map, currentBest.substring(0,currentBest.indexOf("#")), arr[arr.length - 2]);
            //output the map
            printA(map, size);
        } else {
            //if there are no possible paths
            System.out.println("There are no possible paths. You are doomed.");
        }
    }
}
