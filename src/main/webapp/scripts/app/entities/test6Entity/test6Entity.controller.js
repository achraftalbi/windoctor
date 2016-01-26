'use strict';

angular.module('windoctorApp')
    .controller('Test6EntityController', function ($scope, Test6Entity, Test6EntitySearch, ParseLinks) {
        $scope.test6Entitys = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Test6Entity.query({page: $scope.page, per_page: 5}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.test6Entitys.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.test6Entitys = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Test6Entity.get({id: id}, function(result) {
                $scope.test6Entity = result;
                $('#deleteTest6EntityConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Test6Entity.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteTest6EntityConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            Test6EntitySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.test6Entitys = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.test6Entity = {description: null, id: null};
        };
    });
