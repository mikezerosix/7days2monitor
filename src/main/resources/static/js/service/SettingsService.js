'use strict';

sevenMonitor.factory('SettingsService',function( $q, $http) {

   var readSettings = function () {
     return $http.get('/protected/settings');
   }
   var updateSettings = function (id, value) {
      return $http.put('/protected/settings', {id: id, value: value});
   }

   var readUsers = function () {
       return $http.get('/protected/users');
   }


   var readConnections = function () {
      return $http.get('/protected/settings/connections');
   }
   var updateConnection = function (connection) {
      return $http.put('/protected/settings/connections', connection);
   }



   return {
      readSettings: readSettings,
      updateSettings: updateSettings,
      readUsers: readUsers,
      readConnections: readConnections,
      updateConnection: updateConnection
   };

});