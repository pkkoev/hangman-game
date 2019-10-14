'use strict';

var sqlite3 = require('sqlite3').verbose();
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const bodyParser = require('body-parser');

const rootPath = path.normalize(path.join(__dirname, '..'));
const routes = require('./routes');


const app = express();
app.set('app', path.join(rootPath, 'app'));

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());


app.use('/hangman/', routes);

/// catch 404 and forwarding to error handler
app.use(function (req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

/// error handlers
// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
  app.use(function (err, req, res, next) {
    res.status(err.status || 500);
    res.json({
      message: err.message,
      error: err
    });
  });
} else {
  // production error handler
  // no stacktraces leaked to user
  app.use(function (err, req, res, next) {
    res.status(err.status || 500);
    res.json({
      message: err.message,
      error: {}
    });
  });
}

// Initialize DB
const DB_FILE = path.join(__dirname, 'hangman.sqlite');
const TABLES = [
  { 
    name: 'Users', 
    createQuery: `CREATE TABLE Users (id INTEGER PRIMARY KEY, username TEXT UNIQUE, passwordSalt TEXT, passwordHash TEXT);`, 
    populateQuery: `INSERT INTO Users (username,passwordSalt,passwordHash) VALUES ('admin','f67952db-0758-86b2-db6d-93c61d562289','373887f4211be59a34a1d33cbe06be1959f7f2d3e0be4c897a9b1b7868b5423e')`
  }, //admin admin
  { 
    name: 'Words', 
    createQuery: `CREATE TABLE Words (id INTEGER PRIMARY KEY, word TEXT UNIQUE);`, 
    populateQuery: `INSERT INTO Words (word) VALUES 
    ('tarantula'), 
    ('mathematics'), 
    ('table'), 
    ('tenis'), 
    ('interesting'), 
    ('looser')`
  },
  { name: 'Games', createQuery: `CREATE TABLE Games (gameid INTEGER PRIMARY KEY, word TEXT, sth1 TEXT, user TEXT, sth2 INTEGER);`}
];

const db = new sqlite3.Database(DB_FILE, (err) => {
  if (err) throw err;

  //Test if tables exists - if not create it //cb hell
  // db.run(`INSERT INTO Words (id, word) VALUES (7,'testing')`)
  TABLES.map(({ name, createQuery, populateQuery }) => {
    db.all(`SELECT name FROM sqlite_master WHERE type='table' AND name=?;`, [name], (err, table) => {
      if (err) throw err;
      if (!table[0]) {
        db.run(createQuery, [], (err) => {
          if (err) throw err;
          console.log(`Table ${name} successfully created in db: ${DB_FILE}`);
          if(populateQuery) db.run(populateQuery);
        })
      }
    })
  })

  console.log(`Successfully connected to SQLite server`);

  //Add db as app local property
  app.locals.db = db;

  //Start the server
  app.listen(8080, (err) => {
    if (err) throw err;
    console.log('Server started on port 8080!')
  });
});




