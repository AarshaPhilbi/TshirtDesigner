package com.tshirt.designApp;

import java.awt.*;

public class TextElement extends GraphicElement {
    private String text;
    private String fontFamily;
    private int fontSize;
    private Color textColor;
    private Font font;

    public TextElement(double x, double y, String text) {
        super(x, y, 100, 40); // Default size
        this.text = text;
        this.fontFamily = "Arial";
        this.fontSize = 24;
        this.textColor = Color.BLACK;
        updateFont();
    }

    @Override
    public void draw(Graphics2D g) {
        if (!visible || text == null || text.isEmpty()) return;
        
        g.setColor(textColor);
        g.setFont(font);
        
        // Calculate actual bounds based on text metrics
        FontMetrics fm = g.getFontMetrics();
        bounds.setRect(position.getX(), position.getY(), 
                      fm.stringWidth(text), fm.getHeight());
        
        g.drawString(text, (float)position.getX(), (float)position.getY() + fm.getAscent());
    }

    @Override
    public boolean contains(Point point) {
        return bounds.contains(point.getX(), point.getY());
    }

    private void updateFont() {
        this.font = new Font(fontFamily, Font.PLAIN, fontSize);
    }

    // Getters and Setters
    public String getText() { return text; }
    public void setText(String text) { 
        this.text = text; 
        // Recalculate bounds when text changes
    }
    public String getFontFamily() { return fontFamily; }
    public void setFontFamily(String fontFamily) { 
        this.fontFamily = fontFamily; 
        updateFont();
    }
    public int getFontSize() { return fontSize; }
    public void setFontSize(int fontSize) { 
        this.fontSize = fontSize; 
        updateFont();
    }
    public Color getTextColor() { return textColor; }
    public void setTextColor(Color textColor) { this.textColor = textColor; }
}