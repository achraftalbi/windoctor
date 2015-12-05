'use strict';

angular.module('windoctorApp')
    .controller('Supply_typeController', function ($scope, Supply_type, Supply_typeSearch, ParseLinks) {
        $scope.supply_types = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Supply_type.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.supply_types = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Supply_type.get({id: id}, function(result) {
                $scope.supply_type = result;
                $('#deleteSupply_typeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Supply_type.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSupply_typeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            Supply_typeSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.supply_types = result;
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
            $scope.supply_type = {description: null, id: null};
        };
    });
