'use strict';

var sevenMonitor = angular.module('sevenMonitor',['ngRoute','ngResource']);

sevenMonitor.config(['$routeProvider' , function($routeProvider) {

    $routeProvider
    .when('/', {
      templateUrl: 'views/main.html',
      controller: 'MainCtrl',
      resolve: {
            user: function(){
                return 'yyyy';
        }
      }
    })
    .when('/login', {
          templateUrl: 'views/login.html',
          controller: 'LoginCtrl'
      })
    ;
}]);

