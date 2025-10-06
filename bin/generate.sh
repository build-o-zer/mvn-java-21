#!/bin/bash

# 🎯 Maven Java 21 Project Generator
# 📝 Description: Generates a new Maven project using the org.buildozers archetype
# 🏗️  Author: Buildozers
# 📅 Created: $(date +%Y-%m-%d)

# 🔧 Script Constants - Archetype Configuration
readonly ARCHETYPE_GROUP_ID="org.buildozers"
readonly ARCHETYPE_ARTIFACT_ID="mvn-java-21-archetype"
readonly ARCHETYPE_VERSION="1.0.0-SNAPSHOT"

# Verify archetype availability
verify_archetype() {
    echo "🔍 Verifying archetype availability..."
    echo "   📦 Group ID    : $ARCHETYPE_GROUP_ID"
    echo "   🏷️  Artifact ID : $ARCHETYPE_ARTIFACT_ID"
    echo "   🔢 Version     : $ARCHETYPE_VERSION"
    echo ""
    
    # Try to resolve the archetype dependency
    mvn dependency:get \
        -DgroupId="$ARCHETYPE_GROUP_ID" \
        -DartifactId="$ARCHETYPE_ARTIFACT_ID" \
        -Dversion="$ARCHETYPE_VERSION" \
        -q > /dev/null 2>&1
    
    local exit_code=$?
    
    if [ $exit_code -ne 0 ]; then
        echo "❌ ERROR: Archetype not found!"
        echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        echo "🚫 The required archetype is not available in your Maven repositories:"
        echo "   📦 $ARCHETYPE_GROUP_ID:$ARCHETYPE_ARTIFACT_ID:$ARCHETYPE_VERSION"
        echo ""
        echo "💡 SOLUTIONS:"
        echo "   1️⃣  Install the archetype locally:"
        echo "      mvn install:install-file -DgroupId=$ARCHETYPE_GROUP_ID \\"
        echo "                               -DartifactId=$ARCHETYPE_ARTIFACT_ID \\"
        echo "                               -Dversion=$ARCHETYPE_VERSION \\"
        echo "                               -Dpackaging=jar \\"
        echo "                               -Dfile=path/to/archetype.jar"
        echo ""
        echo "   2️⃣  Build the archetype from source if available"
        echo ""
        echo "   3️⃣  Check if the archetype is available in a specific repository"
        echo "      and add it to your Maven settings.xml"
        echo ""
        echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        exit 1
    fi
    
    echo "✅ Archetype verification successful!"
    echo ""
}

# Show help function
show_help() {
    echo "🚀 Maven Java 21 Project Generator"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""
    echo "📖 DESCRIPTION:"
    echo "   This script generates a new Maven project using a predefined archetype"
    echo "   specifically designed for Java 21 projects."
    echo ""
    echo "🎯 USAGE:"
    echo "   $0 <base-package> <project-name> <version>"
    echo ""
    echo "📋 PARAMETERS:"
    echo "   📦 base-package  : The base package name (e.g., com.example)"
    echo "   🏷️  project-name  : The name of the project/artifact (e.g., my-app)"
    echo "   🔢 version       : The initial version (e.g., 1.0.0, 0.1.0-SNAPSHOT)"
    echo ""
    echo "💡 EXAMPLES:"
    echo "   $0 com.mycompany my-awesome-app 1.0.0"
    echo "   $0 org.example hello-world 0.1.0-SNAPSHOT"
    echo ""
    echo "🛠️  OPTIONS:"
    echo "   -h, --help      Show this help message"
    echo ""
    echo "🏗️  ARCHETYPE INFO:"
    echo "   📦 Group ID    : $ARCHETYPE_GROUP_ID"
    echo "   🏷️  Artifact ID : $ARCHETYPE_ARTIFACT_ID"
    echo "   🔢 Version     : $ARCHETYPE_VERSION"
    echo ""
    echo "📚 MORE INFO:"
    echo "   This generator uses Maven archetype:generate with predefined settings"
    echo "   for Java 21 development best practices. The script will verify the"
    echo "   archetype availability before proceeding with project generation."
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
}

# Check for help flags
if [[ "$1" == "-h" ]] || [[ "$1" == "--help" ]] || [[ "$#" -eq 0 ]]; then
    show_help
    exit 0
fi

# Validate arguments
if [ "$#" -ne 3 ]; then
    echo "❌ Error: Invalid number of arguments"
    echo ""
    show_help
    exit 1
fi

PACKAGE=$1
PROJECT=$2
VERSION=$3

echo "🎯 Maven Java 21 Project Generator"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🏗️  Generating   : $PROJECT"
echo "📦 Base package : $PACKAGE"
echo "🔢 Version      : $VERSION"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Verify archetype availability before proceeding
verify_archetype

echo "⚡ Starting Maven archetype generation..."

mvn archetype:generate \
    -DarchetypeGroupId="$ARCHETYPE_GROUP_ID" \
    -DarchetypeArtifactId="$ARCHETYPE_ARTIFACT_ID" \
    -DarchetypeVersion="$ARCHETYPE_VERSION" \
    -DgroupId="$PACKAGE" \
    -DartifactId="$PROJECT" \
    -Dversion="$VERSION" \
    -DinteractiveMode=false

# Check if Maven command was successful
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Project generation completed successfully!"
    echo "📁 Your new project is ready at: ./$PROJECT"
    echo "🎉 Happy coding with Java 21!"
else
    echo ""
    echo "❌ Project generation failed!"
    echo "🔍 Please check the Maven output above for error details."
    exit 1
fi