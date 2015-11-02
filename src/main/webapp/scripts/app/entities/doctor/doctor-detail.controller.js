'use strict';

angular.module('windoctorApp')
    .controller('DoctorDetailController', function ($scope, $rootScope, $stateParams, entity, Doctor, Structure) {
        $scope.doctor = entity;
        $scope.load = function (id) {
            Doctor.get({id: id}, function(result) {
                $scope.doctor = result;
            });
        };
        $rootScope.$on('windoctorApp:doctorUpdate', function(event, result) {
            $scope.doctor = result;
        });
    });
