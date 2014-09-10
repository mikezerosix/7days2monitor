'use strict';

sevenMonitor
  .controller('StatusCtrl', function($scope) {

    $scope.status = {};

    $scope.$on('status_error', function(event, message) {
      $scope.status.state = 'error';
      $scope.status.msg = message;
    });

    $scope.$on('status_warn', function(event, message) {
      $scope.status.state = 'warn';
      $scope.status.msg = message;

    });

    $scope.$on('status_info', function(event, message) {
      $scope.status.state = 'info';
      $scope.status.msg = message;

    });

  });

