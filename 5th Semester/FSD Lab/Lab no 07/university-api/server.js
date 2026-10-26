const express = require('express');
const cors = require('cors');
const path = require('path');
const { PORT } = require('./config');
const { pool } = require('./db');

const app = express();

app.use(cors({
  origin: ['http://localhost', 'http://127.0.0.1', `http://localhost:${PORT}`, `http://127.0.0.1:${PORT}`],
  methods: ['GET', 'POST', 'PUT', 'DELETE'],
  allowedHeaders: ['Content-Type', 'x-api-key'],
  exposedHeaders: ['X-RateLimit-Limit', 'X-RateLimit-Remaining']
}));
app.use(express.json());

/* ---------- API routes ---------- */
app.use('/api/admin', require('./routes/admin'));
app.use('/api', require('./routes/api'));

/* ---------- Frontend (served by Express) ---------- */
const pub = path.join(__dirname, 'public');
app.use('/vendor', express.static(path.join(__dirname, 'node_modules/jquery/dist')));
app.use(express.static(pub));
app.get('/admin', (req, res) => res.sendFile(path.join(pub, 'admin.html')));
app.get('/settings', (req, res) => res.sendFile(path.join(pub, 'settings.html')));
app.get('/student', (req, res) => res.sendFile(path.join(pub, 'student.html')));
app.get('/teacher', (req, res) => res.sendFile(path.join(pub, 'teacher.html')));

/* ---------- 404 + error handling ---------- */
app.use('/api', (req, res) => res.status(404).json({ message: 'API endpoint not found' }));

app.use((err, req, res, next) => {
  if (err.type === 'entity.parse.failed') {
    return res.status(400).json({ message: 'Invalid JSON in request body' });
  }
  console.error('Server error:', err.message);
  res.status(500).json({ message: 'Internal server error: check MySQL connection and tables' });
});

pool.getConnection((err, conn) => {
  if (err) console.error('MySQL connection FAILED:', err.message);
  else { console.log('MySQL connected'); conn.release(); }
});

app.listen(PORT, () => {
  console.log(`University API ready on http://localhost:${PORT}`);
  console.log(`Admin panel: http://localhost:${PORT}/admin`);
});
