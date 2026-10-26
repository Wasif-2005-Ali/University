/* Shared helpers used by every page */

// Wrapper around $.ajax: sends the API key in the x-api-key header
function apiCall(method, url, key, body) {
  return $.ajax({
    url: url,
    type: method,
    dataType: 'json',
    contentType: 'application/json',
    headers: { 'x-api-key': key || '' },
    data: body ? JSON.stringify(body) : undefined
  });
}

// Readable error text from a failed AJAX call
function errText(xhr) {
  if (xhr.responseJSON && xhr.responseJSON.message) {
    return xhr.status + ' - ' + xhr.responseJSON.message;
  }
  return 'Cannot reach the server. Is Node.js running?';
}

// Escape text before putting it in HTML (prevents XSS)
function esc(v) {
  return $('<div>').text(v === null || v === undefined ? '' : String(v)).html();
}

function showAlert(sel, type, msg) {
  $(sel).removeClass('err ok info').addClass(type + ' show').text(msg);
}
function hideAlert(sel) { $(sel).removeClass('show'); }

function maskKey(k) { return k.length > 12 ? k.slice(0, 8) + '...' + k.slice(-4) : k; }

function fmtDate(d) {
  if (!d) return '-';
  const x = new Date(d);
  return x.toLocaleDateString() + ' ' + x.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}

function copyText(text) {
  if (navigator.clipboard && window.isSecureContext !== false) {
    return navigator.clipboard.writeText(text);
  }
  const t = $('<textarea>').val(text).appendTo('body').select();
  document.execCommand('copy');
  t.remove();
  return Promise.resolve();
}

function usageHtml(u) {
  return '<div class="stat"><div><b>' + u.remaining + '</b><small>Requests remaining</small></div>' +
         '<div><b>' + u.used + ' / ' + u.limit + '</b><small>Used / Limit</small></div></div>';
}

function marksRows(rows, withStudent) {
  if (!rows.length) return '<tr><td colspan="9" class="muted">No records found.</td></tr>';
  return rows.map(function (r) {
    const total = Number(r.total);
    return '<tr>' +
      (withStudent ? '<td>' + esc(r.roll_no) + '</td><td>' + esc(r.student_name) + '</td>' : '') +
      '<td>' + esc(r.course_code) + '</td><td>' + esc(r.course_title) + '</td>' +
      '<td>' + Number(r.assignment) + '</td><td>' + Number(r.midterm) + '</td><td>' + Number(r.final_exam) + '</td>' +
      '<td><b>' + total.toFixed(1) + '</b></td>' +
      '<td class="' + (total >= 50 ? 'pass' : 'fail') + '">' + (total >= 50 ? 'Pass' : 'Fail') + '</td></tr>';
  }).join('');
}
