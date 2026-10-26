const express = require('express');
const { query } = require('../db');
const { authenticate } = require('../middleware/auth');

const router = express.Router();

const studentOnly = authenticate(['student']);
const teacherOnly = authenticate(['teacher']);
const anyUser = authenticate(['student', 'teacher']);

const MARKS_SQL = `
  SELECT s.id AS student_id, s.roll_no, s.name AS student_name,
         c.code AS course_code, c.title AS course_title,
         m.assignment, m.midterm, m.final_exam,
         (m.assignment + m.midterm + m.final_exam) AS total
    FROM marks m
    JOIN students s ON s.id = m.student_id
    JOIN courses  c ON c.id = m.course_id`;

const STUDENT_SQL = 'SELECT id, roll_no, name, email, department, semester FROM students';
const TEACHER_SQL = 'SELECT id, name, email, department FROM teachers';

/* ---------- Verify a key (used by the settings page, does NOT use a request) ---------- */
router.get('/whoami', authenticate(['student', 'teacher'], { consume: false }), async (req, res, next) => {
  try {
    const k = req.apiKey;
    const rows = k.owner_type === 'student'
      ? await query(STUDENT_SQL + ' WHERE id = ?', [k.owner_id])
      : await query(TEACHER_SQL + ' WHERE id = ?', [k.owner_id]);
    res.json({ role: k.owner_type, profile: rows[0], usage: req.usage });
  } catch (e) { next(e); }
});

/* ---------- Student endpoints (identity comes from the key, never from the URL) ---------- */
router.get('/student/me', studentOnly, async (req, res, next) => {
  try {
    const rows = await query(STUDENT_SQL + ' WHERE id = ?', [req.apiKey.owner_id]);
    res.json({ data: rows[0], usage: req.usage });
  } catch (e) { next(e); }
});

router.get('/student/marks', studentOnly, async (req, res, next) => {
  try {
    const rows = await query(MARKS_SQL + ' WHERE m.student_id = ? ORDER BY c.code', [req.apiKey.owner_id]);
    res.json({ data: rows, usage: req.usage });
  } catch (e) { next(e); }
});

/* ---------- Teacher endpoints ---------- */
router.get('/teacher/me', teacherOnly, async (req, res, next) => {
  try {
    const rows = await query(TEACHER_SQL + ' WHERE id = ?', [req.apiKey.owner_id]);
    res.json({ data: rows[0], usage: req.usage });
  } catch (e) { next(e); }
});

router.get('/teacher/students', teacherOnly, async (req, res, next) => {
  try {
    const rows = await query(STUDENT_SQL + ' ORDER BY roll_no');
    res.json({ data: rows, usage: req.usage });
  } catch (e) { next(e); }
});

router.get('/teacher/marks', teacherOnly, async (req, res, next) => {
  try {
    const rows = await query(MARKS_SQL + ' ORDER BY s.roll_no, c.code');
    res.json({ data: rows, usage: req.usage });
  } catch (e) { next(e); }
});

/* ---------- Shared endpoint with an id in the URL ----------
   Teacher: any student.  Student: only their own id, otherwise 403.
   This is what stops "change the id in the URL" attacks.            */
router.get('/students/:id/marks', anyUser, async (req, res, next) => {
  try {
    const id = Number(req.params.id);
    if (!Number.isInteger(id) || id < 1) {
      return res.status(400).json({ message: 'Invalid student id' });
    }
    if (req.apiKey.owner_type === 'student' && id !== req.apiKey.owner_id) {
      return res.status(403).json({ message: 'Forbidden: you can only view your own records' });
    }
    const student = await query(STUDENT_SQL + ' WHERE id = ?', [id]);
    if (!student.length) return res.status(404).json({ message: 'Student not found' });

    const marks = await query(MARKS_SQL + ' WHERE m.student_id = ? ORDER BY c.code', [id]);
    res.json({ student: student[0], data: marks, usage: req.usage });
  } catch (e) { next(e); }
});

module.exports = router;
