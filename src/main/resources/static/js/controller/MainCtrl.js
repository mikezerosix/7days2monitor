'use strict';

sevenMonitor
.controller('MainCtrl',  function($scope, $q, $http, SettingsService, TelnetService) {

  $scope.telnetStatus;
  TelnetService.isRunning()
        .success(function (data) {
            alert(data);
            $scope.telnetStatus = data;
        })
        .error(function (status) {
            alert(status);
        });


  $scope.connectTelnet = function() {

  }

   $scope.raw;
   TelnetService.raw()
           .success(function (data) {
               alert(data);
               $scope.raw = data;
           })
           .error(function (status) {
               alert(status);
           });
});