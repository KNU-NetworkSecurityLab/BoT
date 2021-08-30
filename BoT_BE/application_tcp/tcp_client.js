var net = require('net');

var port = 3001;
var host = "";


var socket = net.connect({
    port: port,
    host: host
});

socket.setEncoding('utf8');

// Arduino connection
socket.on('connect', function() {
    console.log("connected to server");

    
    
    setTimeout( function() {
        socket.write('tmpHum,IoT5,11,11');
    }, 1000);

          
});

socket.on('data', function(data) {
    console.log(data);
});

socket.on('close', function()  {
    console.log('close');
});


socket.on('error', function(err) {
    console.log('on error: ', err.code);
});