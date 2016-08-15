'use strict';

angular.module('windoctorApp')
    .controller('Type_structureDetailController', function ($scope, $rootScope, $stateParams, entity, Type_structure, Structure) {
        $scope.type_structure = entity;
        $scope.load = function (id) {
            Type_structure.get({id: id}, function(result) {
                $scope.type_structure = result;
            });
        };
        $rootScope.$on('windoctorApp:type_structureUpdate', function(event, result) {
            $scope.type_structure = result;
        });
    });
