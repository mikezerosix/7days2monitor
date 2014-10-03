'use strict';

sevenMonitor
    .controller('ChatCtrl', function ($scope, $q, $timeout, $http, $window, SettingsService, TelnetService) {
        $scope.chatHistory = [];
        $scope.chatLog = [];
        $scope.today;
        $scope.processing = false;
        $scope.loading = true;
        var setToday = function(msg) {
            $scope.today = msg.date;
        };
        $scope.readChat = function () {
            $scope.processing = true;
            $scope.$emit('show_loading', '');
            TelnetService.chat()
                .success(function (data) {
                    $scope.chatLog = data;
                    setToday(data[data.length-1]);
                    $timeout( $scope.scrollTo, 100);
                })
                .error(function (status) {
                    $scope.$broadcast('status_error', 'Reading chat failed, error ' + status);
                    return false;
                });
            $scope.loading = false;
            $scope.$emit('hide_loading', '');
            $scope.processing = false;
        };

        $scope.readChat();
        $scope.readOlder = function () {
            alert('todo');
        };
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
            $scope.scrollTo();
            //todo: this is not reliable way to know if the log file has actually rolled !!!
            if ($scope.today != msg.date) {
                $scope.readChat();
            }
            setToday(message);
        });

        $scope.scrollTo = function() {
            var el = $window.document.getElementById('lastMessage');
            if (el) {
                el.scrollIntoView();
            }
        }
    });