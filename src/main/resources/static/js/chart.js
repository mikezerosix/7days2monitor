'use strict';

google.load('visualization', '1', {
    packages: ['corechart', 'controls'],
    'language': 'en_UK'
});

google.setOnLoadCallback(function () {
    angular.bootstrap(document.body, ['chartsApp']);
});

angular.module('chartsApp', []).
    controller('ChartsCtrl', ['$scope', '$http', function ($scope, $http) {

        $http.get('/protected/stats/days/3')
            .success(function (data) {
                parseData(data);
            })
            .error(function (status) {
                alert('Failed reading chart data error ' + status);
            });

        var dashboard = new google.visualization.Dashboard(document.getElementById('dashboarddiv'));

        var parseData = function (jsonData) {
            var data = new google.visualization.DataTable();
            data.addColumn('datetime', 'Recorded');
            data.addColumn('number', 'Uptime (h)');
            data.addColumn('number', 'Players');
            data.addColumn('number', 'Zombies');
            data.addColumn('number', 'NPCs');
            data.addColumn('number', 'Entities');
            data.addColumn('number', 'Items');
            data.addColumn('number', 'Mem Heap');
            data.addColumn('number', 'Mem Max');
            data.addColumn('number', 'Chunks');
            data.addColumn('number', 'CGO');
            data.addColumn('number', 'FPS');

            for (var i in jsonData) {
                data.addRow([
                    new Date(jsonData[i].recorded),
                    jsonData[i].uptime/360,
                    jsonData[i].players,
                    jsonData[i].zombies,
                    jsonData[i].entities,
                    jsonData[i].entities2,
                    jsonData[i].items,
                    jsonData[i].memHeap,
                    jsonData[i].memMax,
                    jsonData[i].chunks,
                    jsonData[i].cgo,
                    jsonData[i].fps
                ]);
            }


            $scope.dataview = new google.visualization.DataView(data);
            draw();
        };

        $scope.hideColumns = function(colToHide) {
            alert('hiding '+colToHide );
            $scope.dataview.hideColumns([colToHide]);
            // you can also use dataview.setColumns([1,2]) to show only selected columns and hide the rest
            draw();
        };
        $scope.logScale = false;
        var draw = function() {
            var chart = new google.visualization.LineChart(document.getElementById('chartdiv'));
            chart.draw($scope.dataview, {
                dateFormat: 'dd.MM.yy hh:mm',
                height: 800,
                is3D: true,
                chartArea:{left:40,top:40,width:'90%',height:'80%'},
                explorer: {},
                hAxis:  {format: 'dd.MM.yyyy hh:mm'},
                vAxis: {logScale: $scope.logScale, minValue: 0},
                title: 'Server stats'
            });
        }

    }]);