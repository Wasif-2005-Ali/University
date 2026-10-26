ACCESS PRIVILEGES: PROJECT FOR THE UPDATED 46-SLIDE LECTURE
===========================================================

This project continues the previous custom API lab. It uses your EXISTING
XAMPP database and does not change its tables or stored records.

KEEP THESE SETTINGS UNCHANGED
  Database: mydb8
  Table: customers4
  Columns used: name, address
  MySQL: localhost | root | blank password (from previous lab)
  Node port: 3000
  Student: student-key-123 | max 3 rows | 8 credits
  Teacher: teacher-key-456 | max 10 rows | 30 credits

PROJECT LAYOUT
  XAMPP/htdocs/ajaxdemo/access_ajaxRequest.php
      HTML + CSS + jQuery CDN + AJAX in ONE file.
  NodeJS/access-forRequest.js
      Express API + MySQL connection + checkAccess middleware.
  NodeJS/package.json
      Required packages and start command.
  NodeJS/START_NODE_SERVER.bat
      Windows launcher with first-run npm install.
  Instructor_Guide/SLIDE_TO_CODE_MAP.txt
      Where each concept in the 46 slides appears in code.

START IN 5 STEPS
  1. Copy XAMPP/htdocs/ajaxdemo/access_ajaxRequest.php to:
       C:\xampp\htdocs\ajaxdemo\access_ajaxRequest.php
     Create ajaxdemo folder if it does not exist. Keep your existing
     customer database; do NOT import any new SQL file.

  2. Copy the complete NodeJS folder to a convenient location,
     for example D:\node-projects\Access_Privileges_46_Slide_Project\

  3. Start Apache and MySQL in XAMPP. Confirm mydb8.customers4 already
     contains columns name and address and some customer records.

  4. In the NodeJS folder double-click START_NODE_SERVER.bat.
     Alternatively open Command Prompt in that folder and run:
       npm install
       node access-forRequest.js
     (First npm installation requires internet.)
     Test http://localhost:3000 in your browser.

  5. Open this address in your browser:
       http://localhost/ajaxdemo/access_ajaxRequest.php
     Do not double-click the PHP file. jQuery 3.7.1 comes from a CDN,
     so the browser needs internet access for that script.

CLASSROOM DEMOS MATCHING THE SLIDES
  A. Student + Requested Rows 10 + Load: up to 3 rows, credit 8 -> 7.
  B. Teacher + Requested Rows 10 + Load: up to 10 rows, credit 30 -> 29.
  C. Wrong key + Load: HTTP 401, no query, no credit deduction.
  D. Student + Search Name Ali: finds names containing Ali, max 3 rows.
     Note: Requested Rows has NO effect on Search Customers.
  E. After 8 authorized Student requests, ninth returns HTTP 429.
  F. For 403: temporarily set student allowedTables: [] in NodeJS JS,
     restart Node and request with Student key. Restore afterward.

RESPONSE AND ACCESS RULES
  HTTP 200: valid API key, allowed DB/table, credits present, SQL success.
  HTTP 401: invalid or missing API key.
  HTTP 403: valid key but DB/table permission denied.
  HTTP 429: credits exhausted. Node restart resets counters.
  HTTP 500: server/database error; check MySQL and table columns.

  GET /api/customers?limit=10
     Uses min(requestedRows, user.maxRows).
  GET /api/customers/search?name=Ali
     Uses LIKE ? and LIMIT ?, with the role's maxRows.

IMPORTANT DISTINCTIONS
  - The browser merely REQUESTS data. Only Node grants permission.
  - Database rows are never deleted or modified by this project.
  - Credit is deducted once a request passes checkAccess, even if SQL
    subsequently fails (same behavior as the supplied teaching code).
  - On no matching rows, the API returns an empty list and HTTP 200.
  - Data output uses text() to display MySQL values without injecting HTML.
  - The visual journey is a representation of the response path, not
    a live trace of backend execution timings.
  - The demo keys and MySQL root credentials are classroom-only.
    Do not deploy this setup unchanged to the public internet.

IF SOMETHING DOES NOT WORK
  'jQuery is not defined': CDN didn't load; check internet connection.
  'Network' / CORS error: make sure Node is running on localhost:3000
    and open the PHP page using http://localhost, not file://.
  HTTP 500: check XAMPP MySQL, database mydb8, table customers4,
    and column names name and address.
  Empty table with HTTP 200: database has no rows or search no match.
  HTTP 429: restart the Node server for another classroom demo.
  Port 3000 in use: stop the old Node process before restarting.

===============================================
