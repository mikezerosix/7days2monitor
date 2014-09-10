'use strict';

sevenMonitor
.controller('MenuCtrl', ['$scope', '$location', '$http', function($scope, $location, $http) {

    $scope.alerts = [];
     $scope.addAlert = function(alertMsg) {
        $scope.alerts.push({msg: alertMsg});
      };

      $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
      };
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