'use strict';

angular.module('windoctorApp')
    .controller('EntityTest1Controller', function ($scope, EntityTest1, EntityTest1Search, ParseLinks) {
        $scope.entityTest1s = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            EntityTest1.query({page: $scope.page, per_page: 5}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.entityTest1s = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            EntityTest1.get({id: id}, function(result) {
                $scope.entityTest1 = result;
                $('#deleteEntityTest1Confirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            EntityTest1.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEntityTest1Confirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            EntityTest1Search.query({query: $scope.searchQuery}, function(result) {
                $scope.entityTest1s = result;
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
            $scope.entityTest1 = {description: null, id: null};
        };
    });
