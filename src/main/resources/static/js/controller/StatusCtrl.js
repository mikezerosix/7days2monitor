'use strict';

sevenMonitor
    .controller('StatusCtrl', function ($scope, $rootScope, CometService) {

        $scope.$watch(function() {
            return $rootScope.authorized;
        }, function() {
            CometService.start();
        }, true);

        $scope.isLoading = false;
        $scope.messages = [];
        $scope.status = {'state': 'status-init', 'msg': 'ok'};

        $rootScope.$on('status_error', function (event, message) {
            $scope.status.state = 'status-error';
            $scope.status.msg = message;
            $scope.messages.push($scope.status);
        });

        $rootScope.$on('status_warn', function (event, message) {
            $scope.status.state = 'status-warn';
            $scope.status.msg = message;
            $scope.messages.push($scope.status);
        });

        $rootScope.$on('status_info', function (event, message) {
            $scope.status.state = 'status-ok';
            $scope.status.msg = message;
            $scope.messages.push($scope.status);
        });

        $rootScope.$on('show_loading', function (event, message) {
           //console.log('show loading ');
            $scope.isLoading = true;
        });

        $rootScope.$on('hide_loading', function (event, message) {
           //console.log('hide loading ');
            $scope.isLoading = false;
        });

        $scope.expandStatusMessages = '';
        $scope.showMessages = function() {
            if ( $scope.expandStatusMessages == ''){
                $scope.expandStatusMessages =  'status-messages-expanded';
            } else {
                $scope.expandStatusMessages = '';
            }
        }
    });

