'use strict';

sevenMonitor
.controller('PlayersCtrl', function($scope) {

    $scope.alerts = [];
       $scope.addAlert = function(alertMsg) {
          $scope.alerts.push({msg: alertMsg});
        };

        $scope.closeAlert = function(index) {
          $scope.alerts.splice(index, 1);
        };
});