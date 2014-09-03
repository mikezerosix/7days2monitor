'use strict';

sevenMonitor.factory('TelnetService',function( $q, $http) {

   var isRunning = function () {
       return $http.get('/protected/telnet');
   };

   var run = function () {
          return $http.post('/protected/telnet');
   };


    var raw = function () {
       return $http.get('/protected/telnet/raw');
    };


   return {
        isRunning: isRunning,
        run: run,
        raw: raw
   };
});