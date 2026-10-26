$(function () {
  const key = localStorage.getItem('apiKey');
  if (!key) { window.location.href = '/settings'; return; }

  let allRows = [];

  function fail(xhr) {
    let msg = errText(xhr);
    if (xhr.status === 401 || xhr.status === 403) msg += '  (check your key in Settings)';
    showAlert('#msg', 'err', msg);
  }
  function draw(rows) { $('#marks').html(marksRows(rows, true)); }
  function usage(u) { $('#usage').removeClass('muted').html(usageHtml(u)); }

  function loadAll() {
    hideAlert('#msg');
    apiCall('GET', '/api/teacher/marks', key)
      .done(function (res) { allRows = res.data; draw(allRows); usage(res.usage); $('#search').val(''); })
      .fail(fail);
  }

  apiCall('GET', '/api/teacher/me', key)
    .done(function (res) {
      const t = res.data;
      $('#profile').removeClass('muted').html(
        '<div class="grid"><div><label>Name</label><b>' + esc(t.name) + '</b></div>' +
        '<div><label>Email</label>' + esc(t.email) + '</div>' +
        '<div><label>Department</label>' + esc(t.department) + '</div></div>');
      usage(res.usage);
    })
    .fail(fail);

  apiCall('GET', '/api/teacher/students', key)
    .done(function (res) {
      res.data.forEach(function (s) {
        $('#studentSel').append('<option value="' + s.id + '">' + esc(s.roll_no + ' - ' + s.name) + '</option>');
      });
      usage(res.usage);
    })
    .fail(fail);

  $('#allBtn').on('click', loadAll);

  $('#oneBtn').on('click', function () {
    const id = $('#studentSel').val();
    if (!id) { showAlert('#msg', 'err', 'Select a student first.'); return; }
    hideAlert('#msg');
    apiCall('GET', '/api/students/' + encodeURIComponent(id) + '/marks', key)
      .done(function (res) {
        const rows = res.data.map(function (r) { return r; });
        allRows = rows; draw(rows); usage(res.usage);
      })
      .fail(fail);
  });

  // client-side filter on already loaded rows (no extra API request)
  $('#search').on('input', function () {
    const q = $.trim($(this).val()).toLowerCase();
    draw(allRows.filter(function (r) {
      return (r.student_name + ' ' + r.roll_no + ' ' + r.course_code + ' ' + r.course_title).toLowerCase().indexOf(q) !== -1;
    }));
  });

  loadAll();
});
