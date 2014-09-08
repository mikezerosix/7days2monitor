'use strict';

sevenMonitor
.controller('PlayersCtrl', function($scope, PlayerService) {


    $scope.players;
    $scope.getPlayers = function() {
        PlayerService.getPlayers()
         .success(function (data) {
                 $scope.players = data;
           })
           .error(function (status) {
                alert(status);
           });
    };
    $scope.getPlayers();

    $scope.alerts = [];
       $scope.addAlert = function(alertMsg) {
          $scope.alerts.push({msg: alertMsg});
        };

        $scope.closeAlert = function(index) {
          $scope.alerts.splice(index, 1);
        };
});