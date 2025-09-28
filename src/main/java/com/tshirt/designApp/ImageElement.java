package com.tshirt.designApp;

import java.awt.Color;

public class ImageElement extends GraphicElement {
    private String imagePath;

    public ImageElement(double x, double y, double width, double height, String imagePath) {
        super(x, y, width, height);
        this.imagePath = imagePath;
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

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}