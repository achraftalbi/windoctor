'use strict';

angular.module('windoctorApp')
    .controller('Event_reasonDetailController', function ($scope, $rootScope, $stateParams, entity, Event_reason, Event) {
        $scope.event_reason = entity;
        $scope.load = function (id) {
            Event_reason.get({id: id}, function(result) {
                $scope.event_reason = result;
            });
        };
        $rootScope.$on('windoctorApp:event_reasonUpdate', function(event, result) {
            $scope.event_reason = result;
        });
    });
