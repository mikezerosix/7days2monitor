'use strict';

sevenMonitor
.controller('ChatCtrl',  function($scope, $q, $http, SettingsService, TelnetService) {
   $scope.chatLog = [];

   TelnetService.chat()
           .success(function (data) {
              $scope.chatLog =  data;

           })
           .error(function (status) {
               alert(status);
           });


});