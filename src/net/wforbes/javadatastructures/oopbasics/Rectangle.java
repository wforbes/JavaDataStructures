package net.wforbes.javadatastructures.oopbasics;

public class Rectangle extends Shape{

    private double length;
    private double width;
    public Rectangle(double length, double width){
        this.length = length;
        this.width = width;
        this.area = length * width;
        this.perimeter = (2*length) + (2*width);
    }
}