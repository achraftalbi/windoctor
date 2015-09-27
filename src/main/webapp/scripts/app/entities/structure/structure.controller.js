'use strict';

angular.module('windoctorApp')
    .controller('StructureController', function ($scope, Structure, StructureSearch, ParseLinks) {
        $scope.structures = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Structure.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.structures = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Structure.get({id: id}, function(result) {
                $scope.structure = result;
                $('#deleteStructureConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Structure.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteStructureConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            StructureSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.structures = result;
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
            $scope.structure = {name: null, id: null};
        };
    });
