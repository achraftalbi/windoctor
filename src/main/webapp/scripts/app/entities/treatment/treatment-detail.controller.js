'use strict';

angular.module('windoctorApp')
    .controller('TreatmentDetailController', function ($scope, $rootScope, $stateParams, entity, Treatment, Attachment, Event_reason) {
        $scope.treatment = entity;
        $scope.load = function (id) {
            Treatment.get({id: id}, function(result) {
                $scope.treatment = result;
            });
        };
        $rootScope.$on('windoctorApp:treatmentUpdate', function(event, result) {
            $scope.treatment = result;
        });
    });
