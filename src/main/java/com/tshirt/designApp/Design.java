package com.tshirt.designApp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Design {
    private List<Layer> layers;
    private double width;
    private double height;
    private String backgroundColor;
    private String designName;

    public Design(double width, double height) {
        this.width = width;
        this.height = height;
        this.layers = new ArrayList<>();
        this.backgroundColor = "#FFFFFF";
        this.designName = "Untitled Design";
    }

    // Core CRUD operations
    public void addLayer(Layer layer) {
        layers.add(layer);
        updateLayerIndices();
    }

    public void removeLayer(int index) {
        if (index >= 0 && index < layers.size()) {
            layers.remove(index);
            updateLayerIndices();
        }
    }

    public void removeLayer(Layer layer) {
        layers.remove(layer);
        updateLayerIndices();
    }

    public void moveLayer(int fromIndex, int toIndex) {
        if (fromIndex >= 0 && fromIndex < layers.size() && 
            toIndex >= 0 && toIndex < layers.size()) {
            Layer layer = layers.remove(fromIndex);
            layers.add(toIndex, layer);
            updateLayerIndices();
        }
    }

    private void updateLayerIndices() {
        for (int i = 0; i < layers.size(); i++) {
            layers.get(i).setZIndex(i);
        }
    }

    // Selection and query methods
    public Layer getLayerAtPoint(java.awt.Point point) {
        for (int i = layers.size() - 1; i >= 0; i--) {
            Layer layer = layers.get(i);
            if (layer.isVisible() && layer.contains(point)) {
                return layer;
            }
        }
        return null;
    }

    public List<Layer> getVisibleLayers() {
        return layers.stream()
                .filter(Layer::isVisible)
                .collect(Collectors.toList());
    }

    // GET all layers (for Anne's GUI)
    public List<Layer> getLayers() {
        return new ArrayList<>(layers);
    }

    // Getters and Setters
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }
    public String getDesignName() { return designName; }
    public void setDesignName(String designName) { this.designName = designName; }
    public int getLayerCount() { return layers.size(); }
}