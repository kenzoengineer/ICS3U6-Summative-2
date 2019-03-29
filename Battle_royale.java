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
     * sets the path to a series of "V"s
     * @param map the 2d array of the map
     * @param path the string containing the coordinates of the path
     */
    public static void setV(String[][] map, String path, String start) {
        String[] arr = path.split(" ");
        map[Integer.parseInt(start.substring(0,start.indexOf(",")))][Integer.parseInt(start.substring(start.indexOf(",") + 1))] = "P";
        for (int i = 0; i < arr.length - 1; i++) {
            int x = Integer.parseInt(arr[i].substring(0,arr[i].indexOf(",")));
            int y = Integer.parseInt(arr[i].substring(arr[i].indexOf(",") + 1));
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
     * @param looted a 2d boolean array which holds whether the position is looted
     * @param size the lengthwise size of the map
     * @param path a string which holds the path taken
     * @param max the maximum number of moves possible for the player to make, before the storm makes it impossible (floor(size/2))
     */
    public static void opPath(int x, int y, String[][] map, boolean[][] looted, int size, String path, int max, int loot) {
        int time;
        if (map[x][y].equals("P")) { //path is legal
            //adds path to an array of all possible paths
            paths[nextEmpty(paths)] = path + x + "," + y + " #" + loot;
        } else {
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
            //if the time limit was exceeded earlier, it will end this branch of recursion as the move is no longer legal
            if (max < 0) {
                return;
            }
            //set current coordinate to visited
            looted[x][y] = true;
            //recursively go every possible direction (legal and not visited already)
            if (x + 1 < size) {
                opPath(x + 1, y, map, copyArrayB(looted, size), size, path, max,loot);
            }
            if (x - 1 >= 0) {
                opPath(x - 1, y, map, copyArrayB(looted, size), size, path, max,loot);
            }
            if (y + 1 < size) {
                opPath(x, y + 1, map, copyArrayB(looted, size), size, path, max,loot);
            }
            if (y - 1 >= 0) {
                opPath(x, y - 1, map, copyArrayB(looted, size), size, path, max,loot);
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
    public static String findOptimalPath(String[] arr) {
        int loot = 0;
        int hash;
        String path = "";
        for (int i = 0; i < arr.length && arr[i] != null; i++) {
            hash = arr[i].indexOf("#");
            int temp = Integer.parseInt(arr[i].substring(hash + 1));
            if (temp > loot) {
                loot = temp;
                path = arr[i] + "";
            }
        }
        return path;
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
            Scanner fileIn = new Scanner(file);
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
        max = size/2;
        paths = new String[100000];
        bestPaths = new String[size*size];
        long startT = System.nanoTime();
        
        //begin program to run through all locations, in order to find the best placement for P
        for (int k = 0; k < size; k++) {
            for (int l = 0; l < size; l++) {
                //if the current point is not a number, set the point to the player (cannot drop on loot)
                if ((map[k][l].equals(".") || map[k][l].equals("F")) && (Math.abs(k-max)+Math.abs(l-max)) <= max) {
                    System.out.println(k + " " + l);
                    //create an array to hold all looted values
                    boolean[][] looted = new boolean[size][size];
                    //fill boolean array with 'false'
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
                    //find all paths
                    opPath(max,max,map,looted,size,"",max,0);
                    //set most optimal path as the "winner" of the current player position
                    bestPaths[nextEmpty(bestPaths)] = findOptimalPath(paths);
                    //clear array of paths to be used in next iteration
                    for (int i = 0; i < paths.length; i++) {
                        paths[i] = null;
                    }
                }
            }
        }
        //find which path is the best
        int mostLoot = -1;
        String bestPath = "";
        for (int i = 0; i < nextEmpty(bestPaths); i++) {
            int loot = Integer.parseInt(bestPaths[i].substring(bestPaths[i].indexOf("#") + 1));
            if (loot > mostLoot) {
                mostLoot = loot;
                bestPath = bestPaths[i];
            }
        }
        if (bestPath.length() > 1) {
            if (findP(map, size) != null) {
                map[findP(map, size)[0]][findP(map, size)[1]] = ".";
            }
            String[] arr = bestPath.split(" ");
            System.out.println("The best starting location is " + arr[arr.length - 2]);
            long endT = System.nanoTime();
            System.out.println("This program took " + (endT - startT) / 1000000000.0 + " s");
            setV(map, bestPath.substring(0,bestPath.indexOf("#")), arr[arr.length - 2]);
            printA(map, size);
        } else {
            System.out.println("There are no possible paths. you doomed lol");
        }
    }
}
