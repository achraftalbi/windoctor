'use strict';

angular.module('windoctorApp')
    .controller('MedicationController', function ($scope, Medication, MedicationSearch, ParseLinks) {
        $scope.medications = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Medication.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.medications = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Medication.get({id: id}, function(result) {
                $scope.medication = result;
                $('#deleteMedicationConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Medication.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMedicationConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            MedicationSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.medications = result;
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
            $scope.medication = {name: null, man_description: null, woman_description: null, child_description: null, id: null};
        };
    });
