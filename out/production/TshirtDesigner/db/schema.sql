-- Custom T-Shirt Designer Database Schema

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- T-shirt styles table
CREATE TABLE IF NOT EXISTS tshirt_styles (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(255) NOT NULL
);

-- T-shirt colors table
CREATE TABLE IF NOT EXISTS tshirt_colors (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(255) NOT NULL,
    hex_code VARCHAR(7) NOT NULL
);

-- Symbols library table
CREATE TABLE IF NOT EXISTS symbols_library (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(255) NOT NULL,
    image_path VARCHAR(500) NOT NULL,
    category VARCHAR(255)
);

-- Designs table (main table for user designs)
CREATE TABLE IF NOT EXISTS designs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    tshirt_style_id INTEGER NOT NULL,
    tshirt_color_id INTEGER NOT NULL,
    custom_color_hex VARCHAR(7),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    exported BOOLEAN DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (tshirt_style_id) REFERENCES tshirt_styles(id),
    FOREIGN KEY (tshirt_color_id) REFERENCES tshirt_colors(id)
);

-- Text elements table (text added to designs)
CREATE TABLE IF NOT EXISTS text_elements (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    design_id INTEGER NOT NULL,
    content VARCHAR(1000) NOT NULL,
    font VARCHAR(255),
    size INTEGER,
    color VARCHAR(7),
    position_x INTEGER,
    position_y INTEGER,
    rotation REAL DEFAULT 0,
    FOREIGN KEY (design_id) REFERENCES designs(id) ON DELETE CASCADE
);

-- Image elements table (images/symbols added to designs)
CREATE TABLE IF NOT EXISTS image_elements (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    design_id INTEGER NOT NULL,
    image_path VARCHAR(500) NOT NULL,
    width INTEGER,
    height INTEGER,
    position_x INTEGER,
    position_y INTEGER,
    rotation REAL DEFAULT 0,
    symbol_id INTEGER,
    FOREIGN KEY (design_id) REFERENCES designs(id) ON DELETE CASCADE,
    FOREIGN KEY (symbol_id) REFERENCES symbols_library(id)
);

-- Insert some sample data for t-shirt styles
INSERT OR IGNORE INTO tshirt_styles (id, name) VALUES 
(1, 'Regular T-Shirt'),
(2, 'V-Neck'),
(3, 'Tank Top'),
(4, 'Long Sleeve'),
(5, 'Hoodie');

-- Insert some sample data for t-shirt colors
INSERT OR IGNORE INTO tshirt_colors (id, name, hex_code) VALUES 
(1, 'White', '#FFFFFF'),
(2, 'Black', '#000000'),
(3, 'Red', '#FF0000'),
(4, 'Blue', '#0000FF'),
(5, 'Green', '#008000'),
(6, 'Yellow', '#FFFF00'),
(7, 'Gray', '#808080'),
(8, 'Navy', '#000080'),
(9, 'Pink', '#FFC0CB'),
(10, 'Purple', '#800080');

-- Insert some sample symbols
INSERT OR IGNORE INTO symbols_library (id, name, image_path, category) VALUES 
(1, 'Heart', '/symbols/heart.png', 'Love'),
(2, 'Star', '/symbols/star.png', 'Shapes'),
(3, 'Smiley Face', '/symbols/smiley.png', 'Emotions'),
(4, 'Music Note', '/symbols/music.png', 'Music'),
(5, 'Peace Sign', '/symbols/peace.png', 'Symbols');