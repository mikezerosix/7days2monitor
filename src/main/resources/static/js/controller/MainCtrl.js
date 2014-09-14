'use strict';

sevenMonitor.controller('MainCtrl', function ($scope, $q, $http, SettingsService, TelnetService, FTPService, StatService) {
    $scope.Math = window.Math;

    $scope.uptime;
    SettingsService.uptime()
        .success(function (data) {
            $scope.uptime = data;
        })
        .error(function (status) {
            $scope.$emit('status_error', 'Reading uptime failed, error ' + status);
        });

    $scope.gameServerStatus = {};
    TelnetService.serverInfo()
        .success(function (data) {
            $scope.gameServerStatus = data;
        })
        .error(function (status) {
            $scope.$emit('status_error', 'Reading GameServer status failed, error ' + status);
        });


    $scope.telnetStatus;
    TelnetService.status()
        .success(function (data) {
            $scope.telnetStatus = data;
        })
        .error(function (status) {
            $scope.$emit('status_error', 'Reading Telnet status failed, error ' + status);
        });
    $scope.toggleTelnet = function () {
        if ($scope.telnetStatus.monitoring && $scope.telnetStatus.connected) {
            TelnetService.disconnect()
                .success(function (data) {
                    $scope.telnetStatus = data;
                })
                .error(function (status, data) {
                    $scope.$emit('status_error', 'Reading Telnet disconnect failed, error: ' + status + data);
                });
        } else {
            TelnetService.connect()
                .success(function (data) {
                    $scope.telnetStatus = data;
                })
                .error(function (status) {
                    $scope.$emit('status_error', 'Reading Telnet connect failed, error:' + status);
                });
        }
    };

    $scope.ftpStatus;
    FTPService.isConnected()
        .success(function (data) {
            $scope.ftpStatus = (data== 'true');
        })
        .error(function (status) {
            $scope.ftpStatus = false;
            $scope.$emit('status_error', 'Reading FTP status failed, error ' + status);
        });

    $scope.toggleFTP = function () {
        if ($scope.ftpStatus) {
            FTPService.connect()
                .success(function (data) {
                    $scope.ftpStatus = data ;
                })
                .error(function (status) {
                    $scope.$emit('status_error', 'Reading FTP connect failed, error ' + status);
                });
        } else {
            FTPService.disconnect()
                .success(function (data) {
                    $scope.ftpStatus = data;
                })
                .error(function (status) {
                    $scope.$emit('status_error', 'Reading FTP disconnect failed, error ' + status);
                });
        }
    };


    $scope.news = {};
    $scope.readNews = function () {
        SettingsService.latest()
            .success(function (data) {
                $scope.news = data;
                $scope.$emit('status_info', 'Read Steam News For 7 Days to Die');
            })
            .error(function (status) {
                alert(status);
                $scope.$emit('status_error', 'Error(' + status + ') Reading Steam News For 7 Days to Die');
            });
    };
    $scope.readNews();


    $scope.stats =[];
    $scope.stat = {};
    $scope.readStats = function () {
        StatService.getStats()
            .success(function (data) {
                $scope.stats = data;
                $scope.stat = $scope.stats[ $scope.stats.length - 1];
            })
            .error(function (status) {
                $scope.$emit('status_error', 'Reading Steam GetNewsForApp, error ' + status);
            });
    };
    $scope.readStats();


    $scope.testEmit = function() {
        console.log('sending emit');
        $scope.$emit('status_error', 'ERR test ');
    }
});