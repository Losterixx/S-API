# About
S-API is a lightweight Kotlin API designed to simplify plugin development.  

## Features
- Utility classes for UUIDs, Base64, scheduling, etc.
- Kotlin-first API with integrated Java compatibility (includes bStats metrics)
- Gradle-based build and publishing setup

## Notes
This API is free to use by anyone and can be integrated into your own projects without restrictions.<br>
However, please note that it was originally developed for my own projects, so I may not make changes or customizations specifically to suit other users' needs.<br>
Thank you for understanding and feel free to suggest improvements or report bugs if you find it useful.

# Other stuff

### Import in other projects
```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.Losterixx:S-API:vX.Y.Z")
}
```

### Publish new version (for me)
```shell
git add .
git commit -m "Release vX.Y.Z"
git tag -a vX.Y.Z -m "Release vX.Y.Z"
git push origin main
git push origin vX.Y.Z
```