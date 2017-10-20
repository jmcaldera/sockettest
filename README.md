# sockettest
Demo Android app para conectarse a un servidor corriendo socket.io por medio de una aplicacion Android


## How to use

Clonar el repositorio

```
cd socket_server
npm install
node run index.js
```

En la aplicación Android, editar la linea: 
https://github.com/jmcaldera/sockettest/blob/3f6a53fa0de19ce3f7f190b69af769c5c5043b8b/app/src/main/java/com/example/jmcaldera/sockettest/repository/remote/api/ApiConstants.java#L9 
y colocar la dirección ip de la máquina que está corriendo el servidor. 

Instalar la app en un telefono o emulador.

Para probar la recepción de mensajes cuando la app está en background, ir a ``http://localhost/``. El navegador se comunica con el servidor y este envía un broadcast que recibe el cliente Android.

