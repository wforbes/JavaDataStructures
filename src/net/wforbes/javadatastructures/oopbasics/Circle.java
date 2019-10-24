package net.wforbes.javadatastructures.oopbasics;

public class Circle extends Shape{

    private double radius;
    public Circle(double radius){
        this.area =  Math.PI * radius * radius;
        this.perimeter = 2 * Math.PI * radius;
    }
}