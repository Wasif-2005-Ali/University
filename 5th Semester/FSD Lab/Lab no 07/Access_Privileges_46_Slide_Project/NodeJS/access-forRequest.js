// ACCESS PRIVILEGES — Full Stack Web Development (46-slide lecture)
// FIXED FOR PORT 3309 + AUTO-DATABASE SETUP + ALL LOCALHOST PORTS

const express = require('express');
const mysql = require('mysql');
const cors = require('cors');
const path = require('path');
const fs = require('fs');
const app = express();

// Allow ANY localhost port (80, 8080, 3000, etc.) so CORS never blocks AJAX
app.use(cors({
  origin: true,
  methods: ['GET', 'POST'],
  allowedHeaders: ['Content-Type', 'x-api-key']
}));
app.use(express.json());

const DATABASE = 'mydb8';
const TABLE = 'customers4';

// Two example user profiles (classroom use only).
const USERS = {
  'student-key-123': {
    role: 'student',
    allowedDatabases: ['mydb8'],
    allowedTables: ['customers4'],
    maxRows: 3,
    credits: 8
  },
  'teacher-key-456': {
    role: 'teacher',
    allowedDatabases: ['mydb8'],
    allowedTables: ['customers4'],
    maxRows: 10,
    credits: 30
  }
};

const SQL_ROWS = 'SELECT name, address FROM customers4';
const SQL_SEARCH = SQL_ROWS + ' WHERE name LIKE ? LIMIT ?';

// 1. Auto-create mydb8 and customers4 on Port 3309 at startup
const setupConn = mysql.createConnection({
  host: '127.0.0.1',
  port: 3309,
  user: 'root',
  password: '',
  multipleStatements: true
});

const initSql = `
CREATE DATABASE IF NOT EXISTS mydb8 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mydb8;
CREATE TABLE IF NOT EXISTS customers4 (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
`;

setupConn.connect(function (err) {
  if (err) {
    console.error('❌ Cannot connect to MySQL on port 3309:', err.message);
    return;
  }
  setupConn.query(initSql, function (err) {
    if (err) {
      console.error('❌ Error creating database/table:', err.message);
      setupConn.end();
      return;
    }
    setupConn.query('SELECT COUNT(*) AS count FROM mydb8.customers4', function (err, results) {
      if (!err && results[0].count === 0) {
        const seedSql = `
          INSERT INTO mydb8.customers4 (name, address) VALUES
          ('Ali Khan', '12 Main Street, Lahore'),
          ('Ayesha Ahmed', '45 Green Road, Karachi'),
          ('Bilal Hussain', '78 Garden Avenue, Islamabad'),
          ('Sara Malik', '21 Park Lane, Rawalpindi'),
          ('Omar Farooq', '9 University Road, Peshawar'),
          ('Hina Shah', '33 Canal View, Faisalabad'),
          ('Usman Tariq', '17 Model Town, Lahore'),
          ('Maryam Iqbal', '64 Sea View Road, Karachi'),
          ('Hamza Raza', '5 Blue Area, Islamabad'),
          ('Noor Fatima', '28 Saddar Road, Rawalpindi');
        `;
        setupConn.query(seedSql, function () {
          console.log('✅ Database mydb8 & 10 customers automatically created!');
          setupConn.end();
        });
      } else {
        console.log('✅ Database mydb8 and customers4 are ready! (' + (results ? results[0].count : 10) + ' rows)');
        setupConn.end();
      }
    });
  });
});

// 2. Main connection pool on Port 3309
const db = mysql.createPool({
  host: '127.0.0.1',
  port: 3309,
  user: 'root',
  password: '',
  database: DATABASE,
  connectionLimit: 10
});

// Permission checks happen before SQL.
function checkAccess(req, res, next) {
  const key = req.get('x-api-key');
  const user = Object.prototype.hasOwnProperty.call(USERS, key) ? USERS[key] : null;

  if (!user) {
    return res.status(401).json({ message: 'Unauthorized: invalid API key' });
  }
  if (!user.allowedDatabases.includes(DATABASE)) {
    return res.status(403).json({ message: 'Forbidden: database access denied' });
  }
  if (!user.allowedTables.includes(TABLE)) {
    return res.status(403).json({ message: 'Forbidden: table access denied' });
  }
  if (user.credits <= 0) {
    return res.status(429).json({ message: 'API credits finished' });
  }

  user.credits = user.credits - 1;
  req.currentUser = user;
  next();
}

function getAllowedRows(input, maxRows) {
  let requested = Number(input);
  if (!Number.isInteger(requested) || requested < 1) requested = maxRows;
  return Math.min(requested, maxRows);
}

// Serve the lab UI directly at http://localhost:3000/app as a backup if Apache hangs!
app.get('/app', function (req, res) {
  const phpPath1 = path.join(__dirname, '..', 'XAMPP', 'htdocs', 'ajaxdemo', 'access_ajaxRequest.php');
  const phpPath2 = 'C:/xampp/htdocs/ajaxdemo/access_ajaxRequest.php';
  if (fs.existsSync(phpPath1)) {
    res.type('html').send(fs.readFileSync(phpPath1, 'utf8'));
  } else if (fs.existsSync(phpPath2)) {
    res.type('html').send(fs.readFileSync(phpPath2, 'utf8'));
  } else {
    res.send('Could not find access_ajaxRequest.php');
  }
});

// Server-running check
app.get('/', function (req, res) {
  res.send('<h2>Access Privileges API is running</h2>' +
           '<p>Database: ' + DATABASE + ' | Table: ' + TABLE + ' (Port 3309)</p>' +
           '<p><a href="/app" style="font-size:18px;font-weight:bold;">👉 Click Here to Open Your Lab Project Page</a></p>');
});

// LOAD route
app.get('/api/customers', checkAccess, function (req, res) {
  const user = req.currentUser;
  const limit = getAllowedRows(req.query.limit, user.maxRows);

  db.query(SQL_ROWS + ' LIMIT ?', [limit], function (err, rows) {
    if (err) {
      console.error('MySQL load error:', err.message);
      return res.status(500).json({ message: 'Database error: ' + err.message });
    }

    res.json({
      meta: {
        role: user.role,
        allowedRows: limit,
        remainingCredits: user.credits,
        database: DATABASE,
        table: TABLE
      },
      data: rows
    });
  });
});

// SEARCH route
app.get('/api/customers/search', checkAccess, function (req, res) {
  const user = req.currentUser;
  const name = String(req.query.name || '');
  const pattern = '%' + name + '%';
  const limit = user.maxRows;

  db.query(SQL_SEARCH, [pattern, limit], function (err, rows) {
    if (err) {
      console.error('MySQL search error:', err.message);
      return res.status(500).json({ message: 'Database error: ' + err.message });
    }

    res.json({
      meta: {
        role: user.role,
        allowedRows: limit,
        remainingCredits: user.credits,
        search: name,
        database: DATABASE,
        table: TABLE
      },
      data: rows
    });
  });
});

app.listen(3000, function () {
  console.log('====================================================');
  console.log('🚀 Access Privileges API ready: http://localhost:3000');
  console.log('👉 Open Lab UI directly:        http://localhost:3000/app');
  console.log('👉 Or via XAMPP Apache (8080):  http://localhost:8080/ajaxdemo/access_ajaxRequest.php');
  console.log('====================================================');
});