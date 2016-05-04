'use strict';

angular.module('windoctorApp')
    .controller('CategoryChargeController', function ($scope, CategoryCharge, CategoryChargeSearch, ParseLinks) {
        $scope.categoryCharges = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            CategoryCharge.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.categoryCharges = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            CategoryCharge.get({id: id}, function(result) {
                $scope.categoryCharge = result;
                $('#deleteCategoryChargeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            CategoryCharge.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCategoryChargeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            CategoryChargeSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.categoryCharges = result;
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
            $scope.categoryCharge = {name: null, id: null};
        };
    });
