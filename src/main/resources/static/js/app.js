'use strict';

var sevenMonitor = angular.module('sevenMonitor', ['ngRoute', 'ngResource', 'ui.bootstrap'])
    .config(['$routeProvider', function ($routeProvider) {

        var checkRouting = function ($q, $location, $http, $rootScope) {
            $http.get('/public/login')
                .success(function (data) {
                    if (data != 'null') {
                        $rootScope.authorized = data;
                    } else {
                        $rootScope.authorized = false;
                    }
                })
                .error(function (data, status) {
                    $rootScope.$emit('status_error', 'Not logged in, ' + status);
                    $rootScope.authorized = false;
                });

        };


        $routeProvider
            .when('/', {
                templateUrl: '/views/main.html',
                controller: 'MainCtrl',
                resolve: {
                    authorized: checkRouting
                }
            })
            .when('/main', {
                templateUrl: '/views/main.html',
                controller: 'MainCtrl',
                resolve: {
                    authorized: checkRouting
                }

            })
            .when('/settings', {
                templateUrl: '/views/settings.html',
                controller: 'SettingsCtrl',
                resolve: {
                    authorized: checkRouting
                }

            })
            .when('/chat', {
                templateUrl: '/views/chat.html',
                controller: 'ChatCtrl',
                resolve: {
                    authorized: checkRouting
                }

            })
            .when('/players', {
                templateUrl: '/views/players.html',
                controller: 'PlayersCtrl',
                resolve: {
                    authorized: checkRouting
                }
            });


    }])

    .filter("asUptime", function () {
        return function (uptime) {
            return   Math.floor(uptime / 1000 / 60 / 60 / 60) + ' days '
                + Math.floor(uptime / 1000 / 60 / 60 % 60) + ' hours '
                + Math.floor(uptime / 1000 / 60 % 60) + ' min '
                + Math.floor((uptime / 1000) % 60) + ' s ' + ( Number.isNaN(uptime) ? '  - Batman !!!!!' : '');
        }
    });


