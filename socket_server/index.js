var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

// server.listen(80);

app.get('/', function (req, res) {
  res.sendFile(__dirname + '/index.html');
});

io.on('connection', function (socket) {
  socket.emit('order_received', { 
    id: 1,
    documents: [{
      id: 2,
      name: 'Documento uno',
      precio: 2500
    }, {
      id: 3,
      name: 'Documento dos',
      precio: 7500
    }],
    subsidiary: {
      id: 1,
      name: 'Nombre',
      address: "Las Verbenas 8961"
    } 
  });

  socket.on('get_order', function (data) {
    console.log(data);
    socket.broadcast.emit('order_received', { 
      id: 2,
      documents: [{
        id: 2,
        name: 'Documento uno',
        precio: 2500
      }, {
        id: 3,
        name: 'Documento dos',
        precio: 7500
      }],
      subsidiary: {
        id: 1,
        name: 'Nombre',
        address: "Las Casas 8961"
      } 
    });
  });
});

server.listen(80, function(){
  console.log('listening on *:80');
});