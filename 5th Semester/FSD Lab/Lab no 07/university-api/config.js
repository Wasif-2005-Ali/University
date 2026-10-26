// Central configuration. Environment variables override the defaults.
module.exports = {
  PORT: process.env.PORT || 3000,

  // Predefined admin API key (sent in the x-api-key header for /api/admin/* routes)
  ADMIN_API_KEY: process.env.ADMIN_API_KEY || 'admin-key-2026',

  // Upper limit an admin may assign to a single key
  MAX_REQUEST_LIMIT: 100000,

  DB: {
    host: process.env.DB_HOST || 'localhost',
    user: process.env.DB_USER || 'root',
    password: process.env.DB_PASS || '',          // default XAMPP password is empty
    database: process.env.DB_NAME || 'university_api',
    connectionLimit: 10
  }
};
