'use strict';

angular.module('windoctorApp')
    .controller('Prescription_medicationDetailController', function ($scope, $rootScope, $stateParams, entity, Prescription_medication, Prescription, Medication) {
        $scope.prescription_medication = entity;
        $scope.load = function (id) {
            Prescription_medication.get({id: id}, function(result) {
                $scope.prescription_medication = result;
            });
        };
        $rootScope.$on('windoctorApp:prescription_medicationUpdate', function(event, result) {
            $scope.prescription_medication = result;
        });
    });
