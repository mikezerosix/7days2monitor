'use strict';

sevenMonitor
    .controller('ChatCtrl', function ($scope, $rootScope, $q, $timeout, $http, $window, SettingsService, TelnetService, PlayerService) {
        $rootScope.currTab['chat'] = true;
        $scope.chatDays = [];
        $scope.today = '';
        $scope.processing = false;
        $scope.loading = true;
        $scope.players;
        $scope.getPlayers = function () {
            PlayerService.getPlayers()
                .success(function (data) {
                    $scope.players = data;
                })
                .error(function (status) {
                    alert(status);
                });
        };
        $scope.getPlayers();
        $scope.readDays = function () {
            TelnetService.readChatDays()
                .success(function (data) {
                    $scope.chatDays = data;
                })
                .error(function (status) {
                    $scope.$broadcast('status_error', 'Reading chat days failed, error ' + status);
                });
        };
        $scope.readDay = function (day) {
            $scope.processing = true;
            $scope.$emit('show_loading', '');
            TelnetService.readChatDay(day)
                .success(function (data) {
                    var scroll = false;
                    if (typeof day == 'undefined') {
                        day = data[0].date;
                        $scope.today = day;
                        scroll = true;
                    }
                    $scope.chatDays[day] = data;
                    if (scroll) {
                        $timeout($scope.scrollTo, 100);
                    }
                })
                .error(function (status) {
                    $scope.$broadcast('status_error', 'Reading chat failed, error ' + status);

                });
            $scope.loading = false;
            $scope.$emit('hide_loading', '');
            $scope.processing = false;
        };
        $scope.hideDay = function (day) {
            $scope.chatDays[day] = [];
        };

        $scope.readDays();
        $scope.readDay();

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
            //console.log(JSON.stringify(message));
            var day = message.data.date;
            $scope.chatDays[day] = $scope.chatDays[day] || [];
            $scope.chatDays[day].push(message.data);
            $scope.scrollTo();
            //todo: this is not reliable way to know if the log file has actually rolled !!!
            if ($scope.today != day) {
                $scope.readDays();
                $scope.readDay();
            }
        });
        $scope.$on('PLAYER', function (event, message) {
            $scope.getPlayers();
        });
        $scope.scrollTo = function () {
            var el = $window.document.getElementById('lastMessage');
            if (el) {
                el.scrollIntoView();
            }
        }
    });