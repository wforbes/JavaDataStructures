package net.wforbes.javadatastructures.oopbasics;

abstract class Shape{
    protected double area;
    protected double perimeter;
    public double getArea(){
        return this.area;
    }
    public double getPerimeter(){
        return this.perimeter;
    }
}