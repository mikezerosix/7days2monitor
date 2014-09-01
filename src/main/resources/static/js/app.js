'use strict';

var sevenMonitor = angular.module('sevenMonitor', ['ngRoute', 'ngResource', 'ui.bootstrap'])
  .config(['$routeProvider', function ($routeProvider) {

    var checkRouting = function ($q, $location, $http, $rootScope) {

        $http.get('/protected/login')
          .success(function (data) {
             if (data == 'true') {
                    $rootScope.authorized = true;
             } else {
                   // $location.path("/login").replace();
             }
          })
          .error(function (status) {
                alert('Error checking session user: ' + status);

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
      .when('/login', {
        templateUrl: '/views/login.html',
        controller: 'LoginCtrl'
      });


  }])
   .run( function($rootScope, $location) {

      // register listener to watch route changes
      $rootScope.$on( "$routeChangeStart", function(event, next, current) {
        if ( $rootScope.authorized == null ) {
          // no logged user, we should be going to #login
          if ( next.templateUrl == "/views/login.html" ) {
            // already going to #login, no redirect needed
          } else {
            // not going to #login, we should redirect now
            $location.path( "/login" );
          }
        }
      });
   })

