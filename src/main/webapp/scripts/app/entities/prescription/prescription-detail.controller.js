'use strict';

angular.module('windoctorApp')
    .controller('PrescriptionDetailController', function ($scope, $rootScope, $stateParams, entity, Prescription) {
        $scope.prescription = entity;
        $scope.load = function (id) {
            Prescription.get({id: id}, function(result) {
                $scope.prescription = result;
            });
        };
        $rootScope.$on('windoctorApp:prescriptionUpdate', function(event, result) {
            $scope.prescription = result;
        });
    });
