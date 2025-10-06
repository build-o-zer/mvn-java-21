package org.buildozers.mvnjava21.examples;

/**
 * 🔍 Runtime Version Detector
 * 
 * Generic utility class that dynamically detects library versions at runtime by inspecting 
 * the classpath, JAR manifests, and package information. This provides accurate 
 * version information without hardcoded constants that can become outdated.
 * 
 * <p>Supports detection for any Java library using multiple fallback strategies:
 * <ul>
 *   <li>Package metadata (Implementation-Version, Specification-Version)</li>
 *   <li>JAR filename pattern analysis</li>
 *   <li>Manifest file attributes</li>
 *   <li>System properties</li>
 * </ul>
 * 
 * @author Buildozers
 */
public final class RuntimeVersionDetector {
    
    private RuntimeVersionDetector() {
        // Utility class - prevent instantiation
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Dynamically detects a library version at runtime by inspecting the classpath.
     * Uses multiple fallback strategies to ensure reliable version detection across different environments.
     * 
     * @param referenceClass a class from the target library to use as reference point
     * @param jarNamePrefix the expected prefix of the library's JAR file (e.g., "eclipse-collections", "lombok", "jansi")
     * @param systemPropertyKey optional system property key that might contain the version (can be null)
     * @return the detected version string, or "unknown" if version cannot be determined
     */
    public static String detectVersion(Class<?> referenceClass, String jarNamePrefix, String systemPropertyKey) {
        if (referenceClass == null || jarNamePrefix == null || jarNamePrefix.trim().isEmpty()) {
            return "unknown";
        }
        
        // Try different strategies in order of reliability
        String version = getVersionFromPackage(referenceClass);
        if (isValidVersion(version)) {
            return version;
        }
        
        version = getVersionFromJarPath(referenceClass, jarNamePrefix);
        if (isValidVersion(version)) {
            return version;
        }
        
        version = getVersionFromManifest(referenceClass);
        if (isValidVersion(version)) {
            return version;
        }
        
        if (systemPropertyKey != null && !systemPropertyKey.trim().isEmpty()) {
            version = System.getProperty(systemPropertyKey);
            if (isValidVersion(version)) {
                return version;
            }
        }
        
        return "unknown";
    }
    
    /**
     * Overloaded method for detecting library version without system property fallback.
     * 
     * @param referenceClass a class from the target library to use as reference point
     * @param jarNamePrefix the expected prefix of the library's JAR file
     * @return the detected version string, or "unknown" if version cannot be determined
     */
    public static String detectVersion(Class<?> referenceClass, String jarNamePrefix) {
        return detectVersion(referenceClass, jarNamePrefix, null);
    }
    
    /**
     * Attempts to extract version information from the library's package metadata.
     * This method checks both implementation and specification versions.
     * 
     * @param referenceClass a class from the target library
     * @return the version string from package metadata, or null if not available
     */
    private static String getVersionFromPackage(Class<?> referenceClass) {
        try {
            Package pkg = referenceClass.getPackage();
            if (pkg != null) {
                String version = pkg.getImplementationVersion();
                if (isValidVersion(version)) {
                    return version;
                }
                return pkg.getSpecificationVersion();
            }
        } catch (Exception e) {
            // Ignore and try next method
        }
        return null;
    }
    
    /**
     * Attempts to extract version information from the JAR file path.
     * This method analyzes the code source location to find JAR filenames
     * that contain version information.
     * 
     * @param referenceClass a class from the target library
     * @param jarNamePrefix the expected prefix of the library's JAR file
     * @return the version string extracted from JAR path, or null if not available
     */
    private static String getVersionFromJarPath(Class<?> referenceClass, String jarNamePrefix) {
        try {
            java.net.URL url = referenceClass.getProtectionDomain().getCodeSource().getLocation();
            if (url == null) {
                return null;
            }
            
            String jarPath = url.toString();
            if (!jarPath.contains(jarNamePrefix)) {
                return null;
            }
            
            return extractVersionFromJarName(jarPath, jarNamePrefix);
        } catch (Exception e) {
            // Ignore and try next method
            return null;
        }
    }
    
    /**
     * Extracts version information from JAR filename patterns.
     * Looks for patterns like "eclipse-collections-13.0.0.jar" and extracts the version part.
     * 
     * @param jarPath the full JAR path to analyze
     * @param jarNamePrefix the expected prefix of the library's JAR file (e.g., "eclipse-collections")
     * @return the extracted version string, or null if no valid version pattern is found
     */
    private static String extractVersionFromJarName(String jarPath, String jarNamePrefix) {
        String[] pathParts = jarPath.split("/");
        String jarPrefix = jarNamePrefix + "-";
        for (String part : pathParts) {
            if (part.startsWith(jarPrefix) && part.endsWith(".jar")) {
                String versionPart = part.substring(jarPrefix.length());
                versionPart = versionPart.substring(0, versionPart.length() - ".jar".length());
                if (versionPart.matches("\\d+\\.\\d+\\.\\d+.*")) {
                    return versionPart;
                }
            }
        }
        return null;
    }
    
    /**
     * Attempts to extract version information from JAR manifest files.
     * This method looks for standard manifest attributes like Bundle-Version
     * and Implementation-Version.
     * 
     * @param referenceClass a class from the target library
     * @return the version string from manifest, or null if not available
     */
    private static String getVersionFromManifest(Class<?> referenceClass) {
        try (java.io.InputStream manifestStream = referenceClass.getClassLoader()
                .getResourceAsStream("META-INF/MANIFEST.MF")) {
            if (manifestStream != null) {
                java.util.jar.Manifest manifest = new java.util.jar.Manifest(manifestStream);
                java.util.jar.Attributes attributes = manifest.getMainAttributes();
                
                String version = attributes.getValue("Bundle-Version");
                if (isValidVersion(version)) {
                    return version;
                }
                
                return attributes.getValue("Implementation-Version");
            }
        } catch (Exception e) {
            // Ignore and try next method
        }
        return null;
    }
    
    /**
     * Validates whether a version string is non-null and non-empty.
     * 
     * @param version the version string to validate
     * @return true if the version is valid (non-null and non-empty), false otherwise
     */
    private static boolean isValidVersion(String version) {
        return version != null && !version.trim().isEmpty();
    }
}
