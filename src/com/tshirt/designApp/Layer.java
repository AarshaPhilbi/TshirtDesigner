package com.tshirt.designApp;

public class Layer {
    private GraphicElement content;
    private boolean isVisible;
    private String name;

    public Layer(GraphicElement content, String name) {
        this.content = content;
        this.name = name;
        this.isVisible = true;
    }

    public void moveContent(double newX, double newY) {
        content.move(newX, newY);
    }

    public void resizeContent(double newWidth, double newHeight) {
        content.resize(newWidth, newHeight);
    }

    // Getters and Setters
    public GraphicElement getContent() { return content; }
    public boolean isVisible() { return isVisible; }
    public void setVisible(boolean visible) { isVisible = visible; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}