'use strict';

var sevenMonitor = angular.module('sevenMonitor', ['ngRoute', 'ngResource'])
  .config(function ($routeProvider) {

    var checkRouting = function ($q, $rootScope, $location) {
      alert('do you have access ?' );
      if ($rootScope.user) {
        alert('you have access' );
        return true;
      } else {
        alert('no user: ' + $rootScope.user);
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
      .when('/main', {
        templateUrl: '/views/main.html',
        controller: 'MainCtrl',
        resolve: {
          authorized: checkRouting
        }

      })
      .when('/login', {
        templateUrl: '/views/login.html',
        controller: 'LoginCtrl'
      });


  });

