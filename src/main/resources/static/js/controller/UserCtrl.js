'use strict';
sevenMonitor.controller('UserCtrl', function ($scope, $modalInstance, user) {
  $scope.user = user;

  $scope.ok = function () {
    $modalInstance.close($scope.user);
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
});
