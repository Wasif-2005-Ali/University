# University API Access Management System (Lab 07)

Node.js + Express + MySQL (XAMPP) + jQuery AJAX

## Run (4 steps)
1. XAMPP Control Panel -> Start **MySQL**
2. phpMyAdmin -> Import -> choose `database/university_api.sql` -> Go
3. Terminal in this folder:  `npm install`  then  `node server.js`
4. Open http://localhost:3000

## Pages
| URL | Who |
|---|---|
| /admin | Administrator (admin key: `admin-key-2026`, change in config.js) |
| /settings | Student / Teacher saves their API key |
| /student | Student dashboard (own marks only) |
| /teacher | Teacher dashboard (all students) |

## API endpoints (key goes in `x-api-key` header)
Admin: GET /api/admin/students, GET /api/admin/teachers, GET /api/admin/keys, POST /api/admin/keys,
PUT /api/admin/keys/:id/limit, PUT /api/admin/keys/:id/revoke, PUT /api/admin/keys/:id/reset, GET /api/admin/keys/:id/logs
Student: GET /api/student/me, GET /api/student/marks
Teacher: GET /api/teacher/me, GET /api/teacher/students, GET /api/teacher/marks
Shared: GET /api/whoami (free), GET /api/students/:id/marks (student -> only own id, else 403)
