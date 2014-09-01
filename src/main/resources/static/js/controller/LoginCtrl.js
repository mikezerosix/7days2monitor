'use strict';
sevenMonitor
.controller('LoginCtrl', function ($rootScope, $scope, $http, $location, $route, $routeParams) {
  $scope.user = {username: '', password: ''};
  $scope.message = '';
  $scope.submit = function () {

    $http.post('/protected/login', $scope.user)
      .success(function (data) {
        alert('Happy, happy, joy, joy !!!');
        $rootScope.user = true;
        alert($rootScope.user);
        $location.path = "/main";
      })
      .error(function (status) {
        alert('Error: ' + status);
        $scope.message = 'Error: Invalid user or password';
      });
  };
});