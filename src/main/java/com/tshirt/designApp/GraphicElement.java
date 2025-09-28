package com.tshirt.designApp;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;

// This is the abstract base class for all drawable elements
public abstract class GraphicElement {
    protected Point2D.Double position;
    protected Rectangle2D.Double bounds;
    protected Color fillColor;
    protected Color borderColor;
    protected double borderWidth;

    // Constructor
    public GraphicElement(double x, double y, double width, double height) {
        this.position = new Point2D.Double(x, y);
        this.bounds = new Rectangle2D.Double(x, y, width, height);
        this.fillColor = Color.BLACK; // Default color
        this.borderColor = Color.BLACK;
        this.borderWidth = 1.0;
    }

    // Core methods - these will be implemented by subclasses
    public abstract void move(double newX, double newY);
    public abstract void resize(double newWidth, double newHeight);

    // Getters and Setters
    public Point2D.Double getPosition() { return position; }
    public Rectangle2D.Double getBounds() { return bounds; }
    public Color getFillColor() { return fillColor; }
    public void setFillColor(Color fillColor) { this.fillColor = fillColor; }
    public Color getBorderColor() { return borderColor; }
    public void setBorderColor(Color borderColor) { this.borderColor = borderColor; }
    public double getBorderWidth() { return borderWidth; }
    public void setBorderWidth(double borderWidth) { this.borderWidth = borderWidth; }
}