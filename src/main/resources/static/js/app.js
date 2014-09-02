'use strict';

var sevenMonitor = angular.module('sevenMonitor', ['ngRoute', 'ngResource', 'ui.bootstrap'])
  .config(['$routeProvider', function ($routeProvider) {

    var checkRouting = function ($q, $location, $http, $rootScope) {

        $http.get('/protected/login')
          .success(function (data) {
              console.log(' requestUser= ' + (data == 'true'))
             $rootScope.authorized = (data == 'true');
          })
          .error(function (status) {
                alert('Error checking session user: ' + status);
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
      .when('/players', {
        templateUrl: '/views/players.html',
        controller: 'LoginCtrl'
      });


  }])


