'use strict';
sevenMonitor
.controller('LoginCtrl', function ($rootScope, $scope, $http, $location, $route, $routeParams) {
  $scope.user = {name: '', password: ''};
  $scope.message = '';
  $scope.submit = function () {
    $http.post('/protected/login', $scope.user)
      .success(function (data) {
         $rootScope.authorized = true;
      })
      .error(function (status) {
        alert('Error: ' + status);
        $scope.message = 'Error: Invalid user or password';
      });
  };
});