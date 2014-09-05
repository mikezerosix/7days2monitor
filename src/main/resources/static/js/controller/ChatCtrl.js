'use strict';

sevenMonitor
.controller('ChatCtrl',  function($scope, $q, $http, SettingsService, TelnetService) {
   $scope.chatLog = [];

   var readChat = function() {}
   TelnetService.chat()
           .success(function (data) {
              $scope.chatLog =  data;

           })
           .error(function (status) {
               alert(status);
           });

   readChat();

    $scope.say;

    $scope.send = function() {
     TelnetService.say()
           .success(function (data) {
              $scope.status = data;
              readChat();
           })
           .error(function (status) {
               alert(status);
           });
    }
});