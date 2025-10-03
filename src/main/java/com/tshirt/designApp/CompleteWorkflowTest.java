package com.tshirt.designApp;

import com.tshirt.designApp.database.*;
import java.util.List;

public class CompleteWorkflowTest {

    public static void main(String[] args) {
        System.out.println("=== Complete Database Workflow Test ===");

        DatabaseManager.initializeDatabase();

        // Test 1: User Registration and Login
        System.out.println("\n--- Testing Authentication ---");
        UserAuthentication.registerUser("testuser", "test@email.com", "password123");

        if (UserSession.getInstance().login("testuser", "password123")) {
            System.out.println("Login successful!");
            int userId = UserSession.getInstance().getCurrentUserId();

            // Test 2: Create a Design
            System.out.println("\n--- Testing Design Creation ---");
            int designId = DesignManager.saveNewDesign(userId, 1, 2, null);
            System.out.println("Created design ID: " + designId);

            // Test 3: Add Text Element
            System.out.println("\n--- Testing Text Element ---");
            int textId = DesignManager.addTextElement(designId, "Hello World", "Arial Bold", 48, "#000000", 150, 100, 0);
            System.out.println("Added text element ID: " + textId);

            // Test 4: Add Image Element
            System.out.println("\n--- Testing Image Element ---");
            int imageId = DesignManager.addImageElement(designId, "TshirtDesigner/assets/symbols/shapes/star.png", 100, 100, 200, 150, 0, 10);
            System.out.println("Added image element ID: " + imageId);

            // Test 5: Load User's Designs
            System.out.println("\n--- Testing Load Designs ---");
            List<DesignManager.Design> designs = DesignManager.getUserDesigns(userId);
            System.out.println("User has " + designs.size() + " design(s)");

            // Test 6: Load Design Elements
            System.out.println("\n--- Testing Load Elements ---");
            List<DesignManager.TextElement> texts = DesignManager.getTextElements(designId);
            List<DesignManager.ImageElement> images = DesignManager.getImageElements(designId);
            System.out.println("Design has " + texts.size() + " text element(s) and " + images.size() + " image element(s)");

            // Test 7: Delete Design
            System.out.println("\n--- Testing Delete ---");
            boolean deleted = DesignManager.deleteDesign(designId);
            System.out.println("Design deleted: " + deleted);

            System.out.println("\n=== All tests passed! ===");
        }
    }
}