'use strict';

sevenMonitor
.controller('SettingsCtrl', function($scope, $q, $http, $modal, SettingsService) {

   $scope.settings = [];
   $scope.users = [];
   $scope.servers = [];

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

   SettingsService.readServers()
   .success(function (data) {
        $scope.servers = data;
   })
   .error(function (status) {
        alert(status);
   });

   $scope.server = {}
     $scope.addServer = function () {
       if (typeof $scope.servers !== 'undefined' && $scope.servers.length > 0) {
          alert('currently only one server is supported');
          return;
       }
       var modalInstance = $modal.open({
         templateUrl: '/views/server.html',
         controller: 'ServerCtrl',
         backdrop: 'static',
         resolve: {
           server: function () {
             return $scope.server;
           }
         }
       });

       modalInstance.result.then(function (server) {
       alert('server returned');
       SettingsService.createServer(server)
       .success(function (data) {
            $scope.servers = data;
            SettingsService.readServers()
               .success(function (data) {
                    $scope.servers = data;
               })
               .error(function (status) {
                    alert(status);
               });
       })
       .error(function (status) {
            alert(status);
       });

       //TODO: remove
         $scope.server = server;
       }, function () {
        // closed
       });
     };

});