var sevenMonitor = angular.module('sevenMonitor',['ngRoute']);

sevenMonitor.config(function($routeProvider){
    $routeProvider
    .when('/', {templateUrl: 'index.html', controller: 'MainController'});
});