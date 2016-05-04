'use strict';

angular.module('windoctorApp')
    .controller('ConsumptionController', function ($scope, Consumption, ConsumptionSearch, ParseLinks) {
        $scope.consumptions = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Consumption.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.consumptions = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Consumption.get({id: id}, function(result) {
                $scope.consumption = result;
                $('#deleteConsumptionConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Consumption.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteConsumptionConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ConsumptionSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.consumptions = result;
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
            $scope.consumption = {amount: null, creation_date: null, relative_date: null, id: null};
        };
    });
