'use strict';

angular.module('windoctorApp')
    .controller('TestEntity3DetailController', function ($scope, $rootScope, $stateParams, entity, TestEntity3, TestEntity2) {
        $scope.testEntity3 = entity;
        $scope.load = function (id) {
            TestEntity3.get({id: id}, function(result) {
                $scope.testEntity3 = result;
            });
        };
        $rootScope.$on('windoctorApp:testEntity3Update', function(event, result) {
            $scope.testEntity3 = result;
        });
    });
