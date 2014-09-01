'use strict';

var sevenMonitor = angular.module('sevenMonitor',['ngRoute','ngResource'])
.config(function ($routeProvider) {

    $routeProvider
    .when('/', {
      templateUrl: '/views/main.html',
      controller: 'MainCtrl',
        resolve: {
          loggedInUser: ['$q', function($q) {
            var deferred = $q.defer();
            deferred.resolve({name: 'foo'});
            return deferred.promise;
          }]
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

