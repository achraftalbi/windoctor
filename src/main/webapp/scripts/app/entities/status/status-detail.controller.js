'use strict';

angular.module('windoctorApp')
    .controller('StatusDetailController', function ($scope, $rootScope, $stateParams, entity, Status, Event) {
        $scope.status = entity;
        $scope.load = function (id) {
            Status.get({id: id}, function(result) {
                $scope.status = result;
            });
        };
        $rootScope.$on('windoctorApp:statusUpdate', function(event, result) {
            $scope.status = result;
        });
    });
