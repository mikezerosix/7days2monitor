'use strict';
sevenMonitor
    .controller('LoginCtrl', function ($rootScope, $scope, $http, $q, $location, $route, $routeParams) {
        $scope.user = {name: '', password: ''};
        $scope.message = '';
        $scope.submit = function () {
            $http.post('/public/login', $scope.user)
                .success(function (data) {
                    $rootScope.authorized = data;
                    $rootScope.setupDone = checkSettings($q, $http);
                })
                .error(function (data, status) {
                    $rootScope.$emit('status_error', 'Login failed, error ' + status);
                    $scope.message = 'Error: Invalid user or password';
                });
        };

        var checkSettings = function ($q, $http) {
            var deferred = $q.defer();
            $http.get('/protected/settings/connections')
                .success(function (data) {
                    var telnetOK = false;
                    for (var i in data) {
                        if (data[i].type == 'GAME_TELNET') {
                            if (data[i].address) {
                                console.log('telnet host ok : ' + data[i].address);
                                telnetOK = true;
                            } else {
                                console.log('telnet host not ok : ' + data[i].address);
                            }
                        }
                    }
                    deferred.resolve(telnetOK);
                    if (!telnetOK) {
                        $location.path('settings');
                    }
                })
                .error(function (data, status) {
                    alert('Error checking settings: ' + status);
                    deferred.resolve(false);
                });
            return deferred.promise;
        };
    });