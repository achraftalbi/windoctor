'use strict';

angular.module('windoctorApp')
    .controller('TestEntity4Controller', function ($scope, TestEntity4, TestEntity4Search, ParseLinks) {
        $scope.testEntity4s = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            TestEntity4.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.testEntity4s = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TestEntity4.get({id: id}, function(result) {
                $scope.testEntity4 = result;
                $('#deleteTestEntity4Confirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TestEntity4.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTestEntity4Confirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            TestEntity4Search.query({query: $scope.searchQuery}, function(result) {
                $scope.testEntity4s = result;
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
            $scope.testEntity4 = {description: null, id: null};
        };
    });
