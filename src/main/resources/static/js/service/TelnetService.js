'use strict';

sevenMonitor.factory('TelnetService',function( $q, $http) {

   var isRunning = function () {
       return $http.get('/protected/telnet');
   };

   var run = function () {
          return $http.post('/protected/telnet');
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
        isRunning: isRunning,
        run: run,
        raw: raw,
        chat: chat,
        say: say
   };
});