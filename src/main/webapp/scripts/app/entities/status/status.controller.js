'use strict';

angular.module('windoctorApp')
    .controller('StatusController', function ($scope, Status, StatusSearch) {
        $scope.statuss = [];
        $scope.loadAll = function() {
            Status.query(function(result) {
               $scope.statuss = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Status.get({id: id}, function(result) {
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
            StatusSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.statuss = result;
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
            $scope.status = {description: null, id: null};
        };
    });
