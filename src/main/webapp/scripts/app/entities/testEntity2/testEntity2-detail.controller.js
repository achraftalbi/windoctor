'use strict';

angular.module('windoctorApp')
    .controller('TestEntity2DetailController', function ($scope, $rootScope, $stateParams, entity, TestEntity2, TestEntity3) {
        $scope.testEntity2 = entity;
        $scope.load = function (id) {
            TestEntity2.get({id: id}, function(result) {
                $scope.testEntity2 = result;
            });
        };
        $rootScope.$on('windoctorApp:testEntity2Update', function(event, result) {
            $scope.testEntity2 = result;
        });
    });
