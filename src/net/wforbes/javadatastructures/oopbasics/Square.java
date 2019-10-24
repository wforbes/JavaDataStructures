package net.wforbes.javadatastructures.oopbasics;

public class Square extends Rectangle{

    private double sideLength;
    public Square(double sideLength){
        super(sideLength, sideLength);
        this.sideLength = sideLength;
    }
}