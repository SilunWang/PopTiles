var express = require('express');
var router = express.Router();
var mongo = require('mongodb');
var monk = require('monk');
var db = monk('localhost:27017/popTiles');

/* GET home page. */
router.get('/', function(req, res) {
  res.render('index', { title: 'Express' });
});

router.get('/helloworld', function(req, res) {
  res.render('helloworld', { title: 'Hello World!' });
});

router.get('/userlist', function(req, res) {
  var collection = db.get('users');
  collection.find({},{},function(e,docs){
     res.render('userlist', {
         "userlist" : docs
     });
  });
});

router.get('/scores', function(req, res) {
  var collection = db.get('scores');
  collection.find({},{},function(e,docs){
     res.render('scores', {
         "scores" : docs
     });
  });
});

module.exports = router;
