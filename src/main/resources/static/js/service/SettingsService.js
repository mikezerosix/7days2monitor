'use strict';

sevenMonitor.factory('SettingsService',function( $q, $http) {

   var readSettings = function () {
     return $http.get('/protected/settings');
   }
   var upsertSettings = function (id, value) {
      return $http.put('/protected/settings', {id: id, value: value});
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
      upsertSettings: upsertSettings,
      readUsers: readUsers,
      readConnections: readConnections,
      updateConnection: updateConnection
   };

});