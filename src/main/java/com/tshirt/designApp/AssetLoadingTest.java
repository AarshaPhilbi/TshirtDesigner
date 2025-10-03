package com.tshirt.designApp;

import com.tshirt.designApp.database.AssetLoader;
import com.tshirt.designApp.database.DatabaseManager;
import java.util.List;
import java.util.Map;
import java.io.File;

/**
 * Test program to demonstrate loading fonts, symbols, and colors
 * This shows your GUI team how to retrieve assets from the database
 */
public class AssetLoadingTest {

    public static void main(String[] args) {
        debugFilePaths();
        System.out.println("=== T-Shirt Design Asset Loading Demo ===");

        // Initialize database
        DatabaseManager.initializeDatabase();

        // Demonstrate loading each type of asset
        demonstrateFontLoading();
        demonstrateSymbolLoading();
        demonstrateColorLoading();
        demonstrateTshirtStyleLoading();

        System.out.println("\n=== Asset loading demonstration complete! ===");
        System.out.println("Your GUI team can use these methods to populate dropdowns, lists, and galleries.");
        testImageFiles();
    }

    /**
     * Show how to load and display fonts
     */
    private static void demonstrateFontLoading() {
        System.out.println("\n--- Loading Fonts ---");
        List<AssetLoader.Font> fonts = AssetLoader.loadAllFonts();

        System.out.println("Available fonts for text elements:");
        for (AssetLoader.Font font : fonts) {
            System.out.printf("  • %s (Family: %s, Bold: %s, Italic: %s)%n",
                    font.fontName,
                    font.fontFamily,
                    font.boldAvailable ? "Yes" : "No",
                    font.italicAvailable ? "Yes" : "No"
            );
        }
        System.out.println("Total fonts available: " + fonts.size());
    }

    /**
     * Show how to load symbols by category
     */
    private static void demonstrateSymbolLoading() {
        System.out.println("\n--- Loading Symbols ---");

        // First, show all categories
        List<String> categories = AssetLoader.loadSymbolCategories();
        System.out.println("Symbol categories: " + String.join(", ", categories));

        // Show symbols from each category
        for (String category : categories) {
            List<AssetLoader.Symbol> symbols = AssetLoader.loadSymbols(category);
            System.out.println("\n" + category + " symbols (" + symbols.size() + "):");
            for (AssetLoader.Symbol symbol : symbols) {
                System.out.printf("  • %s (Path: %s)%n", symbol.name, symbol.imagePath);
            }
        }

        // Show how to load ALL symbols at once
        List<AssetLoader.Symbol> allSymbols = AssetLoader.loadSymbols(null);
        System.out.println("\nTotal symbols in database: " + allSymbols.size());
    }

    /**
     * Show how to load t-shirt colors
     */
    private static void demonstrateColorLoading() {
        System.out.println("\n--- Loading T-Shirt Colors ---");
        List<AssetLoader.TshirtColor> colors = AssetLoader.loadAllColors();

        System.out.println("Available t-shirt colors:");
        for (AssetLoader.TshirtColor color : colors) {
            System.out.printf("  • %s (%s)%n", color.name, color.hexCode);
        }
        System.out.println("Total colors available: " + colors.size());
    }

    /**
     * Show how to load t-shirt styles
     */
    private static void demonstrateTshirtStyleLoading() {
        System.out.println("\n--- Loading T-Shirt Styles ---");
        Map<Integer, String> styles = AssetLoader.loadTshirtStyles();

        System.out.println("Available t-shirt styles:");
        for (Map.Entry<Integer, String> style : styles.entrySet()) {
            System.out.printf("  • ID: %d - %s%n", style.getKey(), style.getValue());
        }
        System.out.println("Total styles available: " + styles.size());
    }

    /**
     * Example: How your GUI might populate a font dropdown
     */
    public static String[] getFontNamesForDropdown() {
        List<AssetLoader.Font> fonts = AssetLoader.loadAllFonts();
        return fonts.stream()
                .map(font -> font.fontName)
                .toArray(String[]::new);
    }

    /**
     * Example: How your GUI might create a symbol gallery
     */
    public static List<AssetLoader.Symbol> getSymbolsForCategory(String category) {
        return AssetLoader.loadSymbols(category);
    }

    /**
     * Example: How your GUI might populate a color picker
     */
    public static String[] getColorHexCodes() {
        List<AssetLoader.TshirtColor> colors = AssetLoader.loadAllColors();
        return colors.stream()
                .map(color -> color.hexCode)
                .toArray(String[]::new);
    }
    public static void testImageFiles() {
        System.out.println("\n--- Testing Image File Existence ---");
        List<AssetLoader.Symbol> symbols = AssetLoader.loadSymbols(null);

        int found = 0;
        int missing = 0;

        for (AssetLoader.Symbol symbol : symbols) {
            File imageFile = new File(symbol.imagePath);
            boolean exists = imageFile.exists();
            System.out.printf("%s: %s (%s)%n",
                    symbol.name,
                    exists ? "FOUND" : "MISSING",
                    symbol.imagePath
            );
            if (exists) found++; else missing++;
        }

        System.out.printf("\nSummary: %d found, %d missing%n", found, missing);
    }
    public static void debugFilePaths() {
        System.out.println("\n--- Debugging File Paths ---");
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        // Check if assets folder exists
        File assetsDir = new File("assets");
        System.out.println("Assets folder exists: " + assetsDir.exists());
        System.out.println("Assets absolute path: " + assetsDir.getAbsolutePath());

        // Check a specific file
        File testFile = new File("assets/symbols/shapes/star.png");
        System.out.println("Test file exists: " + testFile.exists());
        System.out.println("Test file absolute path: " + testFile.getAbsolutePath());

        // List what's in assets folder if it exists
        if (assetsDir.exists()) {
            File[] contents = assetsDir.listFiles();
            System.out.println("Contents of assets folder:");
            for (File f : contents) {
                System.out.println("  - " + f.getName());
            }
        }
    }
}