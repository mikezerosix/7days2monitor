'use strict';

sevenMonitor
.controller('SettingsCtrl', function($scope, $q, $http, $modal, SettingsService) {

   $scope.settings = [];
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
        $scope.settings = data;
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