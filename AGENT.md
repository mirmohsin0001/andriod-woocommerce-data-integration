# AGENT.md - Development Guide

## Frequently Used Commands

### Build & Test Commands
```bash
# Clean and build the project
./gradlew clean build

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Install debug build on connected device
./gradlew installDebug

# Check for dependency updates
./gradlew dependencyUpdates
```

### Code Quality Commands
```bash
# Run Kotlin linting
./gradlew ktlintCheck

# Auto-fix Kotlin linting issues
./gradlew ktlintFormat

# Run static analysis
./gradlew lint

# Generate lint report
./gradlew lintDebug
```

## Project Structure Guidelines

### Package Organization
- `data/` - Data layer with API, repositories, models
- `domain/` - Business logic and domain models
- `ui/` - Presentation layer with screens and components
- `di/` - Dependency injection modules
- `utils/` - Utility classes and extensions

### Code Style Preferences
- Use data classes for models
- Prefer sealed classes for state management
- Use descriptive variable names
- Keep functions small and focused
- Add proper documentation for public APIs
- Follow Material 3 design guidelines

### Architecture Patterns
- MVVM pattern with ViewModel and LiveData/StateFlow
- Repository pattern for data access
- Clean Architecture with clear layer separation
- Dependency injection with Hilt
- Navigation Component for screen navigation

### Testing Guidelines
- Unit tests for ViewModels and repositories
- Integration tests for API components
- UI tests for critical user flows
- Aim for 80%+ code coverage
- Mock external dependencies

### Accessibility Standards
- Add contentDescription to all images and icons
- Use semantic markup for screen readers
- Ensure minimum touch target size (48dp)
- Support keyboard navigation
- Use sp units for text sizes
- Maintain proper color contrast ratios

### Performance Best Practices
- Use Paging 3 for large datasets
- Implement proper image caching with Glide
- Optimize network requests with proper caching
- Use LazyColumn/LazyRow for lists
- Minimize recomposition in Compose
- Profile app performance regularly

## Common Issues & Solutions

### Build Issues
- If build fails with Hilt errors, ensure kapt is properly configured
- For Compose issues, check BOM version compatibility
- Clear build cache with `./gradlew clean` if builds are inconsistent

### Network Issues
- Ensure INTERNET permission is added to AndroidManifest.xml
- Check network security config for API calls
- Verify API endpoints and authentication

### UI Issues
- Use preview composables for UI development
- Test on different screen sizes and orientations
- Verify dark/light theme compatibility

## Development Workflow

1. Create feature branch from main
2. Implement feature with tests
3. Run quality checks and tests
4. Update documentation if needed
5. Submit pull request with proper description
6. Code review and merge

## Debugging Tips

- Use Flipper or Stetho for network debugging
- Enable StrictMode in debug builds
- Use Layout Inspector for UI debugging
- Leverage Compose preview for quick iterations
- Use Timber for structured logging

## Dependencies Management

- Keep dependencies up to date
- Use version catalogs for dependency management
- Test thoroughly after dependency updates
- Monitor app size impact of new dependencies
