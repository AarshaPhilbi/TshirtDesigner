package com.tshirt.designApp;

public class Main {
    public static void main(String[] args) {
        // 1. Create a new design (like a blank t-shirt canvas)
        Design myTshirtDesign = new Design(300, 200);
        
        // 2. Create some elements
        Shape redRectangle = new Shape(50, 50, 100, 50, "rectangle");
        redRectangle.setFillColor(java.awt.Color.RED);
        
        TextElement titleText = new TextElement(75, 30, "Hello World!");
        
        // 3. Wrap elements in layers
        Layer rectangleLayer = new Layer(redRectangle, "Red Rectangle");
        Layer textLayer = new Layer(titleText, "Title Text");
        
        // 4. Add layers to the design
        myTshirtDesign.addLayer(rectangleLayer);
        myTshirtDesign.addLayer(textLayer);
        
        // 5. Test moving an element
        myTshirtDesign.moveLayer(0, 20, 20); // Move the rectangle
        
        // 6. See what we have
        System.out.println("Design has " + myTshirtDesign.getLayers().size() + " layers");
        System.out.println("First layer is: " + myTshirtDesign.getLayers().get(0).getName());
        
        System.out.println("Core logic is working! Ready for GUI and Export teams.");
    }
}