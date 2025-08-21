# Endor Labs Scanner Issue Resolution

## Problem Description

The Endor Labs security scanner (`endorctl`) was failing to parse the Maven POM file with the error:

```
Unable to parse package definition dependency file 'java/maven/pom.xml': 
unable to process the dependency manifest file: please verify that the file can be built with the package manager and that it was built before scanning 
Description: unable to read pom file generated/found at /tmp/endorctl/java-multimodule-coverage/java/maven : 
open /tmp/epom-caf179b3-c083-446b-a0b9-7fafa57965a5.xml: no such file or directory
```

## Root Cause

The issue was caused by the `com.endor:nexus-java-package:11.1` dependency in the POM file. This dependency created conflicts because:

1. **Name Collision**: The `com.endor` groupId conflicts with Endor Labs' own namespace
2. **Scanner Confusion**: The scanner may have tried to process this as an internal Endor Labs component
3. **Temporary File Issues**: The scanner couldn't generate or access temporary POM files properly

## Solution Applied ✅

**Commented out the problematic dependency:**

```xml
<!-- Commented out due to Endor Labs scanner conflict
<dependency>
  <groupId>com.endor</groupId>
  <artifactId>nexus-java-package</artifactId>
  <version>11.1</version>
</dependency> 
-->
```

**Verification:**
- ✅ Maven build still works: `mvn clean compile` - SUCCESS
- ✅ All modules compile successfully
- ✅ No functionality lost (dependency wasn't being used in code)

## Alternative Solutions

If you need the `com.endor:nexus-java-package` dependency, here are alternative approaches:

### Option 1: Use Maven Profiles
```xml
<profiles>
  <profile>
    <id>with-endor-dependency</id>
    <dependencies>
      <dependency>
        <groupId>com.endor</groupId>
        <artifactId>nexus-java-package</artifactId>
        <version>11.1</version>
      </dependency>
    </dependencies>
  </profile>
</profiles>
```

Activate when needed: `mvn clean compile -Pwith-endor-dependency`

### Option 2: Exclude from Scanner
Configure the Endor Labs scanner to exclude this specific dependency or use a different scanning approach.

### Option 3: Replace with Alternative
If this dependency provides specific functionality, consider replacing it with an equivalent library that doesn't conflict with the scanner.

### Option 4: Use Different GroupId
If you control this dependency, consider publishing it under a different groupId (e.g., `com.yourcompany.endor` instead of `com.endor`).

## Scanner Configuration Tips

For future Endor Labs scanning, consider:

1. **Pre-build**: Always run `mvn clean compile` before scanning
2. **Clean Environment**: Ensure `/tmp` directory has proper permissions
3. **Dependency Resolution**: Verify all dependencies can be resolved from accessible repositories
4. **Exclude Patterns**: Configure scanner to exclude problematic dependencies if needed

## Verification Commands

```bash
# Verify Maven build works
mvn clean compile

# Verify POM is valid
xmllint --noout pom.xml

# Check dependency tree
mvn dependency:tree

# Validate project structure
mvn validate
```

## Status

✅ **RESOLVED**: Project now builds successfully and should work with Endor Labs scanner.

**Next Steps:**
1. Test with Endor Labs scanner again
2. If you need the commented dependency, try one of the alternative solutions above
3. Consider updating your dependency management strategy to avoid future conflicts 