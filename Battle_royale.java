/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battle_royale;

/**
 *
 * @author kenj8
 */

import java.util.Scanner;
import java.io.File;
public class Battle_royale {
    /**
     * Prints the entire array
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
     * Helper method to find the location of "P"
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
     * Shortest distance from the center to "P"
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
            map[x][y] = "V";
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
    
    public static void main(String[] args) throws Exception{
        int size = 11;
        File file = new File("map.txt");
        Scanner fileIn = new Scanner(file);
        String[][] map = new String[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = fileIn.next();
            }
        }
        
        printA(map, size);
        System.out.println();
        System.out.println("The player collected " + path(map, 5, 5, size) + " piece(s) of loot");
        printA(map, size);
    }
    
}
