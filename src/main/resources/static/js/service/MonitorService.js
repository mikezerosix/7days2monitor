'use strict';

sevenMonitor.factory('MonitorService', function ($q, $http) {

    var getMonitors = function () {
        return $http.get('/protected/monitors');
    };
    var saveMonitor = function (monitor) {
        return $http.post('/protected/monitors',monitor);
    };
    return {
        getMonitors: getMonitors,
        saveMonitor: saveMonitor
    };
});