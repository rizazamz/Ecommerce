E-Commerce Android Application
Aplikasi E-Commerce Android dengan fitur katalog produk, keranjang belanja, dan profil user. Dibangun menggunakan MVVM Architecture, Room Database, dan Retrofit.

✨ Features
✅ Product catalog dengan pagination & search
✅ Product detail dengan add to cart
✅ Shopping cart dengan persistent storage (Room)
✅ Update quantity & delete items
✅ Checkout dengan kalkulasi total + shipping
✅ User profile page
✅ Splash screen 
✅ Pull to refresh & error handling
✅ Bottom navigation
✅ Responsive design (portrait & landscape)


✨ Architecture
```
MVVM (Model-View-ViewModel) Pattern:
View (Activity) → ViewModel → Repository → Data Source (Room/Retrofit)
```
Layer Structure:

Presentation: Activity, Fragment, ViewModel, Adapter
Domain: Model (Business entities)
Data: Repository, Local (Room), Remote (Retrofit)


📦 Tech Stack
Core:

Language: Java 17
Min SDK: 25 (Android 7.0)
Target SDK: 36

Architecture Components:

ViewModel & LiveData
Room Database 2.6.1
View Binding

Networking:

Retrofit 2.11.0
Gson Converter
OkHttp 4.12.0

UI/UX:

Material Design 3
Glide 4.16.0
SwipeRefreshLayout
RecyclerView

Testing:

JUnit 4.13.2
Mockito 5.12.0
Architecture Testing
MockWebServer

🚀 Getting Started
Prerequisites:

Android Studio Android Studio Otter 2 Feature Drop | 2025.2.2 Patch 1
JDK 17+
Android SDK 25+
Gradle 8.0+
```
Installation:
bash git clone https://github.com/rizazamz/Ecommerce.git
cd ecommerce-app

# Open in Android Studio → Sync Gradle → Run
```
Configuration:

API Base URL: https://dummyjson.com/
Database Name: ecommerce_database
Page Size: 20 items


📱 Key Features Implementation

1. Product Catalog (Pagination)
```
java// ProductViewModel.java
private int currentSkip = 0;
private static final int PAGE_SIZE = 20;

public void loadProducts() {
    productRepository.getProducts(PAGE_SIZE, currentSkip)
        .observeForever(resource -> {
            if (resource.getStatus() == Status.SUCCESS) {
                allProducts.addAll(resource.getData());
                currentSkip += PAGE_SIZE;
            }
        });
}
```

2. Shopping Cart (Room Persistence)
```
java// CartRepository.java
public void insert(CartItem cartItem) {
    executorService.execute(() -> cartDao.insert(cartItem));
}

// Data saved at: /data/data/com.riza.ecommerce/databases/
```
3. Search (Client-Side Filtering)
```
javaprivate void filterProducts(String query) {
    List<Product> filtered = allProducts.stream()
        .filter(p -> p.getTitle().toLowerCase().contains(query.toLowerCase()))
        .collect(Collectors.toList());
    adapter.setProducts(filtered);
}
```
5. Configuration Change Handling
```
java@Override
public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    int columns = getResources().getInteger(R.integer.product_grid_columns);
    layoutManager.setSpanCount(columns);
}
```
🧪 Testing
```
Run Tests:
bash./gradlew test                    # Unit tests
```
Test Cases:

Pagination: No duplicate items
Cart Persistence: Data tetap ada setelah restart
Total Calculation: Subtotal + shipping benar
Error 500: Error handling dengan retry button


📊 Responsive Design
DeviceOrientationGrid ColumnsPhonePortrait2PhoneLandscape3Tablet 7"Portrait3Tablet 10"Portrait4Tablet 10"Landscape5
Resource Qualifiers:

values/ - Phone portrait (default)
values-sw600dp/ - Tablet 7"
values-sw720dp/ - Tablet 10"
values-land/ - Landscape


🌐 API Endpoints
```
Base URL: https://dummyjson.com/
```
Get Products:
```
httpGET /products?limit=20&skip=0
```
Get Product Detail:
```
httpGET /products/{id}
```
Response Example:
```
json{
  "products": [{
    "id": 1,
    "title": "Product Name",
    "price": 109.95,
    "thumbnail": "https://...",
    "stock": 100
  }],
  "total": 194
}
```
🔐 Data Persistence
Room Database Schema:
sqlCREATE TABLE cart_items (
    productId INTEGER PRIMARY KEY,
    title TEXT,
    price REAL,
    thumbnail TEXT,
    quantity INTEGER,
    stock INTEGER
)
```

**Storage Location:**
/data/data/com.riza.ecommerce/databases/ecommerce_database
```


🎨 Design Patterns

MVVM: Separation of UI and business logic
Repository: Single source of truth
Observer: LiveData reactive updates
Singleton: Database instance


📈 Performance Optimization

✅ Glide image caching (memory + disk)
✅ Room async operations (ExecutorService)
✅ ViewModel lifecycle-aware
✅ ViewBinding (no findViewById)
✅ RecyclerView view recycling

📄 Dependencies
```
gradle// Architecture Components
implementation 'androidx.lifecycle:lifecycle-viewmodel:2.8.4'
implementation 'androidx.lifecycle:lifecycle-livedata:2.8.4'

// Room Database
implementation 'androidx.room:room-runtime:2.6.1'
annotationProcessor 'androidx.room:room-compiler:2.6.1'

// Retrofit & Networking
implementation 'com.squareup.retrofit2:retrofit:2.11.0'
implementation 'com.squareup.retrofit2:converter-gson:2.11.0'
implementation 'com.squareup.okhttp3:okhttp:4.12.0'

// UI/UX
implementation 'com.google.android.material:material:1.10.0'
implementation 'com.github.bumptech.glide:glide:4.16.0'
implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0'

// Testing
testImplementation 'junit:junit:4.13.2'
testImplementation 'org.mockito:mockito-core:5.12.0'
testImplementation 'androidx.arch.core:core-testing:2.2.0'
testImplementation 'com.squareup.okhttp3:mockwebserver:4.12.0'
```
👨‍💻 Author
Riza Zamzami

Role: Junior Mobile Developer
Email: rizazamzami12@gmail.com
Location: Jakarta, Indonesia


🙏 Acknowledgments

DummyJSON - Free API
Material Design - Design system
Glide - Image loading

📞 Support

GitHub Issues: Create Issue
Email: rizazamzami12@gmail.com


📝 Changelog
v1.0.0 (2026-01-12)

Initial release
Product catalog with pagination
Shopping cart with Room persistence
Search functionality
Responsive design
Unit tests coverage
