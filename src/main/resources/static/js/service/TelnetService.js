'use strict';

sevenMonitor.factory('TelnetService', function ($http) {

    var status = function () {
        return $http.get('/protected/telnet');
    };
    var statusPromise = function ($q) {
        var deferred = $q.defer();
        status()
            .success(function (data) {
                deferred.resolve(data);
            })
            .error(function (data, status) {
                $scope.$emit('status_error', 'Reading uptime failed, error ' + status);
                deferred.reject(status);
            });
        return deferred.promise;
    };


    var serverInfo = function () {
        return $http.get('/protected/telnet/server-info');
    };

    var connect = function () {
        return $http.post('/protected/telnet');
    };

    var disconnect = function () {
        return $http.delete('/protected/telnet');
    };

    var chat = function () {
        return $http.get('/protected/telnet/chat');
    };

    var raw = function () {
        return $http.get('/protected/telnet/raw');
    };

    var say = function (msg) {
        return $http.post('/protected/telnet/say', msg);
    };

    var sendCmd = function (cmd) {
        return $http.post('/protected/telnet/send-cmd', cmd);
    };


    return {
        status: status,
        statusPromise: statusPromise,
        serverInfo: serverInfo,
        connect: connect,
        disconnect: disconnect,
        raw: raw,
        chat: chat,
        say: say,
        sendCmd: sendCmd
    };
});