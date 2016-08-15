'use strict';

angular.module('windoctorApp')
    .controller('Type_structureController', function ($scope, Type_structure, Type_structureSearch, ParseLinks) {
        $scope.type_structures = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Type_structure.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.type_structures = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Type_structure.get({id: id}, function(result) {
                $scope.type_structure = result;
                $('#deleteType_structureConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Type_structure.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteType_structureConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            Type_structureSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.type_structures = result;
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
            $scope.type_structure = {description_en: null, description_fr: null, id: null};
        };
    });
