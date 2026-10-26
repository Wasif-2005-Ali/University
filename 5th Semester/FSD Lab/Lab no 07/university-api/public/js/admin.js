$(function () {
  let ADMIN_KEY = sessionStorage.getItem('adminKey') || '';
  let students = [], teachers = [];

  const A = function (method, url, body) { return apiCall(method, url, ADMIN_KEY, body); };
  function fail(xhr) { showAlert('#msg', 'err', errText(xhr)); }
  function ok(text) { showAlert('#msg', 'ok', text); }

  /* ---------- login / logout ---------- */
  function enter() {
    $('#loginBox').hide(); $('#panel').show(); $('#logout').show();
    loadAll();
  }
  $('#loginBtn').on('click', function () {
    const k = $.trim($('#adminKey').val());
    if (!k) { showAlert('#loginMsg', 'err', 'Enter the admin API key.'); return; }
    ADMIN_KEY = k;
    A('GET', '/api/admin/students').done(function () {
      sessionStorage.setItem('adminKey', k); enter();
    }).fail(function (xhr) { ADMIN_KEY = ''; showAlert('#loginMsg', 'err', errText(xhr)); });
  });
  $('#adminKey').on('keydown', function (e) { if (e.key === 'Enter') $('#loginBtn').click(); });
  $('#logout').on('click', function (e) { e.preventDefault(); sessionStorage.removeItem('adminKey'); location.reload(); });

  /* ---------- tabs ---------- */
  $('.tabs button').on('click', function () {
    $('.tabs button').removeClass('on'); $(this).addClass('on');
    $('.tab').removeClass('on'); $('#tab-' + $(this).data('tab')).addClass('on');
  });

  /* ---------- load data ---------- */
  function loadAll() {
    hideAlert('#msg');
    A('GET', '/api/admin/students').done(function (r) {
      students = r.data;
      $('#studentsBody').html(r.data.map(function (s) {
        return '<tr><td>' + s.id + '</td><td>' + esc(s.roll_no) + '</td><td>' + esc(s.name) + '</td><td>' + esc(s.email) +
               '</td><td>' + esc(s.department) + '</td><td>' + esc(s.semester) + '</td></tr>';
      }).join(''));
      fillOwners();
    }).fail(fail);

    A('GET', '/api/admin/teachers').done(function (r) {
      teachers = r.data;
      $('#teachersBody').html(r.data.map(function (t) {
        return '<tr><td>' + t.id + '</td><td>' + esc(t.name) + '</td><td>' + esc(t.email) + '</td><td>' + esc(t.department) + '</td></tr>';
      }).join(''));
      fillOwners();
    }).fail(fail);

    loadKeys();
  }

  function fillOwners() {
    const list = $('#ownerType').val() === 'student' ? students : teachers;
    $('#ownerId').html(list.map(function (o) {
      return '<option value="' + o.id + '">' + esc((o.roll_no ? o.roll_no + ' - ' : '') + o.name) + '</option>';
    }).join(''));
  }
  $('#ownerType').on('change', fillOwners);

  function loadKeys() {
    A('GET', '/api/admin/keys').done(function (r) {
      if (!r.data.length) { $('#keysBody').html('<tr><td colspan="7" class="muted">No API keys yet. Generate one above.</td></tr>'); return; }
      $('#keysBody').html(r.data.map(function (k) {
        const pct = Math.min(100, Math.round(k.requests_used / k.request_limit * 100));
        const cls = pct >= 100 ? 'full' : pct >= 80 ? 'warn' : '';
        const revoked = k.status === 'revoked';
        return '<tr data-id="' + k.id + '" data-key="' + esc(k.api_key) + '" data-name="' + esc(k.owner_name) + '">' +
          '<td>' + k.id + '</td>' +
          '<td>' + esc(k.owner_name) + '<br><span class="badge ' + k.owner_type + '">' + k.owner_type + '</span></td>' +
          '<td><code>' + esc(maskKey(k.api_key)) + '</code> <button class="btn sm sec copy">Copy</button></td>' +
          '<td><div class="bar"><span class="' + cls + '" style="width:' + pct + '%"></span></div>' + k.requests_used + ' / ' + k.request_limit + ' (' + k.remaining + ' left)</td>' +
          '<td><span class="badge ' + k.status + '">' + k.status + '</span></td>' +
          '<td>' + fmtDate(k.last_used_at) + '</td>' +
          '<td><div style="display:flex;gap:6px;flex-wrap:wrap;align-items:center">' +
            '<input type="number" min="1" class="lim" value="' + k.request_limit + '" style="width:80px"' + (revoked ? ' disabled' : '') + '>' +
            '<button class="btn sm upd"' + (revoked ? ' disabled' : '') + '>Update</button>' +
            '<button class="btn sm sec rst">Reset</button>' +
            '<button class="btn sm red rev"' + (revoked ? ' disabled' : '') + '>Revoke</button>' +
            '<button class="btn sm sec logs">Logs</button></div></td></tr>';
      }).join(''));
    }).fail(fail);
  }

  /* ---------- generate key ---------- */
  $('#genBtn').on('click', function () {
    hideAlert('#msg'); $('#newKey').empty();
    A('POST', '/api/admin/keys', {
      owner_type: $('#ownerType').val(),
      owner_id: $('#ownerId').val(),
      request_limit: $('#reqLimit').val()
    }).done(function (r) {
      ok(r.message + '. Copy the key now and give it to the user.');
      $('#newKey').html('<div class="keybox">' + esc(r.data.api_key) + '</div>');
      loadKeys();
    }).fail(fail);
  });

  /* ---------- row actions (event delegation) ---------- */
  $('#keysBody').on('click', '.copy', function () {
    const k = $(this).closest('tr').data('key');
    copyText(k).then(function () { ok('API key copied to clipboard'); });
  });

  $('#keysBody').on('click', '.upd', function () {
    const tr = $(this).closest('tr');
    A('PUT', '/api/admin/keys/' + tr.data('id') + '/limit', { request_limit: tr.find('.lim').val() })
      .done(function (r) { ok(r.message); loadKeys(); }).fail(fail);
  });

  $('#keysBody').on('click', '.rst', function () {
    const tr = $(this).closest('tr');
    A('PUT', '/api/admin/keys/' + tr.data('id') + '/reset')
      .done(function (r) { ok(r.message); loadKeys(); }).fail(fail);
  });

  $('#keysBody').on('click', '.rev', function () {
    const tr = $(this).closest('tr');
    if (!confirm('Revoke the key of ' + tr.data('name') + '? This cannot be undone.')) return;
    A('PUT', '/api/admin/keys/' + tr.data('id') + '/revoke')
      .done(function (r) { ok(r.message); loadKeys(); }).fail(fail);
  });

  $('#keysBody').on('click', '.logs', function () {
    const tr = $(this).closest('tr');
    A('GET', '/api/admin/keys/' + tr.data('id') + '/logs').done(function (r) {
      $('#logsTitle').text('Usage log - ' + tr.data('name'));
      $('#logsBody').html(r.data.length ? r.data.map(function (l) {
        return '<tr><td>' + fmtDate(l.created_at) + '</td><td>' + esc(l.method) + '</td><td>' + esc(l.endpoint) +
               '</td><td class="' + (l.status_code < 400 ? 'status-ok' : 'status-bad') + '">' + l.status_code + '</td></tr>';
      }).join('') : '<tr><td colspan="4" class="muted">No requests yet.</td></tr>');
      $('#logsCard').show()[0].scrollIntoView({ behavior: 'smooth' });
    }).fail(fail);
  });

  if (ADMIN_KEY) {
    A('GET', '/api/admin/students').done(enter).fail(function () { sessionStorage.removeItem('adminKey'); });
  }
});
