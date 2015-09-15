'use strict';

angular.module('windoctorApp')
    .controller('EventDetailController', function ($scope, $rootScope, $stateParams, entity, Event, Status, Event_reason) {
        $scope.event = entity;
        $scope.load = function (id) {
            Event.get({id: id}, function(result) {
                $scope.event = result;
            });
        };
        $rootScope.$on('windoctorApp:eventUpdate', function(event, result) {
            $scope.event = result;
        });
    });
