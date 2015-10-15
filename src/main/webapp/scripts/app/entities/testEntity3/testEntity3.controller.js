'use strict';

angular.module('windoctorApp')
    .controller('TestEntity3Controller', function ($scope, TestEntity3, TestEntity3Search, ParseLinks) {
        $scope.testEntity3s = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            TestEntity3.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.testEntity3s = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TestEntity3.get({id: id}, function(result) {
                $scope.testEntity3 = result;
                $('#deleteTestEntity3Confirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TestEntity3.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTestEntity3Confirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            TestEntity3Search.query({query: $scope.searchQuery}, function(result) {
                $scope.testEntity3s = result;
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
            $scope.testEntity3 = {description: null, id: null};
        };
    });
