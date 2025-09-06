package com.tshirt.designApp;

import java.awt.Color;

public class Shape extends GraphicElement {
    private String shapeType; // "rectangle", "circle", etc.

    public Shape(double x, double y, double width, double height, String shapeType) {
        super(x, y, width, height);
        this.shapeType = shapeType;
    }

    @Override
    public void move(double newX, double newY) {
        this.position.setLocation(newX, newY);
        this.bounds.setRect(newX, newY, bounds.getWidth(), bounds.getHeight());
    }

    @Override
    public void resize(double newWidth, double newHeight) {
        this.bounds.setRect(position.getX(), position.getY(), newWidth, newHeight);
    }

    public String getShapeType() { return shapeType; }
    public void setShapeType(String shapeType) { this.shapeType = shapeType; }
}