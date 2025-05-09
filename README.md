
# 🛒 Wine Purchase Microservice

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

1. Clone the repository
2. Run the application using Maven or your favorite IDE
3. Access Swagger at `http://localhost:8080/swagger-ui/index.html`

## 📫 Submission

Once completed, please send us the link to your GitHub repository containing the full project.

---

Feel free to reach out if you have any questions!
