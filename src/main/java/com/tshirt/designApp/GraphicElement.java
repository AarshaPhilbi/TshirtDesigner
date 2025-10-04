package com.tshirt.designApp;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

// This is your CORE abstract base class - the single source of truth
public abstract class GraphicElement {
    protected Point2D.Double position;
    protected Rectangle2D.Double bounds;
    protected Color fillColor;
    protected Color borderColor;
    protected double borderWidth;
    protected String elementId;
    protected boolean visible;

    // Constructor
    public GraphicElement(double x, double y, double width, double height) {
        this.position = new Point2D.Double(x, y);
        this.bounds = new Rectangle2D.Double(x, y, width, height);
        this.fillColor = Color.BLACK;
        this.borderColor = Color.BLACK;
        this.borderWidth = 1.0;
        this.elementId = generateId();
        this.visible = true;
    }

    // Core methods - these will be implemented by subclasses
    public abstract void draw(Graphics2D g);
    public abstract boolean contains(Point point);
    
    // Common implementation for move and resize
    public void move(double deltaX, double deltaY) {
        this.position.setLocation(position.getX() + deltaX, position.getY() + deltaY);
        this.bounds.setRect(position.getX(), position.getY(), bounds.getWidth(), bounds.getHeight());
    }

    public void resize(double newX, double newY, double newWidth, double newHeight) {
        if (newWidth > 10 && newHeight > 10) {
            this.position.setLocation(newX, newY);
            this.bounds.setRect(newX, newY, newWidth, newHeight);
        }
    }

    // Utility method
    private String generateId() {
        return "elem_" + System.currentTimeMillis() + "_" + hashCode();
    }

    // Getters and Setters
    public Point2D.Double getPosition() { return position; }
    public Rectangle2D.Double getBounds() { return bounds; }
    public Rectangle getBoundsAsRectangle() { 
        return new Rectangle((int)bounds.getX(), (int)bounds.getY(), 
                           (int)bounds.getWidth(), (int)bounds.getHeight()); 
    }
    public Color getFillColor() { return fillColor; }
    public void setFillColor(Color fillColor) { this.fillColor = fillColor; }
    public Color getBorderColor() { return borderColor; }
    public void setBorderColor(Color borderColor) { this.borderColor = borderColor; }
    public double getBorderWidth() { return borderWidth; }
    public void setBorderWidth(double borderWidth) { this.borderWidth = borderWidth; }
    public String getElementId() { return elementId; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
}