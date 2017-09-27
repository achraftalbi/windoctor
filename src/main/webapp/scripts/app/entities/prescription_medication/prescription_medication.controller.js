'use strict';

angular.module('windoctorApp')
    .controller('Prescription_medicationController', function ($scope, Prescription_medication, Prescription_medicationSearch, ParseLinks) {
        $scope.prescription_medications = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Prescription_medication.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.prescription_medications = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Prescription_medication.get({id: id}, function(result) {
                $scope.prescription_medication = result;
                $('#deletePrescription_medicationConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Prescription_medication.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePrescription_medicationConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            Prescription_medicationSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.prescription_medications = result;
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
            $scope.prescription_medication = {medication_name: null, man_description: null, woman_description: null, child_description: null, id: null};
        };
    });
