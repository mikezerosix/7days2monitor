'use strict';

sevenMonitor
.controller('SettingsCtrl', function($scope, $q, $http, $modal, SettingsService) {

   $scope.settings = [];
   $scope.settingsIds = [];
   $scope.users = [];
   $scope.connections = [];

   SettingsService.readUsers()
   .success(function (data) {
        $scope.users = data;
   })
   .error(function (status) {
        alert(status);
   });

   SettingsService.readSettings()
   .success(function (data) {
        console.log('settings: ' + data)
        for (var i = 0; i < data.length; i++) {
           $scope.settingsIds[i] = data[i].id;
           $scope.settings[data[i].id] = data[i].value;
        }

   })
   .error(function (status) {
        alert(status);
   });

   SettingsService.readConnections()
   .success(function (data) {
        $scope.connections = data;
   })
   .error(function (status) {
        alert(status);
   });

    $scope.monitorChat = $scope.settings['CHAT_HANDLER_ENABLE'];

    $scope.setChatHandlerEnable = function() {
         console.log('updating CHAT_HANDLER_ENABLE =' + $scope.settings['CHAT_HANDLER_ENABLE']);
        SettingsService.updateSettings('CHAT_HANDLER_ENABLE', $scope.settings['CHAT_HANDLER_ENABLE'])
         .success(function (data) {
  console.log('received: ' + data.id +  '=' + data.value);
             $scope.settings[data.id] = data.value;
       })
       .error(function (status) {
            alert(status);
       });
    };


    $scope.openConnection = function (connection) {
       $scope.connection = connection;
       var modalInstance = $modal.open({
         templateUrl: '/views/connection.html',
         controller: 'ConnectionsCtrl',
         backdrop: 'static',
         resolve: {
           connection: function () {
             return $scope.connection;
           }
         }
       });

       modalInstance.result.then(function (connection) {
       alert('connection returned');
       SettingsService.updateConnection(connection)
       .success(function (data) {

            SettingsService.readConnections()
               .success(function (data) {
                    $scope.connections = data;
               })
               .error(function (status) {
                    alert(status);
               });
       })
       .error(function (status) {
            alert(status);
       });

       //TODO: remove
         $scope.connection = connection;
       }, function () {
        // closed
       });
     };

});