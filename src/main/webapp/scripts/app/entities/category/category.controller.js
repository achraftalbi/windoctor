'use strict';

angular.module('windoctorApp')
    .controller('CategoryController', function ($scope, Category, CategorySearch, ParseLinks) {
        $scope.categorys = [];
        $scope.page = 1;
        $scope.searchCalled = false;
        $scope.loadAll = function () {
            Category.query({page: $scope.page, per_page: 5}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.categorys = result;
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            if($scope.searchCalled){
                $scope.loadAllSearch();
            }else{
                $scope.loadAll();
            }
        };

        $scope.loadAll();

        $scope.delete = function (id) {
            Category.get({id: id}, function (result) {
                $scope.category = result;
                $('#deleteCategoryConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Category.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCategoryConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            $scope.page = 1;
            $scope.searchCalled = true;
            $scope.loadAllSearch();
        };
        $scope.loadAllSearch = function () {
            CategorySearch.query({query: $scope.searchQuery,page: $scope.page, per_page: 5}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.categorys = result;
            }, function (response) {
                if (response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.category = {name: null, id: null};
        };
    });
