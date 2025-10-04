package com.tshirt.designApp;

import com.tshirt.designApp.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Adapter class that bridges the core Design classes with Anne's GUI
 * This allows Anne to use your core classes without modifying her GUI code
 */
public class DesignAdapter {
    private Design coreDesign;
    
    public DesignAdapter() {
        this.coreDesign = new Design(800, 800);
    }
    
    // Convert Anne's GUI elements to your core elements
    public void addTextElement(String text, int x, int y) {
        TextElement textElement = new TextElement(x, y, text);
        Layer layer = new Layer(textElement, "Text: " + text);
        coreDesign.addLayer(layer);
    }
    
    public void addImageElement(BufferedImage image, int x, int y) {
        ImageElement imageElement = new ImageElement(x, y, image);
        Layer layer = new Layer(imageElement, "Image");
        coreDesign.addLayer(layer);
    }
    
    public void addShapeElement(String shapeType, int x, int y, int width, int height, Color color) {
        ShapeElement shapeElement = new ShapeElement(x, y, width, height, shapeType);
        shapeElement.setFillColor(color);
        Layer layer = new Layer(shapeElement, "Shape: " + shapeType);
        coreDesign.addLayer(layer);
    }
    
    // Get all elements for drawing
    public List<Layer> getLayersForDrawing() {
        return coreDesign.getLayers();
    }
    
    // Selection methods
    public Layer selectElementAt(Point point) {
        return coreDesign.getLayerAtPoint(point);
    }
    
    // Layer management
    public void bringForward(Layer layer) {
        int currentIndex = coreDesign.getLayers().indexOf(layer);
        if (currentIndex < coreDesign.getLayerCount() - 1) {
            coreDesign.moveLayer(currentIndex, currentIndex + 1);
        }
    }
    
    public void sendBackward(Layer layer) {
        int currentIndex = coreDesign.getLayers().indexOf(layer);
        if (currentIndex > 0) {
            coreDesign.moveLayer(currentIndex, currentIndex - 1);
        }
    }
    
    public void deleteLayer(Layer layer) {
        coreDesign.removeLayer(layer);
    }
    
    // Get the core design for export functionality
    public Design getCoreDesign() {
        return coreDesign;
    }
}