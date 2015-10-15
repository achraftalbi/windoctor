'use strict';

angular.module('windoctorApp')
    .controller('TestEntity5DetailController', function ($scope, $rootScope, $stateParams, entity, TestEntity5, TestEntity4) {
        $scope.testEntity5 = entity;
        $scope.load = function (id) {
            TestEntity5.get({id: id}, function(result) {
                $scope.testEntity5 = result;
            });
        };
        $rootScope.$on('windoctorApp:testEntity5Update', function(event, result) {
            $scope.testEntity5 = result;
        });
    });
