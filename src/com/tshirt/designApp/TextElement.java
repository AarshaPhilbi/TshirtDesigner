package com.tshirt.designApp;

import java.awt.Color;

public class TextElement extends GraphicElement {
    private String text;
    private String fontFamily;
    private int fontSize;

    public TextElement(double x, double y, String text) {
        super(x, y, 100, 40); // Default width and height for text
        this.text = text;
        this.fontFamily = "Arial";
        this.fontSize = 12;
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

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getFontFamily() { return fontFamily; }
    public void setFontFamily(String fontFamily) { this.fontFamily = fontFamily; }
    public int getFontSize() { return fontSize; }
    public void setFontSize(int fontSize) { this.fontSize = fontSize; }
}