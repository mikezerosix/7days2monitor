'use strict';
sevenMonitor.controller('ServerCtrl', function ($scope, $modalInstance, server) {


  $scope.server = server;

  $scope.ok = function () {
    $modalInstance.close($scope.server);
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
});
