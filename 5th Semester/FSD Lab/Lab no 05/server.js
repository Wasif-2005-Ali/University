const express = require('express');
const cors = require('cors');

const app = express();
const PORT = 3000;

// 1. CORS Middleware (allows your HTML file to talk to this server)
app.use(cors());

// 2. Built-in Middleware for JSON parsing
app.use(express.json());

// 3. Custom Middleware (logs HTTP method and requested URL)
app.use((req, res, next) => {
    console.log(`[LOG] ${req.method} request made to: ${req.url}`);
    next();
});

// GET API Endpoint (Predefined JSON data)
app.get('/api/product', (req, res) => {
    res.json({
        id: 101,
        title: "Karakoram Special Edition",
        description: "Fetched from local Express backend. Premium insulated flask.",
        price: 2999
    });
});

// POST API Endpoint (Receives the frontend configuration)
app.post('/api/order', (req, res) => {
    const orderData = req.body;

    // Log the received JSON data in the backend terminal"
    console.log("=== New Order Received ===");
    console.log(orderData);

    // Send a JSON response back to the frontend
    res.json({
        status: "success",
        message: "Order successfully processed by Node.js Backend!",
        received_data: orderData
    });
});

app.listen(PORT, () => {
    console.log(`Backend server running at http://localhost:${PORT}`);
});