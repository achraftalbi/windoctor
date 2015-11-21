'use strict';

angular.module('windoctorApp')
    .controller('MailTypeDetailController', function ($scope, $rootScope, $stateParams, entity, MailType) {
        $scope.mailType = entity;
        $scope.load = function (id) {
            MailType.get({id: id}, function(result) {
                $scope.mailType = result;
            });
        };
        var unsubscribe = $rootScope.$on('windoctorApp:mailTypeUpdate', function(event, result) {
            $scope.mailType = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
