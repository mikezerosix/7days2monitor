'use strict';
sevenMonitor.controller('MonitorCtrl', function ($scope, $modalInstance, monitor) {
  $scope.monitor = monitor;

  $scope.ok = function () {
    $modalInstance.close($scope.monitor);
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
});
