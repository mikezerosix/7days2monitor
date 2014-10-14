'use strict';

sevenMonitor
    .controller('SettingsCtrl', function ($scope, $rootScope,  $q, $http, $modal, SettingsService) {
        for (var key in $rootScope.currTab) {
            $rootScope.currTab[key] = false;
        }
        $scope.settings = [];
        $scope.settingsIds = [];
        $scope.users = [];
        $scope.connections = [];

        var readUsers = function () {
            SettingsService.readUsers()
                .success(function (data) {
                    $scope.users = data;
                })
                .error(function (data, status) {
                    $scope.$emit('status_error', 'Error(' + status + ') reading users: ' + data);
                });
        };
        readUsers();

        SettingsService.readSettings()
            .success(function (data) {
                for (var i = 0; i < data.length; i++) {
                    $scope.settingsIds[i] = data[i].id;
                    $scope.settings[data[i].id] = data[i].value;
                }

            })
            .error(function (data, status) {
                $scope.$emit('status_error', 'Error(' + status + ') reading settings: ' + data);
            });

        SettingsService.readConnections()
            .success(function (data) {
                $scope.connections = data;
            })
            .error(function (data, status) {
                $scope.$emit('status_error', 'Error(' + status + ') reading connection: ' + data);
            });

        $scope.setChatHandlerEnable = function () {
            SettingsService.updateSettings('CHAT_HANDLER_ENABLE', $scope.settings['CHAT_HANDLER_ENABLE'])
                .success(function (data) {
                    $scope.settings[data.id] = data.value;
                })
                .error(function (data, status) {
                    $scope.$emit('status_error', 'Error(' + status + ') enabling chat: ' + data);
                });
        };
        $scope.setSetting = function (key) {
            SettingsService.updateSettings(key, $scope.settings[key])
                .success(function (data) {
                    $scope.settings[data.id] = data.value;
                })
                .error(function (data, status) {
                    $scope.$emit('status_error', 'Error(' + status + ') enabling setting: ' + data);
                });
        };


        $scope.openConnection = function (connection) {
            $scope.connection = connection;
            var modalInstance = $modal.open({
                templateUrl: '/views/connection.html',
                controller: 'ConnectionsCtrl',
                backdrop: 'static',
                resolve: {
                    connection: function () {
                        return $scope.connection;
                    }
                }
            });

            modalInstance.result.then(function (connection) {
                SettingsService.updateConnection(connection)
                    .success(function (data) {
                        SettingsService.readConnections()
                            .success(function (data) {
                                $scope.connections = data;
                            })
                            .error(function (data, status) {
                                $scope.$emit('status_error', 'Error(' + status + ') reading connection: ' + data);
                            });
                    })
                    .error(function (data, status) {
                        $scope.$emit('status_error', 'Error(' + status + ') updating connection: ' + data);
                    });

                //TODO: remove
                $scope.connection = connection;
            }, function () {
                // closed
            });
        };

        $scope.openUser = function (user) {
            $scope.user = user;
            var modalInstance = $modal.open({
                templateUrl: '/views/user.html',
                controller: 'UserCtrl',
                backdrop: 'static',
                resolve: {
                    user: function () {
                        return $scope.user;
                    }
                }
            });

            modalInstance.result.then(function (user) {
                SettingsService.updateUser(user)
                    .success(function (data) {
                        readUsers();
                    })
                    .error(function (data, status) {
                        $scope.$emit('status_error', 'Error(' + status + ') updating connection: ' + data);
                    });

            }, function () {
                // closed
            });
        };

    });