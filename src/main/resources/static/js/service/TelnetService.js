'use strict';

sevenMonitor.factory('TelnetService',function( $q, $http) {

   var status = function () {
       return $http.get('/protected/telnet/status');
   };

   var connect = function () {
      return $http.post('/protected/telnet');
   };

   var disconnect = function () {
       return $http.delete('/protected/telnet');
   };

   var chat = function () {
       return $http.get('/protected/telnet/chat');
   };

   var raw = function () {
       return $http.get('/protected/telnet/raw');
   };

    var say = function (msg) {
       return $http.post('/protected/telnet/say', msg);
   };


   return {
        status: status,
        connect: connect,
        disconnect: disconnect,
        raw: raw,
        chat: chat,
        say: say
   };
});