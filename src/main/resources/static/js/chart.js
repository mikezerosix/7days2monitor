'use strict';

google.load('visualization', '1', { packages: ['corechart', 'controls'], 'language': 'en_UK'});

google.setOnLoadCallback(function () { angular.bootstrap(document.body, ['chartsApp']); });

angular.module('chartsApp', [])
    .controller('ChartsCtrl', ['$scope', '$http', '$timeout', function ($scope, $http, $timeout) {
        var readData = function () {
            $http.get('/protected/stats/days/3')
                .success(function (data) {
                    parseData(data);
                })
                .error(function (status) {
                    alert('Failed reading chart data error ' + status);
                });
            //$timeout(function () { readData(); }, 1000*30);
        };
        readData();
        var dashboard = new google.visualization.Dashboard(document.getElementById('dashboarddiv'));

        var chart = new google.visualization.ChartWrapper({
            'chartType': 'LineChart',
            'containerId': 'chartdiv',
            'options':  {
                dateFormat: 'dd.MM.yy hh:mm',
                height: 700,
                is3D: true,
                chartArea:{left:40,top:40,width:'85%',height:'90%'},
                hAxis: {format: 'dd.MM.yyyy HH:mm'},
                vAxis: {'minValue': 0, viewWindow: {min: 0}},
                title: 'Server stats'
            }
        });

        var recordedRangeSlider = new google.visualization.ControlWrapper({
            'controlType': 'ChartRangeFilter',
            'containerId': 'controlsdiv',
            'options': {
                'filterColumnLabel': 'Recorded',
                'ui':  { chartOptions: {
                    dateFormat: 'dd.MM.yy hh:mm',
                    height: 100,
                    is3D: true,
                    chartArea:{left:40,top:0,width:'85%',height:'100%'},
                    hAxis:  { 'textPosition': 'none'},
                    'vAxis': {
                        'textPosition': 'none',
                        'gridlines': {'color': 'none'}
                    }
                }}
            }
        });
        dashboard.bind(recordedRangeSlider, chart);

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
            dashboard.draw($scope.dataview);
        };

        $scope.shownCols = [0];
        $scope.hideColumns = function() {
            $scope.dataview.setColumns($scope.shownCols.filter(function(n){ return n != undefined }) );
            dashboard.draw($scope.dataview);
        };

    }]);