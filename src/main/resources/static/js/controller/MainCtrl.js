'use strict';

sevenMonitor
.controller('MainCtrl',  function($scope, $q, $http, SettingsService, TelnetService) {

  $scope.telnetStatus;
  TelnetService.status()
        .success(function (data) {
            $scope.telnetStatus = data;
        })
        .error(function (status) {
            alert(status);
        });

  $scope.connectTelnet = function() {
    TelnetService.connect()
        .success(function (data) {
            $scope.telnetStatus = data;
        })
        .error(function (status) {
            alert(status);
        });
  }

  $scope.disconnectTelnet = function() {
    TelnetService.disconnect()
        .success(function (data) {
            $scope.telnetStatus = data;
        })
        .error(function (status) {
            alert(status);
        });
  }
  $scope.status;

  $scope.cmd;
    $scope.sendCmd = function() {
       console.log('sending cmd ' +$scope.cmd );

       TelnetService.sendCmd($scope.cmd)
           .success(function (data) {
              $scope.status = data;
              $scope.cmd = undefined;
           })
           .error(function (status) {
               alert(status);
           });
   }
});