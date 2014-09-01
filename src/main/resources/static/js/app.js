'use strict';

var sevenMonitor = angular.module('sevenMonitor', ['ngRoute', 'ngResource'])
  .config(function ($routeProvider) {

    var checkRouting = function ($q, $rootScope, $location) {
      if ($rootScope.user) {
        return true;
      } else {
        $location.path("/login");
        return false;
      }
    };

    $routeProvider
      .when('/', {
        templateUrl: '/views/main.html',
        controller: 'MainCtrl',
        resolve: {
          authorized: checkRouting
        }
      })
      .when('/login', {
        templateUrl: '/views/login.html',
        controller: 'LoginCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });


  });

