$(function () {
  const key = localStorage.getItem('apiKey');
  if (!key) { window.location.href = '/settings'; return; }

  function fail(xhr) {
    let msg = errText(xhr);
    if (xhr.status === 401 || xhr.status === 403) msg += '  (check your key in Settings)';
    showAlert('#msg', 'err', msg);
  }

  function load() {
    hideAlert('#msg');
    apiCall('GET', '/api/student/me', key)
      .done(function (res) {
        const s = res.data;
        $('#profile').removeClass('muted').html(
          '<div class="grid"><div><label>Name</label><b>' + esc(s.name) + '</b></div>' +
          '<div><label>Roll No</label><b>' + esc(s.roll_no) + '</b></div>' +
          '<div><label>Email</label>' + esc(s.email) + '</div>' +
          '<div><label>Department</label>' + esc(s.department) + '</div>' +
          '<div><label>Semester</label>' + esc(s.semester) + '</div></div>');
        $('#usage').removeClass('muted').html(usageHtml(res.usage));
      })
      .fail(fail);

    apiCall('GET', '/api/student/marks', key)
      .done(function (res) {
        $('#marks').html(marksRows(res.data, false));
        $('#usage').removeClass('muted').html(usageHtml(res.usage));
      })
      .fail(function (xhr) { $('#marks').html('<tr><td colspan="7" class="muted">Not available</td></tr>'); fail(xhr); });
  }

  $('#reload').on('click', load);
  load();
});
