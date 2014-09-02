'use strict';

sevenMonitor.factory('SettingsService',function( $q, $http) {

   var readSettings = function () {
     return $http.get('/protected/settings');
   }
   var readUsers = function () {
       return $http.get('/protected/users');
   }
   var readServers = function () {
      return $http.get('/protected/servers');
   }
   var createServer = function (server) {
      return $http.post('/protected/servers', server);
   }
   var updateServer = function (server) {
      return $http.put('/protected/servers', server);
   }
   var deleteServer = function (server) {
      return $http.delete('/protected/servers', server);
   }

   return {
      readSettings: readSettings,
      readUsers: readUsers,
      readServers: readServers,
      createServer: createServer,
      updateServer: updateServer,
      deleteServer: deleteServer
   };

});