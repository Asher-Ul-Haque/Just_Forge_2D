#!/bin/bash

# Step 1: Run the Gradle shadowJar task in release mode
./gradlew shadowJar

# Step 2: Navigate to the build/libs directory
cd build/libs || exit

# Step 3: Find the JAR file (assuming there's only one)
JAR_FILE=$(ls *.jar | head -n 1)

# Step 4: Rename the JAR file (e.g., to ProjectShadowJar.jar)
NEW_JAR_NAME="Just-Forge-2D-release-1.0-linux.jar"
mv "$JAR_FILE" "$NEW_JAR_NAME"

# Step 5: Move the renamed JAR to the ProjectTemplate/libs folder
mv "$NEW_JAR_NAME" ../../ProjectTemplate/libs/

echo "JAR has been renamed and moved to ProjectTemplate/libs."
