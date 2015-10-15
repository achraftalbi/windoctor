'use strict';

angular.module('windoctorApp')
    .controller('TestEntity5Controller', function ($scope, TestEntity5, TestEntity5Search, ParseLinks) {
        $scope.testEntity5s = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            TestEntity5.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.testEntity5s = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TestEntity5.get({id: id}, function(result) {
                $scope.testEntity5 = result;
                $('#deleteTestEntity5Confirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TestEntity5.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTestEntity5Confirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            TestEntity5Search.query({query: $scope.searchQuery}, function(result) {
                $scope.testEntity5s = result;
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
            $scope.testEntity5 = {description5: null, id: null};
        };
    });
