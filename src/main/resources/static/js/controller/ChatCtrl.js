'use strict';

sevenMonitor
.controller('ChatCtrl',  function($scope, $q, $http, $timeout,  $location, $anchorScroll, SettingsService, TelnetService) {
   $scope.chatLog = [];
   $scope.processing = false;
   $scope.loading = true;

   var readChat = function() {
        $scope.processing = true;
        var lastLine;
        if ($scope.chatLog && $scope.chatLog.lenght > 0) {
             lastLine = $scope.chatLog[$scope.chatLog.lenght-1];
        }
        $timeout(function() {
             TelnetService.chat(lastLine)
                .success(function (data) {
                   $scope.chatLog = data;
                   $location.hash('bottom');
                   $anchorScroll();
                })
                .error(function (status) {
                    alert(status);
                    return false;
                });
                 $scope.loading = false;
               readChat();
           }, 1000);
         $scope.processing = false;
   }
   readChat();

   $scope.message;
   $scope.messageAs;
   $scope.useAs;
   $scope.send = function() {
       console.log('sending message ' +$scope.message );
       var msg = $scope.message;
       if ($scope.useAs) {
           msg = '[' + $scope.messageAs + '] ' +  $scope.message;
       }
       TelnetService.say(msg)
           .success(function (data) {
              $scope.status = data;
              $scope.message = undefined;
           })
           .error(function (status) {
               alert(status);
           });
   }
});