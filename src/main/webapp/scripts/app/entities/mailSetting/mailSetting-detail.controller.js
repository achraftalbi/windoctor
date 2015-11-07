'use strict';

angular.module('windoctorApp')
    .controller('MailSettingDetailController', function ($scope, $rootScope, $stateParams, entity, MailSetting, Structure) {
        $scope.mailSetting = entity;
        $scope.load = function (id) {
            MailSetting.get({id: id}, function(result) {
                $scope.mailSetting = result;
            });
        };
        var unsubscribe = $rootScope.$on('windoctorApp:mailSettingUpdate', function(event, result) {
            $scope.mailSetting = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
