'use strict';

angular.module('windoctorApp')
    .controller('TreatmentController', function ($scope, Treatment, TreatmentSearch, ParseLinks) {
        $scope.treatments = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Treatment.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.treatments = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Treatment.get({id: id}, function(result) {
                $scope.treatment = result;
                $('#deleteTreatmentConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Treatment.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTreatmentConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            TreatmentSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.treatments = result;
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
            $scope.treatment = {treatment_date: null, description: null, price: null, paid_price: null, id: null};
        };
    });
