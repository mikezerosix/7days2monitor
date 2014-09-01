'use strict';

sevenMonitor
.controller('MainCtrl', function($scope, authorized) {
  $scope.authorized = authorized;
});