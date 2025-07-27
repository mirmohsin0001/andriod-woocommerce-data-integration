# Product Showcase Android App

A production-ready Android application showcasing WooCommerce products with modern Android development practices.

## Features

### 🏗️ Architecture
- **MVVM Pattern** with LiveData/ViewModel and Data Binding/View Binding
- **Navigation Component** for seamless screen transitions
- **Clean Architecture** with separate data, domain, and UI layers
- **Dependency Injection** with Hilt

### 🎨 UI & UX
- **Jetpack Compose** for modern, declarative UI
- **Material 3 Design** with dynamic color schemes
- **Toggleable grid/list layouts** for product catalog
- **CoordinatorLayout** with collapsing toolbar on product detail
- **Image carousel** with page indicators
- **Tabbed sections** for description, specs, and reviews
- **Sticky "Add to Cart"** floating action button

### 📱 Data Integration
- **WooCommerce REST API** integration
- **Retrofit** for network calls with proper error handling
- **Paging 3** for efficient data loading with DiffUtil
- **Repository Pattern** for data abstraction

### 🖼️ Image Loading & Caching
- **Glide** for asynchronous image loading
- **Memory and disk caching** for optimal performance
- **Placeholder and error handling** for better UX
- **Image prefetching** for smooth scrolling

### 🎨 Theming & Accessibility
- **Dynamic light/dark theming** following Material 3 guidelines
- **Content descriptions** for all UI elements
- **Scalable text** using sp units
- **TalkBack compatibility** for screen readers
- **High contrast support** and accessible color schemes

## Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Hilt
- **Network**: Retrofit + OkHttp
- **Image Loading**: Glide
- **Navigation**: Navigation Component
- **Async Programming**: Coroutines + Flow
- **Pagination**: Paging 3
- **Testing**: JUnit, Mockito

## Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24+
- Kotlin 1.9+

### Configuration

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ProductShowcase
   ```

2. **WooCommerce API Configuration**
   
   Update the API credentials in `ProductRepositoryImpl.kt`:
   ```kotlin
   companion object {
       const val CONSUMER_KEY = "your_consumer_key_here"
       const val CONSUMER_SECRET = "your_consumer_secret_here"
   }
   ```

3. **Base URL Configuration**
   
   Update the base URL in `NetworkModule.kt`:
   ```kotlin
   private const val BASE_URL = "https://your-woocommerce-site.com/wp-json/wc/v3/"
   ```

### Build and Run

1. **Sync project dependencies**
   ```bash
   ./gradlew sync
   ```

2. **Build the project**
   ```bash
   ./gradlew assembleDebug
   ```

3. **Run tests**
   ```bash
   ./gradlew test
   ```

4. **Install on device/emulator**
   ```bash
   ./gradlew installDebug
   ```

## Project Structure

```
app/src/main/java/com/sparrow/amp2/
├── data/
│   ├── api/                 # API service interfaces
│   ├── mapper/              # Data mappers
│   ├── model/               # API response models
│   ├── paging/              # Paging sources
│   └── repository/          # Repository implementations
├── domain/
│   ├── model/               # Domain models
│   └── repository/          # Repository interfaces
├── ui/
│   ├── components/          # Reusable UI components
│   ├── navigation/          # Navigation setup
│   ├── productdetail/       # Product detail screen
│   ├── productlist/         # Product list screen
│   └── theme/               # Material 3 theming
├── di/                      # Dependency injection modules
└── utils/                   # Utility classes
```

## API Integration

This app integrates with the WooCommerce REST API to fetch product data. Key endpoints used:

- `GET /products` - Fetch paginated product list
- `GET /products/{id}` - Fetch individual product details
- `GET /products?search={query}` - Search products

### Required WooCommerce API Permissions
- Read access to products
- Consumer key and secret for authentication

## Performance Optimizations

- **Image caching** with Glide for fast loading
- **Paging 3** for efficient large dataset handling
- **DiffUtil** for optimal RecyclerView updates
- **Coroutines** for non-blocking operations
- **State management** with ViewModel and StateFlow

## Accessibility Features

- Content descriptions for all interactive elements
- Semantic markup for screen readers
- Scalable text using sp units
- High contrast color support
- Keyboard navigation support
- TalkBack announcements for state changes

## Testing

The project includes:
- **Unit tests** for data layer components
- **Mapper tests** for data transformation
- **ViewModel tests** for business logic
- **Repository tests** with mocked dependencies

Run tests with:
```bash
./gradlew test
```

## Future Enhancements

- [ ] Shopping cart functionality
- [ ] User authentication
- [ ] Wishlist feature
- [ ] Product reviews and ratings
- [ ] Push notifications
- [ ] Offline support with Room database
- [ ] Analytics integration
- [ ] Crash reporting

## Dependencies

Key dependencies used in this project:

- Jetpack Compose (UI framework)
- Navigation Component (navigation)
- Hilt (dependency injection)
- Retrofit (networking)
- Glide (image loading)
- Paging 3 (pagination)
- Material 3 (design system)
- Coroutines (async programming)

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## Support

For issues and questions:
- Create an issue in the repository
- Check the existing documentation
- Review the WooCommerce API documentation
