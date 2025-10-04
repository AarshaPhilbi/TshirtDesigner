package com.tshirt.designApp;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageElement extends GraphicElement {
    private BufferedImage image;
    private String imagePath;

    public ImageElement(double x, double y, double width, double height, BufferedImage image) {
        super(x, y, width, height);
        this.image = image;
        if (image != null) {
            // Set default size based on image dimensions, maintaining aspect ratio
            double aspectRatio = (double) image.getHeight() / image.getWidth();
            this.bounds.setRect(x, y, width, width * aspectRatio);
        }
    }

    public ImageElement(double x, double y, BufferedImage image) {
        this(x, y, image.getWidth(), image.getHeight(), image);
    }

    @Override
    public void draw(Graphics2D g) {
        if (!visible || image == null) return;
        
        g.drawImage(image, (int)position.getX(), (int)position.getY(), 
                   (int)bounds.getWidth(), (int)bounds.getHeight(), null);
    }

    @Override
    public boolean contains(Point point) {
        return bounds.contains(point.getX(), point.getY());
    }

    public BufferedImage getImage() { return image; }
    public void setImage(BufferedImage image) { this.image = image; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}