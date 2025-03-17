# A Single Restaurant Management System with Online Ordering

## Description
This project is a complete Restaurant Management System with Online Ordering capabilities. It allows customers to browse the menu, place orders, and track their order status. The system also includes an admin panel for managing menu items, orders, and customers. The backend is built using Spring Boot, providing a RESTful API for seamless integration with the frontend.

### Deployed Frontend Link:
[Coming Soon]

### Swagger API Documentation:
[http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

## Home Page
![Home Page](https://github.com/rahulmk19/A_Single_Restaurant_Management_System_with_Online_Ordering/raw/master/FoodTasteNow_Frontend/Image/Home%20Page.png)

## Admin Dashboard
![Admin Dashboard](https://github.com/rahulmk19/A_Single_Restaurant_Management_System_with_Online_Ordering/raw/master/FoodTasteNow_Frontend/Image/Admin%20Page.png)

## Table of Contents
- [Installation](#installation)
- [Usage](#usage)
- [Technology Stack](#technology-stack)
- [Database ER Diagram](#database-er-diagram)
- [API Endpoints](#api-endpoints)
- [Contributors](#contributors)

## Installation
1. Clone the repository:  
   ```bash
   git clone https://github.com/rahulmk19/A_Single_Restaurant_Management_System_with_Online_Ordering.git
   ```
2. Install Java JDK 17 and Spring Boot.
3. Setup the database (MySQL is recommended).
4. Configure the `application.properties` file with database credentials.
5. Build and run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```

## 🚀 Features
- **User Roles**: Admin, and Customer roles with role-based access control (RBAC).
- **Menu Management**: Add, update, or delete menu items (Admin only).
- **Order Management**: Place, track, and manage orders (Customers & Staff).
- **Cart System**: Customers can add/remove items to/from their cart.
- **JWT Security**: Secure API endpoints with JSON Web Tokens (JWT).
- **Swagger API Docs**: Interactive API documentation for easy testing.

## Usage
### As an Admin, you can:
- Manage menu items and categories.
- View and manage all orders.
- Manage customers and their orders.

### As a Customer, you can:
- Browse the menu and filter items by category.
- Add items to the cart and place an order.
- Track your order status and view order history.

## Technology Stack
- **Backend:** Java, Spring Boot, Spring Data JPA, JWT Security, MySQL
- **Frontend:** HTML, CSS, JavaScript (To be implemented)
- **API Documentation:** Swagger

## API Endpoints

### 👤 Authentication Controller
| Method | Endpoint | Description |
|--------|----------|------------|
| POST | /foodtastenow/public-api/auth/register | User registration |
| POST | /foodtastenow/public-api/auth/login | User login |

### 👥 User Controller
| Method | Endpoint | Description |
|--------|----------|------------|
| PATCH | /foodtastenow/users/admin/changeRole | Change user role (Admin only) |
| GET | /foodtastenow/users/common/profile | Get user profile |
| GET | /foodtastenow/users/admin/getById/{id} | Get user by ID |
| GET | /foodtastenow/users/admin/getAll | Get all users |
| DELETE | /foodtastenow/users/admin/delete/{id} | Delete user |

### 🍔 Menu Item Controller
| Method | Endpoint | Description |
|--------|----------|------------|
| POST | /foodtastenow/items/admin/save | Create menu item |
| PUT | /foodtastenow/items/admin/update/{id} | Update menu item |
| GET | /foodtastenow/items/public-api/getAll | Get all menu items |
| GET | /foodtastenow/items/admin/getById/{id} | Get item by ID |
| DELETE | /foodtastenow/items/admin/delete/{id} | Delete menu item |

### 📦 Order Controller
| Method | Endpoint | Description |
|--------|----------|------------|
| POST | /foodtastenow/orders/user/save | Create order |
| PATCH | /foodtastenow/orders/common/cancel/{id} | Cancel order |
| GET | /foodtastenow/orders/user/getalluserorders | Get user orders |
| GET | /foodtastenow/orders/admin/with-items/{id} | Get order with items |

### 🛒 Cart Controller
| Method | Endpoint | Description |
|--------|----------|------------|
| POST | /foodtastenow/cart/user/save/{menuItemId} | Add to cart |
| DELETE | /foodtastenow/cart/user/delete/{menuItemId} | Remove from cart |
| GET | /foodtastenow/cart/user | View cart |
| POST | /foodtastenow/cart/user/placedorder | Place order |

## Author
Rahul K Thakur  
Contact: rahulmk.94802@gmail.com  
LinkedIn: [Rahul K Thakur](https://www.linkedin.com/in/rahulmk19/)

