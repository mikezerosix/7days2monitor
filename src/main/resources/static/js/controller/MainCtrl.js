'use strict';

sevenMonitor.controller('MainCtrl', function ($scope, $rootScope, $q, $http, $timeout, SettingsService, TelnetService, FTPService, StatService) {

    $rootScope.currTab['main'] = true;
    $scope.Math = window.Math;
    $scope.errors = $rootScope.errors;
    $scope.uptime;
    SettingsService.uptime()
        .success(function (data) {
            $scope.uptime = data;
        })
        .error(function (status) {
            $scope.$emit('status_error', 'Reading uptime failed, error ' + status);
        });

    $scope.difficultyLabels = ['Easiest', 'Easy', 'Normal', 'Hard', 'Hardest'];

    $scope.gameServerStatus = {};
    var getServerInfo = function () {
        TelnetService.serverInfo()
            .success(function (data) {
                $scope.gameServerStatus = data;
                $scope.$emit('serverName', data.game);
            })
            .error(function (status) {
                $scope.$emit('status_error', 'Reading GameServer status failed, error ' + status);
            });
    };
    getServerInfo();
    $scope.telnetSwitch = false;
    $scope.telnetStatus;
    TelnetService.status()
        .success(function (data) {
            $scope.telnetStatus = data;
            $scope.telnetSwitch = ($scope.telnetStatus == 'MONITORING' || $scope.telnetStatus == 'CONNECTED');
        })
        .error(function (status) {
            $scope.$emit('status_error', 'Reading Telnet status failed, error ' + status);
        });


    $scope.toggleTelnet = function () {
        if ($scope.telnetSwitch) {
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
            $scope.ftpStatus = (data == 'true');
        })
        .error(function (status) {
            $scope.ftpStatus = false;
            $scope.$emit('status_error', 'Reading FTP status failed, error ' + status);
        });

    $scope.toggleFTP = function () {
        if ($scope.ftpStatus) {
            FTPService.connect()
                .success(function (data) {
                    $scope.ftpStatus = data;
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

    $scope.readSteamNews = function () {
        $scope.$emit('show_loading', 'readNews');
        SettingsService.latestSteam()
            .success(function (data) {
                $scope.news.steam = data.appnews.newsitems[0];
                $scope.$emit('status_info', 'Read Steam News For 7 Days to Die');
            })
            .error(function (status) {
                $scope.$emit('status_error', 'Error(' + status + ') Reading Steam News For 7 Days to Die');
            });
    };
    $scope.readTumblrNews = function () {
        SettingsService.latestTumblr()
            .success(function (data) {
                $scope.news.tumblr = data.posts[0];
                $scope.$emit('status_info', 'Read Tumbl News For 7 Days to Die');
            })
            .error(function (status) {
                $scope.$emit('status_error', 'Error(' + status + ') Reading Tumbl News For 7 Days to Die');
            });

        $scope.$emit('hide_loading', 'readNews');
    };
    $scope.readSteamNews();
    $scope.readTumblrNews();


    $scope.heartbeat = 999999;
    var heartbeat = function () {
        $scope.heartbeat++;

        $timeout(function () {
            heartbeat();
        }, 1000);
    };
    heartbeat();
    $scope.stat = {};
    StatService.getStats().success(function (data) {
        $scope.stat = data;
        $scope.heartbeat = (data.ts - data.current.recorded) / 1000;
    }).error(function (data, status) {
        $scope.$emit('status_error', 'Error(' + status + ') Reading initial stats failed.');
    });
    $scope.$on('STAT', function (event, message) {
        $scope.stat = message.data;
        if (typeof message.timestamp != 'undefined' && typeof $scope.stat != 'undefined' && typeof $scope.stat.current != 'undefined') {
            $scope.heartbeat = window.Math.round((message.timestamp - $scope.stat.current.recorded) / 1000);
        }
    });
    $scope.$on('TELNET_STATUS', function (event, message) {
        if ($scope.telnetStatus != 'MONITORING' && message.data == 'MONITORING') {
            getServerInfo();
        }
        $scope.telnetStatus = message.data;
        $scope.telnetSwitch = ($scope.telnetStatus == 'MONITORING' || $scope.telnetStatus == 'CONNECTED');
    });
});