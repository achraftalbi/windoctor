'use strict';

angular.module('windoctorApp')
    .controller('EntryDetailController', function ($scope, $rootScope, $stateParams, entity, Entry, Charge, Fund) {
        $scope.entry = entity;
        $scope.load = function (id) {
            Entry.get({id: id}, function(result) {
                $scope.entry = result;
            });
        };
        $rootScope.$on('windoctorApp:entryUpdate', function(event, result) {
            $scope.entry = result;
        });
    });
