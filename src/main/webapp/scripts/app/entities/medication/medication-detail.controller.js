'use strict';

angular.module('windoctorApp')
    .controller('MedicationDetailController', function ($scope, $rootScope, $stateParams, entity, Medication) {
        $scope.medication = entity;
        $scope.load = function (id) {
            Medication.get({id: id}, function(result) {
                $scope.medication = result;
            });
        };
        $rootScope.$on('windoctorApp:medicationUpdate', function(event, result) {
            $scope.medication = result;
        });
    });
