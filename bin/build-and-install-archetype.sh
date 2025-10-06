#!/bin/bash

# 🏗️ Maven Archetype Builder & Installer
# 📝 Description: Builds the current project as a Maven archetype and installs it to local repository
# 🎯 Author: Buildozers
# 📅 Created: $(date +%Y-%m-%d)

# 🔧 Script Configuration
readonly SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
readonly PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
readonly ARCHETYPE_SUFFIX="-archetype"

# Show help function
show_help() {
    echo "🏗️ Maven Archetype Builder & Installer"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""
    echo "📖 DESCRIPTION:"
    echo "   This script builds the current Maven project as an archetype and installs"
    echo "   it into your local Maven repository (~/.m2/repository)."
    echo ""
    echo "🎯 USAGE:"
    echo "   $0 [OPTIONS]"
    echo ""
    echo "🛠️  OPTIONS:"
    echo "   -h, --help      Show this help message"
    echo "   -c, --clean     Clean the project before building"
    echo "   -v, --verbose   Enable verbose output"
    echo "   -s, --skip-tests Skip running tests during build"
    echo ""
    echo "📋 WHAT THIS SCRIPT DOES:"
    echo "   1️⃣  Validates the current project structure"
    echo "   2️⃣  Generates the Maven archetype from current project"
    echo "   3️⃣  Installs the archetype to local Maven repository"
    echo "   4️⃣  Verifies the installation was successful"
    echo "   5️⃣  Creates a bash alias 'mvn-java21-generate' for easy access"
    echo ""
    echo "💡 EXAMPLES:"
    echo "   $0                    # Build and install archetype"
    echo "   $0 --clean            # Clean build and install"
    echo "   $0 --verbose          # With detailed output"
    echo "   $0 --clean --verbose  # Clean build with verbose output"
    echo ""
    echo "📁 PROJECT STRUCTURE:"
    echo "   Current project: $PROJECT_DIR"
    echo "   Will be built as archetype with '-archetype' suffix"
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
}

# Extract project information from pom.xml
extract_project_info() {
    echo "📋 Extracting project information..."
    
    if [ ! -f "$PROJECT_DIR/pom.xml" ]; then
        echo "❌ ERROR: No pom.xml found in project directory!"
        echo "   Expected location: $PROJECT_DIR/pom.xml"
        exit 1
    fi
    
    # Extract groupId, artifactId, and version using xmllint or basic grep/sed
    if command -v xmllint >/dev/null 2>&1; then
        GROUP_ID=$(xmllint --xpath "//*[local-name()='project']/*[local-name()='groupId']/text()" "$PROJECT_DIR/pom.xml" 2>/dev/null)
        ARTIFACT_ID=$(xmllint --xpath "//*[local-name()='project']/*[local-name()='artifactId']/text()" "$PROJECT_DIR/pom.xml" 2>/dev/null)
        VERSION=$(xmllint --xpath "//*[local-name()='project']/*[local-name()='version']/text()" "$PROJECT_DIR/pom.xml" 2>/dev/null)
    else
        # Fallback to grep/sed if xmllint is not available
        GROUP_ID=$(grep -m1 "<groupId>" "$PROJECT_DIR/pom.xml" | sed 's/.*<groupId>\(.*\)<\/groupId>.*/\1/' | xargs)
        ARTIFACT_ID=$(grep -m1 "<artifactId>" "$PROJECT_DIR/pom.xml" | sed 's/.*<artifactId>\(.*\)<\/artifactId>.*/\1/' | xargs)
        VERSION=$(grep -m1 "<version>" "$PROJECT_DIR/pom.xml" | sed 's/.*<version>\(.*\)<\/version>.*/\1/' | xargs)
    fi
    
    # Validate extracted information
    if [ -z "$GROUP_ID" ] || [ -z "$ARTIFACT_ID" ] || [ -z "$VERSION" ]; then
        echo "❌ ERROR: Could not extract project information from pom.xml"
        echo "   Group ID: ${GROUP_ID:-'NOT FOUND'}"
        echo "   Artifact ID: ${ARTIFACT_ID:-'NOT FOUND'}"
        echo "   Version: ${VERSION:-'NOT FOUND'}"
        exit 1
    fi
    
    # Set archetype details
    ARCHETYPE_GROUP_ID="$GROUP_ID"
    ARCHETYPE_ARTIFACT_ID="${ARTIFACT_ID}${ARCHETYPE_SUFFIX}"
    ARCHETYPE_VERSION="$VERSION"
    
    echo "✅ Project information extracted successfully:"
    echo "   📦 Group ID    : $GROUP_ID"
    echo "   🏷️  Artifact ID : $ARTIFACT_ID"
    echo "   🔢 Version     : $VERSION"
    echo ""
    echo "🎯 Will create archetype:"
    echo "   📦 Group ID    : $ARCHETYPE_GROUP_ID"
    echo "   🏷️  Artifact ID : $ARCHETYPE_ARTIFACT_ID"
    echo "   🔢 Version     : $ARCHETYPE_VERSION"
    echo ""
}

