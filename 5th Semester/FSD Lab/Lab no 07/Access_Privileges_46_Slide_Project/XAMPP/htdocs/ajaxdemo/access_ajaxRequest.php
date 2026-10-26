<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Access Privileges | Full Stack Web Development</title>
  <!-- One PHP file contains HTML, CSS and jQuery AJAX, as in the prior lab. -->
  <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
  <style>
    :root { --navy:#102942; --dark:#0c2135; --teal:#168d89; --blue:#2878c8; --canvas:#f3f7fa; --text:#263a4e; --muted:#617488; --line:#dce6eb; --white:#fff; --green:#167b5a; --red:#ad3d43; }
    * { box-sizing:border-box; }
    body { margin:0; color:var(--text); background:var(--canvas); font:15px/1.55 "Segoe UI",Arial,sans-serif; }
    .page { max-width:1180px; margin:0 auto; padding:29px 21px 46px; }
    .eyebrow { text-transform:uppercase; letter-spacing:2px; font-size:11px; font-weight:750; }
    .hero { color:white; padding:26px 29px 25px; background:linear-gradient(110deg,var(--dark),#174460); border-radius:15px; display:grid; grid-template-columns:1.4fr 1fr; gap:25px; align-items:center; }
    .hero .eyebrow { color:#7be2d9; }
    h1 { margin:5px 0 9px; font-size:34px; line-height:1.13; letter-spacing:-.8px; }
    .hero p { margin:0; color:#d1e1ed; max-width:500px; }
    .hero-graphic { display:flex; align-items:center; gap:9px; justify-content:space-between; }
    .hero-graphic .square { flex:1; min-width:0; background:#ffffff12; border:1px solid #ffffff3a; padding:16px 7px; text-align:center; border-radius:11px; font-weight:650; font-size:13px; }
    .hero-graphic .arrow { color:#7be2d9; font-size:22px; }
    .section-title { display:flex; align-items:baseline; justify-content:space-between; flex-wrap:wrap; gap:8px; margin:27px 0 12px; }
    .section-title h2 { font-size:20px; margin:0; color:var(--navy); }
    .section-title .hint { font-size:13px; color:var(--muted); }
    .card { background:white; border:1px solid var(--line); border-radius:13px; box-shadow:0 6px 26px #0b325108; padding:23px; }
    .form-grid { display:grid; grid-template-columns:1.05fr .7fr 1fr; gap:19px; }
    label { font-size:13px; font-weight:750; display:block; color:#2c4053; margin-bottom:6px; }
    input, select { appearance:auto; width:100%; border:1px solid #bfcfda; background:#fff; border-radius:8px; padding:10px 12px; font:15px "Segoe UI",Arial,sans-serif; height:45px; }
    input:focus,select:focus { outline:2px solid #8bd7d2; border-color:var(--teal); outline-offset:1px; }
    .micro { color:var(--muted); font-size:12px; margin-top:6px; }
    .actions { display:flex; gap:11px; flex-wrap:wrap; margin-top:22px; }
    button { cursor:pointer; border:0; border-radius:8px; padding:12px 19px; min-height:43px; font:700 14px "Segoe UI",Arial,sans-serif; background:var(--navy); color:#fff; }
    button.secondary { color:#0d6674; background:#e2f4f3; border:1px solid #bde5df; }
    button:hover { filter:brightness(1.07); }
    button:disabled { opacity:.6; cursor:wait; }
    .notice { padding:11px 15px; border-radius:9px; margin-top:17px; border:1px solid #d2e3f2; background:#edf5fc; color:#235177; }
    .notice.ok { background:#e9f7f2; border-color:#bbe7d3; color:var(--green); }
    .notice.fail { background:#fff1f2; border-color:#f0cbd0; color:var(--red); }
    .journey { display:grid; grid-template-columns:repeat(6,minmax(0,1fr)); gap:6px; margin:15px 0 0; }
    .step { position:relative; border:1px solid var(--line); background:#f7fafc; padding:12px 6px 10px; min-height:93px; border-radius:9px; text-align:center; }
    .step .num { display:inline-grid; place-items:center; width:24px; height:24px; background:#e4ebf1; color:#4c657c; border-radius:50%; font-weight:750; font-size:12px; }
    .step b { display:block; font-size:13px; margin-top:6px; color:#284358; }
    .step small { color:#7990a2; font-size:11px; display:block; }
    .step.done { background:#e9f7f3; border-color:#9bd5c4; }
    .step.done .num { background:var(--teal); color:white; }
    .step.denied { background:#fff0f1; border-color:#efb4b7; }
    .step.denied .num { background:#c64c55; color:white; }
    .step.active { background:#e7f2fc; border-color:#84b9e4; }
    .step.active .num { background:var(--blue); color:white; }
    .legend { color:#62778a; font-size:12px; margin-top:10px; }
    .stats { display:grid; grid-template-columns:repeat(4,1fr); gap:12px; margin-top:16px; }
    .stat { background:#f5f9fb; border:1px solid #e0ebf0; border-radius:10px; padding:11px 14px; }
    .stat span { font-size:12px; color:#657e90; display:block; }
    .stat strong { display:block; font-size:25px; line-height:1.5; color:var(--navy); }
    .two-cols { display:grid; grid-template-columns:minmax(0,1.25fr) minmax(0,.75fr); gap:18px; margin-top:18px; align-items:start; }
    .two-cols .card { min-width:0; }
    .panelhead { margin:0 0 8px; font-size:16px; color:var(--navy); }
    .panelhint { color:var(--muted); font-size:13px; margin:0 0 13px; }
    table { width:100%; border-collapse:collapse; }
    th { background:#153b5b; color:white; text-align:left; padding:11px 14px; font-size:13px; }
    td { padding:10px 14px; border-bottom:1px solid #e9eff3; }
    tbody tr:nth-child(even) td { background:#f5f8fa; }
    .empty { color:#8795a2; font-style:italic; text-align:center; padding:27px 14px; }
    .table-scroll { overflow-x:auto; }
    .tag { display:inline-block; border-radius:6px; background:#e5f1f4; padding:3px 8px; color:#26596c; font-size:12px; font-weight:750; }
    .requestline { font-family:Consolas,"Courier New",monospace; background:#132e46; color:#e7f8ff; white-space:pre-wrap; overflow-wrap:anywhere; max-height:300px; overflow-y:auto; padding:13px; border-radius:8px; margin:8px 0 0; font-size:12px; line-height:1.75; }
    .binding { border-left:3px solid var(--teal); background:#f4faf9; padding:9px 12px; margin:12px 0 0; color:#375568; border-radius:2px 6px 6px 2px; font-size:13px; }
    .rule-grid { display:grid; grid-template-columns:repeat(3,1fr); gap:12px; }
    .rule { border:1px solid var(--line); padding:12px; border-radius:9px; }
    .rule strong { display:block; color:var(--navy); font-size:14px; }
    .rule span { display:block; font-size:12px; color:var(--muted); margin-top:2px; }
    .mini { color:#576b7e; font-size:13px; margin-top:14px; }
    .footer { margin-top:28px; font-size:12px; color:#7b8d9a; display:flex; justify-content:space-between; gap:10px; flex-wrap:wrap; }
    @media(max-width:900px) { .hero {grid-template-columns:1fr} .two-cols {grid-template-columns:1fr} }
    @media(max-width:680px) { .page { padding:13px 12px 28px} .hero {padding:23px 17px} h1 {font-size:27px} .form-grid {grid-template-columns:1fr} .journey {grid-template-columns:repeat(3,1fr)} .stats {grid-template-columns:repeat(2,1fr)} .rule-grid {grid-template-columns:1fr} .card {padding:17px} }
  </style>
</head>
<body>
  <main class="page">
    <header class="hero">
      <div>
        <div class="eyebrow">Full Stack Web Development · Node.js + AJAX + XAMPP</div>
        <h1>Access Privileges</h1>
        <p>One custom API. Two user roles. Node.js decides which records the browser can receive.</p>
      </div>
      <div class="hero-graphic" aria-label="Application structure">
        <div class="square">Browser<br>jQuery AJAX</div><span class="arrow">→</span>
        <div class="square">Node.js<br>checkAccess</div><span class="arrow">→</span>
        <div class="square">MySQL<br>customers4</div>
      </div>
    </header>

    <div class="section-title"><h2>1. Send a request</h2><span class="hint">Slides 8–12, 17–18 and 25–30</span></div>
    <section class="card">
      <div class="form-grid">
        <div><label for="apiKey">API key / role</label>
          <select id="apiKey">
            <option value="student-key-123">Student · 3 rows · 8 credits</option>
            <option value="teacher-key-456">Teacher · 10 rows · 30 credits</option>
            <option value="wrong-key">Wrong key · HTTP 401</option>
          </select>
          <div class="micro">Demo keys identify a client; they are not MySQL passwords.</div>
        </div>
        <div><label for="rowLimit">Requested rows</label>
          <input id="rowLimit" type="number" min="1" max="100" value="10">
          <div class="micro">Used by Load Customers only.</div>
        </div>
        <div><label for="searchName">Search customer name</label>
          <input id="searchName" type="text" placeholder="Example: Ali" maxlength="100">
          <div class="micro">Search uses the role maximum (3 or 10 rows).</div>
        </div>
      </div>
      <div class="actions">
        <button id="loadCustomers" type="button">Load Customers</button>
        <button id="searchCustomers" class="secondary" type="button">Search Customers</button>
      </div>
      <div class="notice" id="status" role="status" aria-live="polite">Ready. Choose a role and press Load Customers.</div>
      <div class="stats" aria-label="API response summary">
        <div class="stat"><span>Role</span><strong id="roleText">—</strong></div>
        <div class="stat"><span>Allowed rows</span><strong id="allowedText">—</strong></div>
        <div class="stat"><span>Credits left</span><strong id="creditsText">—</strong></div>
        <div class="stat"><span>Rows received</span><strong id="countText">—</strong></div>
      </div>
    </section>

    <div class="section-title"><h2>2. Follow the request</h2><span class="hint">Slides 10–16, 23, 31–34 and 42</span></div>
    <section class="card">
      <p class="panelhint">The steps below reflect the request outcome. A rejected request stops before MySQL.</p>
      <div class="journey" id="journey">
        <div class="step" data-step="1"><span class="num">1</span><b>Click</b><small>Button pressed</small></div>
        <div class="step" data-step="2"><span class="num">2</span><b>AJAX</b><small>GET + key</small></div>
        <div class="step" data-step="3"><span class="num">3</span><b>Access</b><small>3 checks</small></div>
        <div class="step" data-step="4"><span class="num">4</span><b>MySQL</b><small>SELECT rows</small></div>
        <div class="step" data-step="5"><span class="num">5</span><b>JSON</b><small>meta + data</small></div>
        <div class="step" data-step="6"><span class="num">6</span><b>Table</b><small>Display rows</small></div>
      </div>
      <p class="legend"><span class="tag">Access checks</span> API key (401) → database/table permission (403) → credits (429).</p>
      <div class="two-cols">
        <div>
          <h3 class="panelhead">Request sent from the browser</h3>
          <div class="requestline" id="requestText">GET /api/customers?limit=10
x-api-key: student-key-123</div>
        </div>
        <div>
          <h3 class="panelhead">What the server checks</h3>
          <div class="rule-grid">
            <div class="rule"><strong>Who?</strong><span>API key</span></div>
            <div class="rule"><strong>What?</strong><span>mydb8 / customers4</span></div>
            <div class="rule"><strong>How much?</strong><span>Rows + credits</span></div>
          </div>
        </div>
      </div>
    </section>

    <div class="section-title"><h2>3. View the response</h2><span class="hint">Slides 19–20 and 32–39</span></div>
    <div class="two-cols" style="margin-top:0">
      <section class="card">
        <h3 class="panelhead">Customer records</h3>
        <p class="panelhint">Only the <b>name</b> and <b>address</b> columns are returned.</p>
        <div class="table-scroll">
          <table><thead><tr><th>Name</th><th>Address</th></tr></thead>
            <tbody id="customerTable"><tr><td class="empty" colspan="2">No request yet.</td></tr></tbody>
          </table>
        </div>
      </section>
      <section class="card">
        <h3 class="panelhead">Why these rows?</h3>
        <p class="panelhint">A small representation of the SQL values (Slides 18 and 26–28).</p>
        <div class="binding" id="sqlExplain">Load: requested rows → Math.min(requested, maxRows) → SQL LIMIT ?</div>
        <h3 class="panelhead" style="margin-top:18px">Server response</h3>
        <div class="requestline" id="responseText">Waiting for the first response…</div>
      </section>
    </div>

    <div class="section-title"><h2>4. Compare the two roles</h2><span class="hint">Slides 8–9 and 35–41</span></div>
    <section class="card">
      <div class="rule-grid">
        <div class="rule"><strong>Student</strong><span>Max 3 rows · 8 authorized requests</span></div>
        <div class="rule"><strong>Teacher</strong><span>Max 10 rows · 30 authorized requests</span></div>
        <div class="rule"><strong>Wrong key</strong><span>HTTP 401 · SQL not executed</span></div>
      </div>
      <p class="mini">Try: Student + 10 rows → Teacher + 10 rows → Wrong key → Search “Ali”. Use <b>F12 → Network</b> to inspect the header, status and response JSON. To demonstrate 403, temporarily remove <code>customers4</code> from a role’s <code>allowedTables</code> in Node.js and restart the server.</p>
    </section>
    <div class="footer"><span>Instructor project companion · 46-slide Access Privileges lecture</span><span>Local classroom demonstration · No database modifications</span></div>
  </main>

  <!-- jQuery AJAX: Browser side. Node.js access rules are in the SEPARATE JS file. -->
  <script>
  $(function () {
    const API = 'http://localhost:3000';

    // Slides 33 and 35-38: each JSON data item becomes one table row.
    function showRows(rows) {
      const table = $('#customerTable').empty();
      $.each(rows, function (_, customer) {
        const tr = $('<tr>');
        $('<td>').text(customer.name).appendTo(tr);
        $('<td>').text(customer.address).appendTo(tr);
        table.append(tr);
      });
      if (!rows.length) {
        table.append('<tr><td colspan="2" class="empty">No matching customers.</td></tr>');
      }
    }

    // Slide 34: six stages shown in the same order as the lecture flow.
    // This visual is derived from the HTTP outcome, not internal timing.
    function setJourney(done, failed) {
      $('.step').removeClass('active done denied');
      $('.step').each(function (i) {
        const stage = i + 1;
        if (stage <= done) $(this).addClass('done');
        if (stage === failed) $(this).addClass('denied');
      });
    }

    function showStats(meta, rows) {
      $('#roleText').text(meta.role);
      $('#allowedText').text(meta.allowedRows);
      $('#creditsText').text(meta.remainingCredits);
      $('#countText').text(rows.length);
    }
    function resetOutput() {
      $('#roleText,#allowedText,#creditsText,#countText').text('—');
      $('#customerTable').html('<tr><td class="empty" colspan="2">No records displayed.</td></tr>');
    }

    function showSqlValues(type, response, name, limit) {
      if (type === 'load') {
        $('#sqlExplain').text('Load: ' + limit + ' requested → ' + response.meta.allowedRows +
          ' allowed → LIMIT ? = ' + response.meta.allowedRows);
      } else {
        const pattern = '%' + name + '%';
        $('#sqlExplain').text('Search: “' + name + '” → “' + pattern +
          '” → LIKE ? → LIMIT ? = ' + response.meta.allowedRows);
      }
    }

    // Slides 11-12, 30-32: send an API key in headers, not a MySQL password.
    function sendRequest(type) {
      const key = $('#apiKey').val();
      const limit = $('#rowLimit').val();
      const name = $('#searchName').val().trim();
      const path = type === 'load'
        ? '/api/customers?limit=' + encodeURIComponent(limit)
        : '/api/customers/search?name=' + encodeURIComponent(name);

      $('#requestText').text('GET ' + path + '\nx-api-key: ' + key);
      $('#responseText').text('Waiting for Node.js…');
      $('#status').removeClass('ok fail').text('Sending request to Node.js…');
      $('button').prop('disabled', true);
      resetOutput();
      setJourney(1, 0);
      $('.step[data-step="2"]').addClass('active');

      $.ajax({
        url: API + path,
        type: 'GET',
        dataType: 'json',
        headers: { 'x-api-key': key },
        success: function (response) {
          setJourney(6, 0);
          showRows(response.data);
          showStats(response.meta, response.data);
          showSqlValues(type, response, name, limit);
          $('#responseText').text(JSON.stringify(response, null, 2));
          $('#status').addClass('ok').text('HTTP 200 OK — permission granted; table updated without a full page reload.');
        },
        error: function (xhr) {
          const code = xhr.status || 0;
          const data = xhr.responseJSON;
          const message = data && data.message ? data.message : 'Node.js API could not be reached.';
          if (code === 401 || code === 403 || code === 429) setJourney(2, 3);
          else if (code === 500) setJourney(3, 4);
          else setJourney(1, 2);
          $('#responseText').text(data ? JSON.stringify(data, null, 2) : 'No response from the API.');
          $('#sqlExplain').text('SQL records are not displayed for this request.');
          $('#status').addClass('fail').text('HTTP ' + (code || 'Network') + ' — ' + message);
        },
        complete: function () { $('button').prop('disabled', false); }
      });
    }

    // Slides 17-19, 29 and 39: requested number applies to Load.
    $('#loadCustomers').on('click', function () { sendRequest('load'); });
    // Slides 25-29, 38-39: Search uses the maxRows for the role.
    $('#searchCustomers').on('click', function () { sendRequest('search'); });
  });
  </script>
</body>
</html>
