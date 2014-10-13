'use strict';

sevenMonitor
  .controller('StatusCtrl', function($scope, $rootScope, $location, $anchorScroll, CometService) {

    $scope.$watch(function() {
      return $rootScope.authorized;
    }, function() {
      CometService.start();
    }, true);

    $scope.isLoading = false;
    $scope.messages = [];
    $scope.status = {'state': 'status-init', 'msg': 'ok'};
    $rootScope.errors = [];
    $rootScope.$on('status_clear', function(event, message) {
            $scope.status.state = 'ok';
            $scope.messages = [];
    });
    $rootScope.$on('status_error', function(event, message) {
      $scope.status.state = 'status-error';
      $scope.status.msg = message;
      addMsg(message);
    });

    $rootScope.$on('status_warn', function(event, message) {
      $scope.status.state = 'status-warn';
      $scope.status.msg = message;
      addMsg(message);
    });

    $rootScope.$on('status_info', function(event, message) {
      $scope.status.state = 'status-ok';
      $scope.status.msg = message;
      addMsg(message);
    });

    $rootScope.$on('ERROR', function(event, message) {
      $scope.status.state = 'status-error';
      $scope.status.msg = message;
      $rootScope.errors.push(message);
      addMsg(message);
    });

    $rootScope.$on('show_loading', function(event, message) {
      //console.log('show loading ');
      $scope.isLoading = true;
    });

    $rootScope.$on('hide_loading', function(event, message) {
      //console.log('hide loading ');
      $scope.isLoading = false;
    });

    var addMsg = function(msg) {
      $scope.messages.unshift(msg);
    };

    $scope.expandStatusMessages = '';
    $scope.showMessages = function() {
      if ($scope.expandStatusMessages == '') {
        $scope.expandStatusMessages = 'status-messages-expanded';
      } else {
        $scope.expandStatusMessages = '';
      }
    }
  });

