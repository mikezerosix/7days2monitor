'use strict';

sevenMonitor.controller('MainCtrl', function ($scope, $q, $http, $timeout, SettingsService, TelnetService, FTPService, StatService) {
    $scope.Math = window.Math;

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
                console.log('error:' + status);
                $scope.$emit('status_error', 'Error(' + status + ') Reading Tumbl News For 7 Days to Die');
            });

        $scope.$emit('hide_loading', 'readNews');
    };
    $scope.readSteamNews();
    $scope.readTumblrNews();

    $scope.stat = {};
    $scope.heartbeat = 999999;
    var heartbeat = function () {
        $scope.heartbeat++;

        $timeout(function () {
            heartbeat();
        }, 1000);
    };
    heartbeat();
    $scope.$on('STAT', function (event, message) {
        $scope.stat = message.data;
        console.log('on stat  ' + JSON.stringify(message));
        if (typeof message.timestamp != 'undefined' && typeof $scope.stat != 'undefined' && typeof $scope.stat.current != 'undefined') {
            $scope.heartbeat = window.Math.round((message.timestamp - $scope.stat.current.recorded) / 1000);
        }
    });
});