'use strict';

sevenMonitor
    .controller('TriggersCtrl', function ($scope, $rootScope, $modal, MonitorService) {
        $rootScope.currTab['triggers'] = true;
        $scope.monitors = [];
        $scope.monitor = {};
        $scope.getMonitors = function () {
            MonitorService.getMonitors()
                .success(function(data, status) {
                    $scope.monitors = data;
                })
                .error(function (data, status) {
                    $scope.$emit('ERROR', data );
                })
        };
        $scope.createMonitor = function () {
            $scope.openMonitor({});
        };

        $scope.openMonitor = function (monitor) {
            $scope.monitor = monitor;
            var modalInstance = $modal.open({
                templateUrl: '/views/partial/monitor.html',
                controller: 'MonitorCtrl',
                backdrop: 'static',
                resolve: {
                    monitor: function () {
                        return $scope.monitor;
                    }
                }
            });

            modalInstance.result.then(function (user) {
                MonitorService.saveMonitor(monitor)
                    .success(function(data, status) {
                        $scope.getMonitors();
                    })
                    .error(function (data, status) {
                        $scope.$emit('ERROR', data );
                    })

            }, function () {
                // closed
            });
        };
    });