# Validate project structure
validate_project() {
    echo "🔍 Validating project structure..."
    
    local required_dirs=("src/main/java" "src/test/java")
    local missing_dirs=()
    
    for dir in "${required_dirs[@]}"; do
        if [ ! -d "$PROJECT_DIR/$dir" ]; then
            missing_dirs+=("$dir")
        fi
    done
    
    if [ ${#missing_dirs[@]} -gt 0 ]; then
        echo "⚠️  WARNING: Some standard directories are missing:"
        for missing_dir in "${missing_dirs[@]}"; do
            echo "   📁 $missing_dir"
        done
        echo ""
        echo "   The archetype generation may still work, but consider adding these directories."
        echo ""
    else
        echo "✅ Project structure looks good!"
        echo ""
    fi
}

# Clean project if requested
clean_project() {
    if [ "$CLEAN_BUILD" = true ]; then
        echo "🧹 Cleaning project..."
        cd "$PROJECT_DIR" || exit 1
        mvn clean $MAVEN_OPTS
        
        if [ $? -ne 0 ]; then
            echo "❌ ERROR: Failed to clean project!"
            exit 1
        fi
        
        echo "✅ Project cleaned successfully!"
        echo ""
    fi
}

# Generate archetype from current project
generate_archetype() {
    echo "🏗️  Generating archetype from current project..."
    echo "   📁 Working directory: $PROJECT_DIR"
    
    cd "$PROJECT_DIR" || exit 1
    
    # Create archetype from project
    mvn archetype:create-from-project $MAVEN_OPTS
    
    if [ $? -ne 0 ]; then
        echo "❌ ERROR: Failed to generate archetype from project!"
        echo "   Please check the Maven output above for details."
        exit 1
    fi
    
    echo "✅ Archetype generated successfully!"
    echo ""
}

# Install archetype to local repository
install_archetype() {
    echo "📦 Installing archetype to local Maven repository..."
    
    local archetype_dir="$PROJECT_DIR/target/generated-sources/archetype"
    
    if [ ! -d "$archetype_dir" ]; then
        echo "❌ ERROR: Archetype directory not found!"
        echo "   Expected: $archetype_dir"
        echo "   Please ensure archetype generation was successful."
        exit 1
    fi
    
    cd "$archetype_dir" || exit 1
    
    # Install the archetype
    mvn install $MAVEN_OPTS
    
    if [ $? -ne 0 ]; then
        echo "❌ ERROR: Failed to install archetype!"
        echo "   Please check the Maven output above for details."
        exit 1
    fi
    
    echo "✅ Archetype installed successfully!"
    echo ""
}

# Verify archetype installation
verify_installation() {
    echo "🔍 Verifying archetype installation..."
    
    # Try to resolve the archetype
    mvn dependency:get \
        -DgroupId="$ARCHETYPE_GROUP_ID" \
        -DartifactId="$ARCHETYPE_ARTIFACT_ID" \
        -Dversion="$ARCHETYPE_VERSION" \
        -q > /dev/null 2>&1
    
    if [ $? -eq 0 ]; then
        echo "✅ Verification successful! Archetype is available in local repository."
        echo ""
        echo "🎯 ARCHETYPE DETAILS:"
        echo "   📦 Group ID    : $ARCHETYPE_GROUP_ID"
        echo "   🏷️  Artifact ID : $ARCHETYPE_ARTIFACT_ID"
        echo "   🔢 Version     : $ARCHETYPE_VERSION"
        echo ""
        echo "💡 USAGE EXAMPLE (with alias):"
        echo "   mvn-java21-generate com.example my-project 1.0.0"
        echo ""
        echo "💡 USAGE EXAMPLE (direct Maven):"
        echo "   mvn archetype:generate \\"
        echo "       -DarchetypeGroupId=$ARCHETYPE_GROUP_ID \\"
        echo "       -DarchetypeArtifactId=$ARCHETYPE_ARTIFACT_ID \\"
        echo "       -DarchetypeVersion=$ARCHETYPE_VERSION \\"
        echo "       -DgroupId=com.example \\"
        echo "       -DartifactId=my-project \\"
        echo "       -Dversion=1.0.0"
        echo ""
    else
        echo "❌ ERROR: Verification failed!"
        echo "   The archetype may not have been installed correctly."
        exit 1
    fi
}

# Create bash alias for the generate script
create_bash_alias() {
    echo "🔗 Setting up bash alias for project generator..."
    
    local alias_name="mvn-java21-generate"
    local generate_script_path="$PROJECT_DIR/bin/generate.sh"
    local alias_command="alias $alias_name='$generate_script_path'"
    
    # Check if alias already exists in current session
    if alias "$alias_name" >/dev/null 2>&1; then
        echo "ℹ️  Alias '$alias_name' already exists in current session."
        echo ""
        return 0
    fi
    
    # Determine which file to use for aliases
    local alias_file=""
    local file_description=""
    
    if [ -f "$HOME/.bash_aliases" ]; then
        alias_file="$HOME/.bash_aliases"
        file_description="~/.bash_aliases"
    elif [ -f "$HOME/.bashrc" ]; then
        alias_file="$HOME/.bashrc"
        file_description="~/.bashrc"
    else
        echo "⚠️  Neither ~/.bash_aliases nor ~/.bashrc found."
        echo "   Creating ~/.bash_aliases..."
        touch "$HOME/.bash_aliases"
        alias_file="$HOME/.bash_aliases"
        file_description="~/.bash_aliases"
    fi
    
    # Check if alias already exists in the file
    if grep -q "alias $alias_name=" "$alias_file" 2>/dev/null; then
        echo "ℹ️  Alias '$alias_name' already exists in $file_description"
        echo "   Current definition:"
        grep "alias $alias_name=" "$alias_file" | head -1 | sed 's/^/   /'
        echo ""
        return 0
    fi
    
    # Add alias to the file
    echo "" >> "$alias_file"
    echo "# Maven Java 21 Project Generator - Added by build-and-install-archetype.sh" >> "$alias_file"
    echo "$alias_command" >> "$alias_file"
    
    # Set alias in current session
    eval "$alias_command"
    
    echo "✅ Bash alias created successfully!"
    echo "   📄 File        : $file_description"
    echo "   🏷️  Alias name  : $alias_name"
    echo "   📁 Target      : $generate_script_path"
    echo ""
    echo "💡 USAGE:"
    echo "   $alias_name com.example my-project 1.0.0"
    echo ""
    echo "🔄 To use the alias immediately, run:"
    echo "   source $file_description"
    echo "   # or restart your terminal"
    echo ""
}

# Parse command line arguments
CLEAN_BUILD=false
VERBOSE=false
SKIP_TESTS=false
MAVEN_OPTS=""

while [[ $# -gt 0 ]]; do
    case $1 in
        -h|--help)
            show_help
            exit 0
            ;;
        -c|--clean)
            CLEAN_BUILD=true
            shift
            ;;
        -v|--verbose)
            VERBOSE=true
            MAVEN_OPTS="$MAVEN_OPTS -X"
            shift
            ;;
        -s|--skip-tests)
            SKIP_TESTS=true
            MAVEN_OPTS="$MAVEN_OPTS -DskipTests"
            shift
            ;;
        *)
            echo "❌ ERROR: Unknown option: $1"
            echo ""
            show_help
            exit 1
            ;;
    esac
done

# Main execution
main() {
    echo "🏗️ Maven Archetype Builder & Installer"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""
    
    # Extract project information
    extract_project_info
    
    # Validate project structure
    validate_project
    
    # Clean if requested
    clean_project
    
    # Generate archetype
    generate_archetype
    
    # Install archetype
    install_archetype
    
    # Verify installation
    verify_installation
    
    # Create bash alias for easy access
    create_bash_alias
    
    echo "🎉 SUCCESS: Archetype build and installation completed!"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
}

# Run main function
main "$@"
