'use strict';

sevenMonitor.factory('SettingsService',function( $q, $http) {

   var readSettings = function () {
     return $http.get('/protected/settings');
   }
   var readUsers = function () {
       return $http.get('/protected/users');
   }
   var readConnections = function () {
      return $http.get('/protected/connections');
   }
   var updateConnection = function (connection) {
      return $http.put('/protected/connections', connection);
   }

   return {
      readSettings: readSettings,
      readUsers: readUsers,
      readConnections: readConnections,
      updateConnection: updateConnection

   };

});