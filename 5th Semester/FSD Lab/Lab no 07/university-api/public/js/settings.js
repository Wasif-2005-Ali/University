$(function () {
  function showSaved() {
    const k = localStorage.getItem('apiKey');
    if (!k) { $('#info').html('<p class="muted">No API key saved in this browser.</p>'); return; }
    const role = localStorage.getItem('apiRole');
    $('#info').html('<p>Saved key: <code>' + esc(maskKey(k)) + '</code> <span class="badge ' + esc(role) + '">' + esc(role) + '</span></p>' +
      '<p><a class="btn sm" href="/' + esc(role) + '">Open ' + esc(role) + ' dashboard</a></p>');
  }
  showSaved();

  $('#save').on('click', function () {
    const key = $.trim($('#key').val());
    hideAlert('#msg');
    if (!key) { showAlert('#msg', 'err', 'Please enter your API key.'); return; }

    apiCall('GET', '/api/whoami', key)
      .done(function (res) {
        localStorage.setItem('apiKey', key);
        localStorage.setItem('apiRole', res.role);
        $('#key').val('');
        showAlert('#msg', 'ok', 'Key verified and saved. Welcome, ' + res.profile.name + ' (' + res.role + ').');
        showSaved();
      })
      .fail(function (xhr) { showAlert('#msg', 'err', errText(xhr)); });
  });

  $('#clear').on('click', function () {
    localStorage.removeItem('apiKey');
    localStorage.removeItem('apiRole');
    showAlert('#msg', 'info', 'Saved key removed.');
    showSaved();
  });
});
