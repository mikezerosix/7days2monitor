'use strict';

sevenMonitor
.controller('MainCtrl', function($scope, loggedInUser) {
  $scope.user = loggedInUser;
});