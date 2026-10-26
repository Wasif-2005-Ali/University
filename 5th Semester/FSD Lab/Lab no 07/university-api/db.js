const mysql = require('mysql');
const util = require('util');
const { DB } = require('./config');

const pool = mysql.createPool(DB);

// query(sql, [params]) -> Promise (so we can use async/await)
const query = util.promisify(pool.query).bind(pool);

module.exports = { pool, query };
