package battle_royale;
import java.util.Scanner;
import java.io.File;
public class Battle_royale {
    //size of the map
    static int size;
    //holds all possible paths for 1 position of P
    static String[] paths;
    //holds the best paths from all positions of P
    static String[] bestPaths;
    //holds a 2d array of the map
    static String[][] map;
    
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
        String[] arr = path.split(" ");
            map[Integer.parseInt(arr[arr.length-1].substring(0,arr[arr.length-1].indexOf(".")))][Integer.parseInt(arr[arr.length-1].substring(arr[arr.length-1].indexOf(".") + 1))] = "P";
        for (int i = 0; i < arr.length - 1; i++) {
            int x = Integer.parseInt(arr[i].substring(0,arr[i].indexOf(".")));
            int y = Integer.parseInt(arr[i].substring(arr[i].indexOf(".") + 1));
            if (x == size/2 && y == size/2) {
                map[x][y] = "F";
            } else {
                map[x][y] = "V";
            }
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
        int time;
        if (map[x][y].equals("P")) { //path is legal
            //adds path to an array of all possible paths
            paths[nextEmpty(paths)] = path + Integer.toString(findP(map, size)[0]) + "." + Integer.toString(findP(map, size)[1]);
        } else {
            //add current coordinate to the path
            path += x + "." + y + " ";
            //get the number at the location, corresponding to time taken AND value of loot
            if (map[x][y].equals("P") || map[x][y].equals(".") || map[x][y].equals("F")) {
                time = 1;
            } else {
                time = Integer.parseInt(map[x][y]);
            }
            //decrement amount of moves left depending on the time taken
            max -= time;
            //if the time limit was exceeded earlier, it will end this branch of recursion as the move is no longer legal
            if (max < 0) {
                return;
            }
            //set current coordinate to visited
            isVisited[x][y] = true;
            //recursively go every possible direction (legal and not visited already)
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
    
    /**
     * selects best (most loot) path out of the array of all legal paths
     * @param map the 2d array holding the map
     * @param arr the array of paths
     * @return a string containing the path coordinates AND the loot collected
     */
    public static String findOptimalPath(String[][] map, String[] arr) {
        int loot = 0;
        String[] coordinates;
        String optimal = "";
        for (int i = 0; i < arr.length && arr[i] != null; i++) {
            int temp = 0;
            coordinates = arr[i].split(" ");
            for (int j = 0; j < coordinates.length; j++) {
                temp += value(map,coordinates[j]);
            }
            
            if (temp >= loot) {
                loot = temp;
                optimal = arr[i] + " " + loot;
            }
        }
        return optimal;
    }                                                
    
    /**
     * finds the loot value of the coordinate location on the map
     * @param map a 2d array of the map
     * @param coord a coordinate in the form "x.y"
     * @return the integer value of the coordinate
     */
    public static int value(String[][] map, String coord) {
        int x = Integer.parseInt(coord.substring(0,coord.indexOf(".")));
        int y = Integer.parseInt(coord.substring(coord.indexOf(".") + 1));
        if (map[x][y].equals("P") || map[x][y].equals(".") || map[x][y].equals("F")) {
            return 0;
        } else {
            return Integer.parseInt(map[x][y]);
        }
    }
    
    /**
     * helper method to find the next empty space in an array because I can't use array lists or stacks or queues :((((
     * @param list the array containing all legal paths
     * @return the index of the next empty space, -1 if it doesn't exist
     */
    public static int nextEmpty(String[] list) {
        for (int i = 0; i < list.length; i++) {
            if (list[i] == null) {
                return i;
            } else if (list[i].length() <= 1) {
                return i;
            }
        }
        return -1;
    }
    
    public static void main(String[] args){
        //initialize variables
        int max = 0;
        Scanner sc = new Scanner(System.in);
        //ask for file name
        System.out.println("What is the name of the file?");
        String name = sc.next();
        File file = new File(name);
        
        //get size of map from file
        try {
            Scanner fileSize = new Scanner(file);
            size = fileSize.nextLine().length() / 2 + 1;
            max = size/2;
            //paths = new String[4+(3*(size/2-1))];
            paths = new String[100000];
            bestPaths = new String[size*size];
            map = new String[size][size];
            fileSize.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        
        long startT = System.nanoTime();
        
        //begin program to run through all locations, in order to find the best placement for P
        for (int k = 0; k < size; k++) {
            for (int l = 0; l < size; l++) {
                //create an array to hold all visited values
                boolean[][] isVisited = new boolean[size][size];
                //fill boolean array with 'false'
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        isVisited[i][j] = false;
                    }
                }

                //file IO, scan to 2d array
                try {
                    Scanner fileIn = new Scanner(file);
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            map[i][j] = fileIn.next();
                        }
                    }

                    //if the current point is not a number, set the point to the player (cannot drop on loot)
                    if (map[k][l].equals(".") || map[k][l].equals("F")) {
                        //set previous "P" to "."
                        if (findP(map, size) != null) {
                            map[findP(map, size)[0]][findP(map, size)[1]] = ".";
                        }
                        //set current position to "P"
                        map[k][l] = "P";
                        //find all paths
                        opPath(max,max,map,isVisited,size,"",max);
                        //set most optimal path as the "winner" of the current player position
                        bestPaths[nextEmpty(bestPaths)] = findOptimalPath(map, paths);
                        //clear array of paths to be used in next iteration
                        for (int i = 0; i < paths.length; i++) {
                            paths[i] = null;
                        }
                    }
                } catch (Exception e) { //catch all exceptions
                    //print exceptions
                    System.out.println(e);
                }
            }
        }
        //find the path with the best loot
        int maxLoot = 0;
        String bestPath = "";
        for (int i = 0; i < bestPaths.length; i++) {
            if (bestPaths[i] != null && bestPaths[i].length() > 1) {
                String lootString = bestPaths[i].substring(bestPaths[i].lastIndexOf(" ") + 1);
                if (!lootString.equals("") && !lootString.equals(" ") && Integer.parseInt(lootString) >= maxLoot) {
                    maxLoot = Integer.parseInt(lootString);
                    bestPath = bestPaths[i];
                }
            }
        }
        
        //Remove the final "P"
        if (findP(map, size) != null) {
            map[findP(map, size)[0]][findP(map, size)[1]] = ".";
        }
        
        long endT = System.nanoTime();
        
        //Find starting coordinate
        String startCoord = bestPath.substring(0,bestPath.lastIndexOf(" "));
        //Output starting location, loot and map
        System.out.println("The best starting location is" + startCoord.substring(startCoord.lastIndexOf(" ")));
        System.out.println("It will yield " + maxLoot + " piece(s) of loot");
        System.out.println("Below is a diagram of the path it takes");
        setV(map, bestPath.substring(0,bestPath.lastIndexOf(" ")));
        printA(map, size);
        System.out.println((endT - startT)/1000000000.0);
    }
}
