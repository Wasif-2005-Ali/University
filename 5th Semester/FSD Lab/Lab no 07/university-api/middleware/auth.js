const crypto = require('crypto');
const { query } = require('../db');
const { ADMIN_API_KEY } = require('../config');

// Constant-time string comparison (avoids timing attacks)
function safeEqual(a, b) {
  const x = Buffer.from(String(a));
  const y = Buffer.from(String(b));
  return x.length === y.length && crypto.timingSafeEqual(x, y);
}

/* ------------------------------------------------------------------
   adminAuth: protects every /api/admin/* endpoint
   401 -> header missing or key unknown
   403 -> key is a valid student/teacher key, but not the admin key
------------------------------------------------------------------- */
async function adminAuth(req, res, next) {
  try {
    const key = req.get('x-api-key');
    if (!key) {
      return res.status(401).json({ message: 'Unauthorized: x-api-key header is missing' });
    }
    if (safeEqual(key, ADMIN_API_KEY)) return next();

    const rows = await query('SELECT id FROM api_keys WHERE api_key = ?', [key]);
    if (rows.length) {
      return res.status(403).json({ message: 'Forbidden: admin API key required' });
    }
    return res.status(401).json({ message: 'Unauthorized: invalid admin API key' });
  } catch (err) {
    next(err);
  }
}

/* ------------------------------------------------------------------
   authenticate(roles, { consume }): protects student / teacher endpoints
   Steps:
     1. read x-api-key header                       -> 401 if missing
     2. find key in database                        -> 401 if unknown
     3. key must be active                          -> 403 if revoked
     4. key role must be allowed on this route      -> 403 if wrong role
     5. atomically consume one request              -> 429 if limit reached
   Every identified request is written to api_logs.
------------------------------------------------------------------- */
function authenticate(roles, options = {}) {
  const consume = options.consume !== false;

  return async function (req, res, next) {
    try {
      const key = req.get('x-api-key');
      if (!key) {
        return res.status(401).json({ message: 'Unauthorized: x-api-key header is missing' });
      }

      const rows = await query(
        `SELECT id, api_key, owner_type, student_id, teacher_id,
                request_limit, requests_used, status
           FROM api_keys WHERE api_key = ?`, [key]);

      if (!rows.length) {
        return res.status(401).json({ message: 'Unauthorized: invalid API key' });
      }
      const k = rows[0];
      k.owner_id = k.student_id || k.teacher_id;

      // Log the outcome of every request made with a known key
      res.on('finish', () => {
        query('INSERT INTO api_logs (api_key_id, method, endpoint, status_code) VALUES (?,?,?,?)',
          [k.id, req.method, req.originalUrl.split('?')[0], res.statusCode]).catch(() => {});
      });

      if (k.status === 'revoked') {
        return res.status(403).json({ message: 'Forbidden: this API key has been revoked' });
      }
      if (!roles.includes(k.owner_type)) {
        return res.status(403).json({ message: `Forbidden: ${k.owner_type} keys cannot access this endpoint` });
      }

      if (consume) {
        // Atomic update: increments only if the key is still active and below its limit.
        const result = await query(
          `UPDATE api_keys
              SET requests_used = requests_used + 1, last_used_at = NOW()
            WHERE id = ? AND status = 'active' AND requests_used < request_limit`, [k.id]);

        if (result.affectedRows === 0) {
          const again = await query('SELECT status FROM api_keys WHERE id = ?', [k.id]);
          if (again.length && again[0].status === 'revoked') {
            return res.status(403).json({ message: 'Forbidden: this API key has been revoked' });
          }
          return res.status(429).json({ message: 'Too Many Requests: API request limit reached' });
        }
        k.requests_used += 1;
      }

      const remaining = Math.max(k.request_limit - k.requests_used, 0);
      res.set('X-RateLimit-Limit', String(k.request_limit));
      res.set('X-RateLimit-Remaining', String(remaining));

      req.apiKey = k;
      req.usage = { limit: k.request_limit, used: k.requests_used, remaining };
      next();
    } catch (err) {
      next(err);
    }
  };
}

module.exports = { adminAuth, authenticate };
