# Getting Started

Clone the repository   git clone https://github.com/sohel72989/bulk-product-image-upload.git

****************************for beackend**************************************

<!-- =================================# Features:================== -->

1. Upload multiple product images at once.

2. Each product has its own folder (product-1, product-2, …).

3. Product metadata stored as product.json inside the folder.

4. Image stored inside the same folder with proper naming.

5. Get all product details via API.

6. Serve product images directly in the browser.

<!-- ===========================# Tech Stack================== -->

# Java 8   // jdk 1.8

# Spring Boot

# Maven

# Jackson (for JSON serialization)

# File system storage (no database required)


<!-- ========== How to Run ========= -->

1. Clone the repository.
2. Build the project with Maven:  mvn clean install
3. Run the application:           mvn spring-boot:run
4. The app will run on: http://localhost:8080





# Use Postman or frontend to test:

1. Upload multiple images via POST /api/products/upload/images.

curl --location 'http://localhost:8080/api/products/upload/images' \
--form 'files=@"/C:/Users/User/Downloads/file (1).jpg"' \
--form 'files=@"/C:/Users/User/Downloads/Love Phone Wallpaper.png"'


2. View product list via GET /api/products/all/projucts.
curl --location 'http://localhost:8080/api/products/all/projucts'

3. Open product images via GET /api/products/image/{folder}/{filename}.

curl --location 'http://localhost:8080/api/products/image/product-1/product-1_photo.jpg' \
--header 'Authorization: Basic bXljbGllbnQ6MTIzNDU2'




****************************************For frontent***********************************************


 # cd product-upload-frontend/
 # npm install
 # npm run dev
 # The app will run on: http://localhost:3000

