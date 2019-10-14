exports.sendErrorResponse = function (req, res, statusCode, message, error) {
// development error handler
// will print stacktrace
res.status(200);
res.send(false);
return;

if (req.get('env') === 'development') {
  res.status(statusCode || 500);
  res.json({
    'errors': [{
      message,
      error: error || {}
    }]
  });
} else {
  // production error handler
  // no stacktraces leaked to user
  res.status(statusCode || 500);
  res.json({
    'errors': [{
      message,
      error: {}
    }]
  });
  return;
} 

res.status(statusCode || 404);
  res.json({
    'errors': [{
      message,
      error: error || {}
    }]
  });
}