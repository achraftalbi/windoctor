'use strict';

angular.module('windoctorApp')
    .controller('PlanController', function ($scope, Plan, PlanSearch, ParseLinks) {
        $scope.plans = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Plan.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.plans = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Plan.get({id: id}, function(result) {
                $scope.plan = result;
                $('#deletePlanConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Plan.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePlanConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            PlanSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.plans = result;
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
            $scope.plan = {name: null, number: null, archive: null, creation_date: null, id: null};
        };
    });
