#!/bin/bash

echo "Cleaning previous build..."
rm -rf bin MazeGame.jar

echo "Creating bin directory..."
mkdir bin

echo "Compiling Java files..."
javac -encoding UTF-8 -d bin src/App.java src/gui/*.java src/model/*.java src/game/*.java

echo "Creating JAR file..."
jar cvfm MazeGame.jar manifest.txt -C bin .

echo "Making JAR executable..."
chmod +x MazeGame.jar

echo "Build complete!"
echo "You can now run the game by double-clicking MazeGame.jar or typing: java -jar MazeGame.jar" 