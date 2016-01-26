'use strict';

angular.module('windoctorApp')
    .controller('Test6EntityDetailController', function ($scope, $rootScope, $stateParams, entity, Test6Entity) {
        $scope.test6Entity = entity;
        $scope.load = function (id) {
            Test6Entity.get({id: id}, function(result) {
                $scope.test6Entity = result;
            });
        };
        $rootScope.$on('windoctorApp:test6EntityUpdate', function(event, result) {
            $scope.test6Entity = result;
        });
    });
