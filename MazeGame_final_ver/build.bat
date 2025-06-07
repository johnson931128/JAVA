@echo off
echo Cleaning previous build...
if exist bin rmdir /s /q bin
if exist MazeGame.jar del MazeGame.jar

echo Creating bin directory...
mkdir bin

echo Compiling Java files...
javac -encoding UTF-8 -d bin src/Main.App.java src/gui/*.java src/model/*.java src/game/*.java

echo Creating JAR file...
jar cvfm MazeGame.jar manifest.txt -C bin .

echo Build complete!
echo You can now run the game by double-clicking MazeGame.jar or typing: java -jar MazeGame.jar 