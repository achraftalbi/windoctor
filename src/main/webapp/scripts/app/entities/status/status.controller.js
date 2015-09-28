'use strict';

angular.module('windoctorApp')
    .controller('StatusController', function ($scope, Status, StatusSearch, ParseLinks) {
        $scope.statuss = [];
        $scope.page = 1;
        $scope.loadAll = function () {
            Status.query({page: $scope.page, per_page: 5}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.statuss = result;
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Status.get({id: id}, function (result) {
                $scope.status = result;
                $('#deleteStatusConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Status.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteStatusConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            StatusSearch.query({query: $scope.searchQuery}, function (result) {
                $scope.statuss = result;
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
            $scope.status = {description: null, id: null};
        };
    });
