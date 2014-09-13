'use strict';

sevenMonitor.factory('TelnetService', function ($q, $http) {

    var status = function () {
        return $http.get('/protected/telnet');
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
        serverInfo: serverInfo,
        connect: connect,
        disconnect: disconnect,
        raw: raw,
        chat: chat,
        say: say,
        sendCmd: sendCmd
    };
});