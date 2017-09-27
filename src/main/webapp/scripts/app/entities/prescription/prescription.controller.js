'use strict';

angular.module('windoctorApp')
    .controller('PrescriptionController', function ($scope, Prescription, PrescriptionSearch, ParseLinks) {
        $scope.prescriptions = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Prescription.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.prescriptions = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Prescription.get({id: id}, function(result) {
                $scope.prescription = result;
                $('#deletePrescriptionConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Prescription.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePrescriptionConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            PrescriptionSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.prescriptions = result;
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
            $scope.prescription = {description: null, archived: null, medication_persist: null, creation_date: null, update_date: null, id: null};
        };
    });
