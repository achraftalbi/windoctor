'use strict';

angular.module('windoctorApp')
    .controller('TestEntity2Controller', function ($scope, TestEntity2, TestEntity2Search, ParseLinks) {
        $scope.testEntity2s = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            TestEntity2.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.testEntity2s = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TestEntity2.get({id: id}, function(result) {
                $scope.testEntity2 = result;
                $('#deleteTestEntity2Confirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TestEntity2.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTestEntity2Confirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            TestEntity2Search.query({query: $scope.searchQuery}, function(result) {
                $scope.testEntity2s = result;
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
            $scope.testEntity2 = {description: null, id: null};
        };
    });
