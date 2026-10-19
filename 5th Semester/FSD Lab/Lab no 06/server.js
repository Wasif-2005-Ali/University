const express = require('express');
const cors = require('cors');
const mysql = require('mysql');

const app = express();
const PORT = 3000;

// 1. Database Connection (Port 3309, Database: lab06_db)
const db = mysql.createConnection({
    host: 'localhost',
    port: 3309,
    user: 'root',
    password: '',
    database: 'lab06_db'
});

db.connect((err) => {
    if (err) {
        console.error('Error connecting to MySQL:', err);
        return;
    }
    console.log('Connected to MySQL database via XAMPP on port 3309');
});

// 2. Middleware
app.use(cors());
app.use(express.json());

app.use((req, res, next) => {
    console.log(`[LOG] ${req.method} request made to: ${req.url}`);
    next();
});

// 3. GET API Endpoint (Predefined JSON data for product)
app.get('/api/product', (req, res) => {
    res.json({
        id: 101,
        title: "Karakoram Special Edition",
        description: "Fetched from local Express backend. Premium insulated flask.",
        price: 2999
    });
});

// 4. GET API Endpoint (Fetch all orders from database)
app.get('/api/orders', (req, res) => {
    const sql = 'SELECT * FROM orders ORDER BY id DESC';

    db.query(sql, (err, results) => {
        if (err) {
            console.error('Database error:', err);
            return res.status(500).json({ error: 'Failed to fetch orders' });
        }
        res.json(results);
    });
});

// 5. POST API Endpoint (Save order to database)
app.post('/api/order', (req, res) => {
    const { customerName, customerEmail, theme, engravingText, quantity, totalPrice } = req.body;

    console.log("=== New Order Received ===");
    console.log(req.body);

    const sql = `INSERT INTO orders (customerName, customerEmail, theme, engravingText, quantity, totalPrice) 
                 VALUES (?, ?, ?, ?, ?, ?)`;

    const values = [customerName, customerEmail, theme, engravingText, quantity, totalPrice];

    db.query(sql, values, (err, result) => {
        if (err) {
            console.error('Database error:', err);
            return res.status(500).json({
                status: "error",
                message: "Failed to save order to database"
            });
        }

        console.log('Order saved to database with ID:', result.insertId);

        res.json({
            status: "success",
            message: "Order successfully saved to MySQL database!",
            received_data: req.body
        });
    });
});

app.listen(PORT, () => {
    console.log(`Backend server running at http://localhost:${PORT}`);
});