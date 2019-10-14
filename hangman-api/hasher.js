var sha = require('sha256')
var guid = require('guid')

var createHash = function(password){
    var salt = guid.create()
    var saltedPassword = password + salt
    var hash = sha(saltedPassword)
    return {
        passwordHash: hash,
        passwordSalt: salt.value
    }
}

var checkPassword = function(password, salt, hashedPassword){
    var saltedPassword = password + salt
    var hash = sha(saltedPassword)
    return hash == hashedPassword
}

exports.createHash = createHash
exports.checkPassword = checkPassword