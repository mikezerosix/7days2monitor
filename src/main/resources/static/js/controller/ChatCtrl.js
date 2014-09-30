'use strict';

sevenMonitor
    .controller('ChatCtrl', function ($scope, $q, $http, $window, SettingsService, TelnetService) {

        $scope.chatLog = [];
        $scope.processing = false;
        $scope.loading = true;

        var readChat = function () {
            $scope.processing = true;
            $scope.$emit('show_loading', '');
            TelnetService.chat()
                .success(function (data) {
                    $scope.chatLog = data;

                })
                .error(function (status) {
                    $scope.$broadcast('status_error', 'Reading chat failed, error ' + status);
                    return false;
                });
            $scope.loading = false;
            $scope.$emit('hide_loading', '');
            $scope.processing = false;
        };

        readChat();

        $scope.message;
        $scope.useAs;
        $scope.send = function () {
            var msg = $scope.message;
            if ($scope.useAs) {
                msg = '[' + $scope.authorized.name + '] ' + $scope.message;
            }
            TelnetService.say(msg)
                .success(function (data, status) {
                    $scope.message = undefined;
                })
                .error(function (data, status) {
                    $scope.$broadcast('status_error', 'Sending chat message failed, error(' + status + '): ' + data);
                });
        };

        $scope.$on('CHAT', function (event, message) {
            console.log(JSON.stringify(message));
            $scope.chatLog.push(message.data);
            var el = $window.document.getElementById('lastMessage');
            if (el) {
                el.scrollIntoView();
            }

        });

    });