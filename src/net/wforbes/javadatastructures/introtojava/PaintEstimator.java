package net.wforbes.javadatastructures.introtojava;

import java.util.Scanner;
import java.lang.Math;     // Note: Needed for math functions in part (3)

public class PaintEstimator {
    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        double wallHeight;
        double wallWidth;
        double wallArea;
        final double wallPerGallon = 350.0;

        System.out.println("Enter wall height (feet):");
        wallHeight = scnr.nextDouble();

        //wallWidth = 10.0;                      // FIXME (1): Prompt user to input wall's width
        System.out.println("Enter wall width (feet):");
        wallWidth = scnr.nextDouble();

        // Calculate and output wall area
        //wallArea = 0.0;                        // FIXME (1): Calculate the wall's area
        wallArea = wallHeight * wallWidth;
        System.out.println("Wall area: "+wallArea+" square feet");     // FIXME (1): Finish the output statement

        // FIXME (2): Calculate and output the amount of paint in gallons needed to paint the wall
        double paintNeeded = wallArea / wallPerGallon;
        System.out.println("Paint needed: " + paintNeeded + " gallons");

        // FIXME (3): Calculate and output the number of 1 gallon cans needed to paint the wall, rounded up to nearest integer
        int cans = (int)Math.ceil(paintNeeded);
        System.out.println("Cans needed: " + cans + " can(s)");
    }
}