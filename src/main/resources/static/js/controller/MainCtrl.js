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
    $scope.connectTelnet = function () {
        TelnetService.connect()
            .success(function (data) {
                $scope.telnetStatus = data;
            })
            .error(function (status) {
                $scope.$broadcast('status_error', 'Reading Telnet connect failed, error ' + status);
            });
    };
    $scope.disconnectTelnet = function () {
        TelnetService.disconnect()
            .success(function (data) {
                $scope.telnetStatus = data;
            })
            .error(function (status) {
                $scope.$broadcast('status_error', 'Reading Telnet disconnect failed, error ' + status);
            });
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
    $scope.connectFTP = function () {
        FTPService.connect()
            .success(function (data) {
                $scope.ftpStatus = data;
            })
            .error(function (status) {
                $scope.$broadcast('status_error', 'Reading FTP connect failed, error ' + status);
            });
    };

    $scope.disconnectFTP = function () {
        FTPService.disconnect()
            .success(function (data) {
                $scope.ftpStatus = data;
            })
            .error(function (status) {
                $scope.$broadcast('status_error', 'Reading FTP disconnect failed, error ' + status);
            });
    };
});