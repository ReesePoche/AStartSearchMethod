/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc4444project1;

import java.util.Scanner;



/**
 *
 * @author Reese
 */
public class CSC4444Project1 {

    /**
     * The following implements A* Search method on a given problem.
     * You have a robot vacuum cleaner that could move or suck up dirt that has
     * a utility function. 
     * State of the vacuum and cleaning area is given by RobotState.java
     * The A* Search is implemented in the ASearchTree.java
     * The following code is a sample run with the robot starting in position 4
     * and having the squares 0, 1, and 2 be dirty
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Scanner kb = new Scanner(System.in);
        System.out.println("Hello about to run the simulation given on HW worksheet");
        int[] ds = {0, 1, 2};
        RobotState state0 = new RobotState(4, ds);
        ASearchTree search1 = new ASearchTree(state0);
        System.out.print("The first huristic function exspanded a total of " + search1.solve() + " nodes\nHere are the following steps with costs\n");
        String[] path = search1.getPath();
        int[] cost = search1.getCosts();
        for(int i = 0; i < cost.length; i++){
            System.out.println("Step " + i + " action: " + path[i] + " cost so far: " + cost[i]);
        }
        ASearchTree search2 = new ASearchTree(state0);
        System.out.print("The second huristic function exspanded a total of " + search2.solveH2() + " nodes\nHere are the following steps with costs\n");
        path = search2.getPath();
        cost = search2.getCosts();
        for(int i = 0; i < cost.length; i++){
            System.out.println("Step " + i + " action: " + path[i] + " cost so far: " + cost[i]);
        }
    }
        
        
        
        
    }
    
