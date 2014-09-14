'use strict';

sevenMonitor
    .controller('StatusCtrl', function ($scope, $rootScope) {
        $scope.messages = [];
        $scope.status = {'state': 'status-init', 'msg': 'ok'};

        $rootScope.$on('status_error', function (event, message) {
            $scope.status.state = 'status-error';
            $scope.status.msg = message;
            $scope.messages.push(status);
        });

        $rootScope.$on('status_warn', function (event, message) {
            $scope.status.state = 'status-warn';
            $scope.status.msg = message;
            $scope.messages.push(status);
        });

        $rootScope.$on('status_info', function (event, message) {
            $scope.status.state = 'status-ok';
            $scope.status.msg = message;
            $scope.messages.push(status);
        });

        $scope.showMessages = function() {
            $scope.status = {'state': 'status-init', 'msg': 'TODO: show messages - functionality'};
        }
    });

