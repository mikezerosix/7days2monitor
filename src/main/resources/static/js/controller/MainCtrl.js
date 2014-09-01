'use strict';

sevenMonitor
.controller('MainCtrl', ['$scope', function($scope, authorized) {
    $scope.authorized = authorized;
}]);