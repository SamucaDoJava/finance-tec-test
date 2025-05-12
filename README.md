
# 🛒 Finance Tec Test
## Wine Purchase Microservice

This microservice is responsible for processing, analyzing and recommending data based on wine purchase records. It was developed to simulate business logic over mocked purchase and client data, allowing insights and recommendations over client behavior and product consumption.

## 📌 What This Application Does

The application exposes REST endpoints that allow the user to retrieve and analyze purchase information. It does not rely on a database; instead, it consumes mocked data and processes it in-memory. The goal is to simulate realistic behavior of a wine shop analytics system.

Once completed, the microservice will:

- Expose endpoints to retrieve all purchases in an ordered manner.
- Allow querying for the biggest purchase made in a specific year.
- Return the most loyal clients based on purchase frequency and value.
- Recommend wine types to clients based on their past preferences.

## 🚀 Endpoints

### `GET /compras`
Returns a list of all purchases sorted in ascending order by total purchase value.

**Response includes:**
- Client name and CPF
- Product details
- Quantity purchased
- Total purchase value

---

### `GET /maior-compra/{ano}`
Returns the biggest purchase made in the specified year.

**Response includes:**
- Client name and CPF
- Product details
- Quantity purchased
- Total value of that purchase

---

### `GET /clientes-fieis`
Returns the Top 3 most loyal clients based on the number of recurring and high-value purchases.

---

### `GET /recomendacao/{cliente}/{tipo}`
Returns a wine recommendation based on the wine types that the specified client purchases most frequently.

---

## 📦 How to Run

# With docker mode
1. Test this application using a public image of dockerhub of this application access the follow link
2. dockerhub link:
3. with docker installed in your machine run `````docker compose up````` command for create docker instance image.
4. Access the follow swagger url for access the all endpoints of application.
5. Swagger URL:

# With dev mod
🚨
Please note, if you are not using Docker and are unable to create a Redis image with the default port 6379
this application will not work properly.

1. Clone the repository
2. Run the application using Maven or your favorite IDE
3. Access Swagger at `http://localhost:8080/swagger-ui/index.html`

## 📌 About application construction
All code created and encoded with english sintaxe.
This application using default JDK 22 by default and Springboot version 3.4
This application is all created of SOLID principal.
The entire system is covered by a layer of unit and integration tests using Junity / Mokito.
The external endpoints used in the application use cached data from REDIS to help with application performance and performance.

