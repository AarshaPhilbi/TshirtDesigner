package com.tshirt.designApp;

public class Layer {
    private GraphicElement content;
    private boolean visible;
    private String name;
    private int zIndex; // For layer ordering

    public Layer(GraphicElement content, String name) {
        this.content = content;
        this.name = name;
        this.visible = true;
        this.zIndex = 0;
    }

    // Delegation methods to the content
    public void moveContent(double deltaX, double deltaY) {
        content.move(deltaX, deltaY);
    }

    public void resizeContent(double newX, double newY, double newWidth, double newHeight) {
        content.resize(newX, newY, newWidth, newHeight);
    }

    public void draw(java.awt.Graphics2D g) {
        if (visible && content.isVisible()) {
            content.draw(g);
        }
    }

    public boolean contains(java.awt.Point point) {
        return content.contains(point);
    }

    // Getters and Setters
    public GraphicElement getContent() { return content; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getZIndex() { return zIndex; }
    public void setZIndex(int zIndex) { this.zIndex = zIndex; }
}