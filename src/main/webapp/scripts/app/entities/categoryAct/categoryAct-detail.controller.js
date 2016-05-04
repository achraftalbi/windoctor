'use strict';

angular.module('windoctorApp')
    .controller('CategoryActDetailController', function ($scope, $rootScope, $stateParams, entity, CategoryAct, Structure) {
        $scope.categoryAct = entity;
        $scope.load = function (id) {
            CategoryAct.get({id: id}, function(result) {
                $scope.categoryAct = result;
            });
        };
        $rootScope.$on('windoctorApp:categoryActUpdate', function(event, result) {
            $scope.categoryAct = result;
        });
    });
