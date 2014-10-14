'use strict';

sevenMonitor
    .controller('PlayersCtrl', function ($scope, $rootScope, PlayerService) {
        $rootScope.currTab['players'] = true;
        $scope.selectedPlayer = {};
        $scope.players;
        $scope.getPlayers = function () {
            PlayerService.getPlayers()
                .success(function (data) {
                    $scope.players = data;
                })
                .error(function (status) {
                    alert(status);
                });
        };
        $scope.getPlayers();
        $scope.$on('PLAYER', function (event, message) {
            $scope.getPlayers();
        });

        $scope.selectPlayer= function(player) {
            $scope.selectedPlayer = player;
        }
    });