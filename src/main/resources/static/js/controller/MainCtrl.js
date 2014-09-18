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

    $scope.difficultyLabels = ['Easiest','Easy','Normal','Hard','Hardest'];

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

    $scope.readNews = function () {
        $scope.$emit('show_loading', 'readNews');
        SettingsService.latestSteam()
            .success(function (data) {
                $scope.news.steam = data.appnews.newsitems[0];
                $scope.$emit('status_info', 'Read Steam News For 7 Days to Die');
            })
            .error(function (status) {
                $scope.$emit('status_error', 'Error(' + status + ') Reading Steam News For 7 Days to Die');
            });
        SettingsService.latestTumbl()
        .success(function (data) {
          $scope.news.tumbl = data.posts[0];
          $scope.$emit('status_info', 'Read Tumbl News For 7 Days to Die');
        })
        .error(function (status) {
            console.log('error:'+ status );
          $scope.$emit('status_error', 'Error(' + status + ') Reading Tumbl News For 7 Days to Die');
        });

      $scope.$emit('hide_loading', 'readNews');
    };
    $scope.readNews();


    $scope.stat = {};
    $scope.heartbeat = 999999;
    var pollStats = function () {
        $scope.heartbeat++;
        if ($scope.heartbeat >= 30 && $scope.heartbeat%5) {
            $scope.$emit('show_loading', 'pollStats');
            StatService.getStats()
                .success(function (data) {
                    $scope.stat = data;
                    if (typeof $scope.stat !== 'undefined' && typeof $scope.stat.current !== 'undefined') {
                      $scope.heartbeat = window.Math.round(($scope.stat.ts - $scope.stat.current.recorded) / 1000);
                    }
                })
                .error(function (status) {
                    $scope.$emit('status_error', 'Reading Steam GetNewsForApp, error ' + status);
                });
            $scope.$emit('hide_loading', 'pollStats');
        }
        $timeout(function () {
            pollStats();
        }, 1000);
    };
    pollStats();

    $scope.testEmit = function () {
        console.log('sending emit');
        $scope.$emit('status_error', 'ERR test ');
    }

});