'use strict';

sevenMonitor
    .controller('MenuCtrl', function ($scope, $rootScope, $location, $http) {
        $rootScope.currTab = '' || $rootScope.currTab;
        $scope.serverName = '';
        $rootScope.$on('serverName', function (event, message) {
            $scope.serverName = message;
        });

        $scope.alerts = [];
        $rootScope.$on('alert', function (event, message) {
            $scope.status.state = 'status-warn';
            $scope.alerts.push(message);
        });


        $scope.logout = function () {
            $http.delete('/protected/login')
                .success(function (data) {
                    alert('You are logged out');
                })
                .error(function (status) {
                    alert('Error: ' + status);

                });

        };

        $scope.selectTab = function (path) {
            $location.path("/" + path).replace();
        };
        $scope.currTab = $rootScope.currTab;

        $scope.credits = "Credits: Michael Holopainen (design, lead dev), moztr (contrribution), by PC,Duesseldorf (icons@iconfinder), proto.io (on/off flipswitch)  "

    });