'use strict';
sevenMonitor
.controller('LoginCtrl', function ($rootScope, $scope, $http, $location, $route, $routeParams) {
  $scope.user = {username: '', password: ''};
  $scope.message = '';
  $scope.submit = function () {

    $http.post('/protected/login', $scope.user)
      .success(function (data) {
        $rootScope.$apply(function() {
            $location.path("/main");
        });
      })
      .error(function (status) {
        alert('Error: ' + status);
        $scope.message = 'Error: Invalid user or password';
      });
  };
});