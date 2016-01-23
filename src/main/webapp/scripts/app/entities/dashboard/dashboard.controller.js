'use strict';

angular.module('windoctorApp')
    .controller('DashboardController', function ($scope, Dashboard, DashboardSearch, ParseLinks,Language) {
        $scope.dashboardDTO;
        $scope.dashboards = [];
        $scope.lineLabels = [];
        $scope.lineDataFirst = [];
        $scope.lineData = [[]];
        $scope.barLabels = [];
        $scope.years = [];
        $scope.barDataFirst = [];
        $scope.barData = [[]];
        $scope.page = 1;
        $scope.currentLang;
        $scope.selectedYear=null;
        $scope.loadAll = function() {
            Dashboard.get({typeDashboard: 1, year: $scope.selectedYear}, function(result) {
                $scope.dashboardDTO = result;
                $scope.dashboards = $scope.dashboardDTO.dashboardList;
                $scope.years = $scope.dashboardDTO.years;
                if($scope.selectedYear===null) {
                    $scope.selectedYear = $scope.years[0];
                }
                for (var i = 0; i < $scope.years.length; i++) {
                    console.log("$scope.years[i] 1  "+$scope.years[i]);
                }

                Language.getCurrent().then(function(current) {
                    $scope.currentLang = current;
                    for (var i = 0; i < $scope.dashboards.length; i++) {
                        var start;
                        if ($scope.currentLang==='en') {
                            start = $scope.dashboards[i].descriptionEn;
                        }else{
                            start = $scope.dashboards[i].descriptionFr;
                        }
                        $scope.lineLabels[i] = start;
                        if($scope.dashboards[i].value === undefined || $scope.dashboards[i].value === null){
                            $scope.lineDataFirst[i] = 0;
                        }else{
                            $scope.lineDataFirst[i] = $scope.dashboards[i].value;
                        }
                        console.log("$scope.lineData[i] 1  "+$scope.lineDataFirst[i]);
                        $scope.lineData = [
                            $scope.lineDataFirst
                        ];
                    }
                });
            });
            Dashboard.get({typeDashboard: 2, year: $scope.selectedYear}, function(result) {
                $scope.dashboardDTO = result;
                $scope.dashboards = $scope.dashboardDTO.dashboardList;
                Language.getCurrent().then(function(current) {
                    $scope.currentLang = current;
                    for (var i = 0; i < $scope.dashboards.length; i++) {
                        var start;
                        if ($scope.currentLang==='en') {
                            start = $scope.dashboards[i].descriptionEn;
                        }else{
                            start = $scope.dashboards[i].descriptionFr;
                        }
                        $scope.barLabels[i] = start;
                        if($scope.dashboards[i].value === undefined || $scope.dashboards[i].value === null){
                            $scope.barDataFirst[i] = 0;
                        }else{
                            $scope.barDataFirst[i] = $scope.dashboards[i].value;
                        }
                        console.log("$scope.barDataFirst[i] 1  "+$scope.barDataFirst[i]);
                        $scope.barData = [
                            $scope.barDataFirst
                        ];
                    }
                });
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Dashboard.get({id: id}, function(result) {
                $scope.dashboard = result;
                $('#deleteDashboardConfirmation').modal('show');
            });
        };

        $scope.changeYear = function () {
            $scope.loadAll();
        };

        $scope.confirmDelete = function (id) {
            Dashboard.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteDashboardConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            DashboardSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.dashboards = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.dashboard = {value: null, descriptionEn: null, descriptionFr: null, id: null};
        };

        // Dashboard developpement
        //$scope.lineLabels = ["January", "February", "March", "April", "May", "June", "July","August","September","November","December"];
        //$scope.series = ['Series A', 'Series B'];
        $scope.lineSeries = ['Profit'];
        /*$scope.data = [
         [65, 59, 80, 81, 56, 55, 40],
         [28, 48, 40, 19, 86, 27, 90]
         ];*/

        //$scope.colors =['#FD1F5E','#1EF9A1','#7FFD1F','#68F000'];
        $scope.lineColors =['#FD1F5E','#1EF9A1'];
        /*[{
         fillColor: 'rgba(47, 132, 71, 0.8)',
         strokeColor: 'rgba(47, 132, 71, 0.8)',
         highlightFill: 'rgba(47, 132, 71, 0.8)',
         highlightStroke: 'rgba(47, 132, 71, 0.8)'
         }];*/
        $scope.onClickLine = function (points, evt) {
            console.log(points, evt);
        };



        //$scope.barLabels = ["January", "February", "March", "April", "May", "June", "July","August","September","November","December"];
        $scope.barSeries = ['All patients'];//,'New patients','Old patients'];
        /*$scope.data = [
         [65, 59, 80, 81, 56, 55, 40],
         [28, 48, 40, 19, 86, 27, 90]
         ];*/
        /*$scope.barData = [
            [28, 48, 40, 19, 86, 27, 90,70,40,30,20],
            [8, 20, 10, 9, 70, 7, 60,50,30,10,5],
            [20, 28, 30, 10, 16, 20, 30,20,10,20,15]
        ];*/
        //$scope.colors =['#FD1F5E','#1EF9A1','#7FFD1F','#68F000'];
        $scope.barColors =['#0994EB']//,'#FA0509','#1EF9A1'];
        /*[{
         fillColor: 'rgba(47, 132, 71, 0.8)',
         strokeColor: 'rgba(47, 132, 71, 0.8)',
         highlightFill: 'rgba(47, 132, 71, 0.8)',
         highlightStroke: 'rgba(47, 132, 71, 0.8)'
         }];*/
        $scope.onClickBar = function (points, evt) {
            console.log(points, evt);
        };


        $scope.doughnutLabels = ["New patients", "Old patients"];
        $scope.doughnutData = [329,200];
        $scope.doughnutColors =['#FF0004','#378C03'];



    });
