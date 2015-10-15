'use strict';

angular.module('windoctorApp')
    .controller('TestEntity4DetailController', function ($scope, $rootScope, $stateParams, entity, TestEntity4, TestEntity5) {
        $scope.testEntity4 = entity;
        $scope.load = function (id) {
            TestEntity4.get({id: id}, function(result) {
                $scope.testEntity4 = result;
            });
        };
        $rootScope.$on('windoctorApp:testEntity4Update', function(event, result) {
            $scope.testEntity4 = result;
        });
    });
