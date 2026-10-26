const express = require('express');
const crypto = require('crypto');
const { query } = require('../db');
const { adminAuth } = require('../middleware/auth');
const { MAX_REQUEST_LIMIT } = require('../config');

const router = express.Router();
router.use(adminAuth);                       // every route below needs the admin key

function toInt(v) {
  const n = Number(v);
  return Number.isInteger(n) ? n : null;
}
function validLimit(n) {
  return n !== null && n >= 1 && n <= MAX_REQUEST_LIMIT;
}

const KEY_SELECT = `
  SELECT k.id, k.api_key, k.owner_type,
         COALESCE(k.student_id, k.teacher_id) AS owner_id,
         COALESCE(s.name, t.name)             AS owner_name,
         k.request_limit, k.requests_used,
         GREATEST(k.request_limit - k.requests_used, 0) AS remaining,
         k.status, k.created_at, k.last_used_at
    FROM api_keys k
    LEFT JOIN students s ON s.id = k.student_id
    LEFT JOIN teachers t ON t.id = k.teacher_id`;

/* ---------- View students and teachers ---------- */
router.get('/students', async (req, res, next) => {
  try {
    const rows = await query(
      `SELECT id, roll_no, name, email, department, semester FROM students ORDER BY roll_no`);
    res.json({ data: rows });
  } catch (e) { next(e); }
});

router.get('/teachers', async (req, res, next) => {
  try {
    const rows = await query(`SELECT id, name, email, department FROM teachers ORDER BY name`);
    res.json({ data: rows });
  } catch (e) { next(e); }
});

/* ---------- API keys: list ---------- */
router.get('/keys', async (req, res, next) => {
  try {
    const rows = await query(KEY_SELECT + ' ORDER BY k.id DESC');
    res.json({ data: rows });
  } catch (e) { next(e); }
});

/* ---------- API keys: generate ---------- */
router.post('/keys', async (req, res, next) => {
  try {
    const ownerType = req.body.owner_type;
    const ownerId = toInt(req.body.owner_id);
    const limit = toInt(req.body.request_limit);

    if (!['student', 'teacher'].includes(ownerType)) {
      return res.status(400).json({ message: 'owner_type must be "student" or "teacher"' });
    }
    if (!ownerId || ownerId < 1) {
      return res.status(400).json({ message: 'owner_id must be a valid number' });
    }
    if (!validLimit(limit)) {
      return res.status(400).json({ message: `request_limit must be a whole number between 1 and ${MAX_REQUEST_LIMIT}` });
    }

    // table/column names come from a fixed whitelist, never from user input
    const table = ownerType === 'student' ? 'students' : 'teachers';
    const col = ownerType === 'student' ? 'student_id' : 'teacher_id';

    const owner = await query(`SELECT id, name FROM ${table} WHERE id = ?`, [ownerId]);
    if (!owner.length) {
      return res.status(404).json({ message: `${ownerType} not found` });
    }

    const active = await query(`SELECT id FROM api_keys WHERE ${col} = ? AND status = 'active'`, [ownerId]);
    if (active.length) {
      return res.status(409).json({ message: `${owner[0].name} already has an active key. Revoke it first.` });
    }

    const apiKey = (ownerType === 'student' ? 'stu_' : 'tch_') + crypto.randomBytes(20).toString('hex');
    const result = await query(
      `INSERT INTO api_keys (api_key, owner_type, ${col}, request_limit) VALUES (?,?,?,?)`,
      [apiKey, ownerType, ownerId, limit]);

    res.status(201).json({
      message: `API key generated for ${owner[0].name}`,
      data: { id: result.insertId, api_key: apiKey, owner_type: ownerType,
              owner_id: ownerId, owner_name: owner[0].name, request_limit: limit }
    });
  } catch (e) { next(e); }
});

/* ---------- API keys: update request limit ---------- */
router.put('/keys/:id/limit', async (req, res, next) => {
  try {
    const id = toInt(req.params.id);
    const limit = toInt(req.body.request_limit);
    if (!id) return res.status(400).json({ message: 'Invalid key id' });
    if (!validLimit(limit)) {
      return res.status(400).json({ message: `request_limit must be a whole number between 1 and ${MAX_REQUEST_LIMIT}` });
    }
    const r = await query('UPDATE api_keys SET request_limit = ? WHERE id = ?', [limit, id]);
    if (r.affectedRows === 0) return res.status(404).json({ message: 'API key not found' });
    res.json({ message: 'Request limit updated' });
  } catch (e) { next(e); }
});

/* ---------- API keys: revoke ---------- */
router.put('/keys/:id/revoke', async (req, res, next) => {
  try {
    const id = toInt(req.params.id);
    if (!id) return res.status(400).json({ message: 'Invalid key id' });
    const r = await query(
      `UPDATE api_keys SET status = 'revoked', revoked_at = NOW() WHERE id = ?`, [id]);
    if (r.affectedRows === 0) return res.status(404).json({ message: 'API key not found' });
    res.json({ message: 'API key revoked' });
  } catch (e) { next(e); }
});

/* ---------- API keys: reset usage counter ---------- */
router.put('/keys/:id/reset', async (req, res, next) => {
  try {
    const id = toInt(req.params.id);
    if (!id) return res.status(400).json({ message: 'Invalid key id' });
    const r = await query('UPDATE api_keys SET requests_used = 0 WHERE id = ?', [id]);
    if (r.affectedRows === 0) return res.status(404).json({ message: 'API key not found' });
    res.json({ message: 'Usage counter reset to 0' });
  } catch (e) { next(e); }
});

/* ---------- API keys: usage history ---------- */
router.get('/keys/:id/logs', async (req, res, next) => {
  try {
    const id = toInt(req.params.id);
    if (!id) return res.status(400).json({ message: 'Invalid key id' });
    const rows = await query(
      `SELECT id, method, endpoint, status_code, created_at
         FROM api_logs WHERE api_key_id = ? ORDER BY id DESC LIMIT 25`, [id]);
    res.json({ data: rows });
  } catch (e) { next(e); }
});

module.exports = router;
