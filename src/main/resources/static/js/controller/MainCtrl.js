'use strict';

sevenMonitor.controller('MainCtrl', function ($scope, $q, $http, SettingsService, TelnetService, FTPService) {
    $scope.Math = window.Math;

    $scope.uptime;
    SettingsService.uptime()
        .success(function (data) {
            $scope.uptime = data;
        })
        .error(function (status) {
            $scope.$broadcast('status_error', 'Reading uptime failed, error ' + status);
        });

    $scope.gameServerStatus = {};
    TelnetService.serverInfo()
        .success(function (data) {
            $scope.gameServerStatus = data;
        })
        .error(function (status) {
            $scope.$broadcast('status_error', 'Reading GameServer status failed, error ' + status);
        });


    $scope.telnetStatus;
    TelnetService.status()
        .success(function (data) {
            $scope.telnetStatus = data;
        })
        .error(function (status) {
            $scope.$broadcast('status_error', 'Reading Telnet status failed, error ' + status);
        });
    $scope.toggleTelnet = function () {
        if ($scope.telnetStatus.monitoring && $scope.telnetStatus.connected) {
            TelnetService.disconnect()
                .success(function (data) {
                    $scope.telnetStatus = data;
                })
                .error(function (status) {
                    $scope.$broadcast('status_error', 'Reading Telnet disconnect failed, error ' + status);
                });
        } else {
            TelnetService.connect()
                .success(function (data) {
                    $scope.telnetStatus = data;
                })
                .error(function (status) {
                    $scope.$broadcast('status_error', 'Reading Telnet connect failed, error ' + status);
                });
        }
    };

    $scope.ftpStatus;
    FTPService.isConnected()
        .success(function (data) {
            $scope.ftpStatus = data;
        })
        .error(function (status) {
            $scope.ftpStatus = false;
            $scope.$broadcast('status_error', 'Reading FTP status failed, error ' + status);
        });

    $scope.toggleFTP = function () {
        if ($scope.ftpStatus) {
            FTPService.connect()
                .success(function (data) {
                    $scope.ftpStatus = data;
                })
                .error(function (status) {
                    $scope.$broadcast('status_error', 'Reading FTP connect failed, error ' + status);
                });
        } else {
            FTPService.disconnect()
                .success(function (data) {
                    $scope.ftpStatus = data;
                })
                .error(function (status) {
                    $scope.$broadcast('status_error', 'Reading FTP disconnect failed, error ' + status);
                });
        }
    };


    $scope.news = {};
    $scope.readNews = function () {
        SettingsService.latest()
            .success(function (data) {
                $scope.news = data;
            })
            .error(function (status) {
                alert(status);
                $scope.$broadcast('status_error', 'Reading Steam GetNewsForApp, error ' + status);
            });
    };
    $scope.readNews();


    $scope.stat = {'mem' : 22};

});