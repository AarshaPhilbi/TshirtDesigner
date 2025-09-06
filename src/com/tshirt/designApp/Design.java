package com.tshirt.designApp;

import java.util.ArrayList;
import java.util.List;

public class Design {
    private List<Layer> layers;
    private double width;
    private double height;
    private String backgroundColor;

    public Design(double width, double height) {
        this.width = width;
        this.height = height;
        this.layers = new ArrayList<>();
        this.backgroundColor = "#FFFFFF"; // White default
    }

    // CREATE: Add a new layer
    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    // DELETE: Remove a layer by index
    public void deleteLayer(int index) {
        if (index >= 0 && index < layers.size()) {
            layers.remove(index);
        }
    }

    // MOVE: Move a specific layer's content
    public void moveLayer(int index, double newX, double newY) {
        if (index >= 0 && index < layers.size()) {
            layers.get(index).moveContent(newX, newY);
        }
    }

    // RESIZE: Resize a specific layer's content
    public void resizeLayer(int index, double newWidth, double newHeight) {
        if (index >= 0 && index < layers.size()) {
            layers.get(index).resizeContent(newWidth, newHeight);
        }
    }

    // GET all layers (for Anne's GUI)
    public List<Layer> getLayers() {
        return new ArrayList<>(layers); // Return a copy for safety
    }

    // Getters and Setters
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }
}