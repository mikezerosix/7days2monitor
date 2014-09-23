'use strict';

sevenMonitor.factory('CometService', function ($q, $http, $rootScope, $timeout) {
    $rootScope.lastMessage = 0;
    $rootScope.polling = false;
    var cometPoll = function () {

        $http({method: 'GET', url: '/protected/comet/' + $rootScope.lastMessage})
            .success(function (data, status, headers, config) {
                for (var i in data) {
                    console.log("Comet msg:" + data[i].messageTarget);
                    $rootScope.lastMessage = data[i].timestamp;
                    $rootScope.$broadcast(data[i].messageTarget, data[i]);
                }
                $timeout(function () {
                    cometPoll();
                }, 500);
            })
            .error(function (data, status) {
                $rootScope.$emit('status_error', 'CometService error(' + status + '):' + data);
                $timeout(function () {
                    cometPoll();
                }, 10000);
            });
    };
    var start = function () {
        if (!$rootScope.polling) {
            $rootScope.polling = true;
            cometPoll();
        }
    };

    return {
        start: start
    }
});