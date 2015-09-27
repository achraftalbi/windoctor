'use strict';

angular.module('windoctorApp')
    .controller('StructureDetailController', function ($scope, $rootScope, $stateParams, entity, Structure, Patient) {
        $scope.structure = entity;
        $scope.load = function (id) {
            Structure.get({id: id}, function(result) {
                $scope.structure = result;
            });
        };
        $rootScope.$on('windoctorApp:structureUpdate', function(event, result) {
            $scope.structure = result;
        });
    });
