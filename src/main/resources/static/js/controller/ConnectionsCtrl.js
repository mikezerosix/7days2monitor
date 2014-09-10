'use strict';
sevenMonitor.controller('ConnectionsCtrl', function ($scope, $modalInstance, connection) {


  $scope.connection = connection;

  $scope.ok = function () {
    $modalInstance.close($scope.connection);
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
});
