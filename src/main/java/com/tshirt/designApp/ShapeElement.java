package com.tshirt.designApp;

import java.awt.*;

public class ShapeElement extends GraphicElement {
    private String shapeType; // "rectangle", "circle", "triangle"
    private Color fillColor;

    public ShapeElement(double x, double y, double width, double height, String shapeType) {
        super(x, y, width, height);
        this.shapeType = shapeType;
        this.fillColor = Color.RED; // Default
    }

    @Override
    public void draw(Graphics2D g) {
        if (!visible) return;
        
        g.setColor(fillColor);
        switch (shapeType.toLowerCase()) {
            case "rectangle":
                g.fillRect((int)position.getX(), (int)position.getY(), 
                          (int)bounds.getWidth(), (int)bounds.getHeight());
                break;
            case "circle":
                g.fillOval((int)position.getX(), (int)position.getY(), 
                          (int)bounds.getWidth(), (int)bounds.getHeight());
                break;
            case "triangle":
                int[] xPoints = {(int)position.getX(), 
                               (int)(position.getX() + bounds.getWidth()/2), 
                               (int)(position.getX() + bounds.getWidth())};
                int[] yPoints = {(int)(position.getY() + bounds.getHeight()), 
                               (int)position.getY(), 
                               (int)(position.getY() + bounds.getHeight())};
                g.fillPolygon(xPoints, yPoints, 3);
                break;
        }
        
        // Draw border
        if (borderWidth > 0) {
            g.setColor(borderColor);
            g.setStroke(new BasicStroke((float)borderWidth));
            switch (shapeType.toLowerCase()) {
                case "rectangle":
                    g.drawRect((int)position.getX(), (int)position.getY(), 
                              (int)bounds.getWidth(), (int)bounds.getHeight());
                    break;
                case "circle":
                    g.drawOval((int)position.getX(), (int)position.getY(), 
                              (int)bounds.getWidth(), (int)bounds.getHeight());
                    break;
            }
        }
    }

    @Override
    public boolean contains(Point point) {
        return bounds.contains(point.getX(), point.getY());
    }

    public String getShapeType() { return shapeType; }
    public void setShapeType(String shapeType) { this.shapeType = shapeType; }
    public Color getFillColor() { return fillColor; }
    public void setFillColor(Color fillColor) { this.fillColor = fillColor; }
}