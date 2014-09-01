'use strict';

sevenMonitor
.controller('MenuCtrl', ['$scope', '$location', '$http', function($scope, $location, $http) {


    $scope.logout = function() {
     $http.delete('/protected/login')
          .success(function (data) {
              alert('You are logged out');
          })
          .error(function (status) {
            alert('Error: ' + status);

          });

    }

    $scope.selectTab = function(path) {
     $location.path("/"+ path).replace();
    }

}]);