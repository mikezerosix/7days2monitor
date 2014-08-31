'use strict';
sevenMonitor
.controller('LoginCtrl', function ($scope, $http, $window) {
  $scope.user = {username: '', password: ''};
  $scope.message = '';
  $scope.submit = function () {
    $http.post('/protected/login', $scope.user)
      .success(function (data, status, headers, config) {
        $scope.message = 'Welcome';
      })
      .error(function (data, status, headers, config) {
        $scope.message = 'Error: Invalid user or password';
      });
  };
});