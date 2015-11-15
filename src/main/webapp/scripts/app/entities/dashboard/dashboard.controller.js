'use strict';

angular.module('windoctorApp')
    .controller('DashboardController', function ($scope, Dashboard, DashboardSearch, ParseLinks) {
        $scope.dashboards = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Dashboard.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.dashboards = result;
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
        $scope.labels = ["January", "February", "March", "April", "May", "June", "July"];
        $scope.series = ['Series A', 'Series B'];
        $scope.data = [
            [65, 59, 80, 81, 56, 55, 40],
            [28, 48, 40, 19, 86, 27, 90]
        ];
        $scope.colors =['#FD1F5E','#1EF9A1','#7FFD1F','#68F000'];
            /*[{
            fillColor: 'rgba(47, 132, 71, 0.8)',
            strokeColor: 'rgba(47, 132, 71, 0.8)',
            highlightFill: 'rgba(47, 132, 71, 0.8)',
            highlightStroke: 'rgba(47, 132, 71, 0.8)'
        }];*/
        $scope.onClick = function (points, evt) {
            console.log(points, evt);
        };


    });
