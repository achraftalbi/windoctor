'use strict';

angular.module('windoctorApp')
    .controller('EntityTest1DetailController', function ($scope, $rootScope, $stateParams, entity, EntityTest1) {
        $scope.entityTest1 = entity;
        $scope.load = function (id) {
            EntityTest1.get({id: id}, function(result) {
                $scope.entityTest1 = result;
            });
        };
        $rootScope.$on('windoctorApp:entityTest1Update', function(event, result) {
            $scope.entityTest1 = result;
        });
    });
