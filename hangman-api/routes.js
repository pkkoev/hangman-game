'use strict';

const express = require('express');
const router = express.Router();
const error = require('./helpers').sendErrorResponse;
var hasher = require('./hasher')
const util = require('util');
//const indicative = require('indicative');


router.post('/register', function (req, res) {
  const db = req.app.locals.db;
  const query = "INSERT INTO Users (username, passwordSalt, passwordHash) VALUES (?, ?, ?)";
  const userExistsQuery = "SELECT * FROM Users WHERE username = ?";

  let { passwordHash, passwordSalt } = hasher.createHash(req.body.password);
  let username = req.body.username;

  try{
  db.get(userExistsQuery, [username], function (err, result) {
    if (err) { //db query err checking if the user exists
      error(req, res, 500, `The DB select query couldnt be executed.`,err); return;
    }
    if (result) { //the username already exists
      error(req, res, 400, `The username already exists.`); return;
    }

    db.run(query, [username, passwordSalt, passwordHash], function (err, result) {
      if (err) { // err insert query
        error(req, res, 500, `The DB insert query couldnt be executed.`,err); return;
      }
      res.send(result || true);
    })
  })} catch(err) {
    error(req, res, 500, 'Unknown error',err)
  }
})

router.post('/authorize', function (req, res) {
  const db = req.app.locals.db;
  const query = "SELECT * FROM Users WHERE username = ?";

  let { username, password } = req.body;

  try{
  db.get(query, [username], function (err, result) {
    if (err) { //db query error 
      error(req, res, 500, `The DB query couldnt be executed.`,err); return;
    }
    if (result == undefined) { // if the user doesnt exist
      error(req, res, 400, `The username or password is incorrect.`); return;
    }
    if (!hasher.checkPassword(password, result.passwordSalt, result.passwordHash)) {
      //if the password doesnt match
      error(req, res, 400, `The username or password is incorrect.`); return;
    }
    else {
      res.send(true); return; // if the password matches
    }
  })} catch(err) {
    error(req, res, 500, 'Unknown error',err)
  }
})

router.get('/getWord', function (req, res) {
  const db = req.app.locals.db;
  const query = "SELECT word FROM Words ORDER BY RANDOM() LIMIT 1;";
  try{
  db.get(query, [], function (err, results) {
    if (err) { error(req, res, 500, 'Error selecting a word'); return; }
    res.send(results["word"])
  })} catch(errors) {
    error(req, res, 500, 'Unknown error')
  };
})

router.get('/users', function (req, res) {
  const db = req.app.locals.db;
  const query = "SELECT username FROM Users"
  try{
    db.all(query, [], function (err, result) {
      if (err) { error(req, res, 500, 'Error selecting users'); return; }
      res.send(result);
    })
  } catch (errors) {
    error(req, res, 500, 'Unknown error')
  }
})


module.exports = router;
