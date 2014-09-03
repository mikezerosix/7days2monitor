'use strict';

sevenMonitor
.controller('MainCtrl', ['$scope', function($scope) {
  var getTelnetStatus = function () {
       return $http.get('/protected/telnet');
  }

  $scope.telnetStatus;

  getTelnetStatus.success(function (data) {
                        $scope.telnetStatus = data;
                    })
                    .error(function (status) {
                         alert(status);
                    });


}]);