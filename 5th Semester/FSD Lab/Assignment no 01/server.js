const express = require('express');
const mysql = require('mysql');
const cors = require('cors');
const multer = require('multer');
const path = require('path');

const app = express();
const port = 3000;

// Middleware
app.use(cors()); // Allows the front-end to communicate with this API
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Expose the uploads folder so the front-end can display the images/GIFs
app.use('/uploads', express.static(path.join(__dirname, 'public/uploads')));

// 1. Database Connection Pool
const con = mysql.createPool({
    host: "localhost",
    user: "root",
    password: "",
    database: "media_gallery_db",
    port: 3309, //
    connectionLimit: 10
});

// 2. Configure Multer for File Uploads
const storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, 'public/uploads/');
    },
    filename: function (req, file, cb) {
        // Generates a unique filename using the current timestamp
        cb(null, Date.now() + path.extname(file.originalname));
    }
});
const upload = multer({ storage: storage });

// 3. Upload Route (POST endpoint)
app.post('/upload', upload.single('mediaFile'), (req, res) => {
    // Extract text fields from the request body
    const { title, media_type, description } = req.body;

    // Extract the newly generated filename from multer
    const file_name = req.file.filename;

    // Insert the record into MySQL securely using placeholders
    const sql = "INSERT INTO media (title, file_name, media_type, description) VALUES (?, ?, ?, ?)";

    con.query(sql, [title, file_name, media_type, description], (err, result) => {
        if (err) {
            console.error(err);
            return res.status(500).json({ message: "Database Error" });
        }
        res.status(200).json({ message: "Media uploaded and saved to database successfully!" });
    });
});

// 4. GET Endpoint for jQuery AJAX
app.get('/media', (req, res) => {
    // Fetch all records, newest first
    const sql = "SELECT * FROM media ORDER BY id DESC";

    con.query(sql, function (err, result) {
        if (err) {
            console.error(err);
            return res.status(500).json({ message: "Database Error" });
        }
        // Returns the database records as a JSON array to the browser
        res.json(result);
    });
});

// Start the server
app.listen(port, () => {
    console.log(`Server running on http://localhost:${port}`);
});