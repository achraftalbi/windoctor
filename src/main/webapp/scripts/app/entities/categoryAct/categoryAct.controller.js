'use strict';

angular.module('windoctorApp')
    .controller('CategoryActController', function ($scope, CategoryAct, CategoryActSearch, ParseLinks) {
        $scope.categoryActs = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            CategoryAct.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.categoryActs = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            CategoryAct.get({id: id}, function(result) {
                $scope.categoryAct = result;
                $('#deleteCategoryActConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            CategoryAct.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCategoryActConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            CategoryActSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.categoryActs = result;
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
            $scope.categoryAct = {name: null, id: null};
        };
    });
