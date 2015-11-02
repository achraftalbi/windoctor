'use strict';

angular.module('windoctorApp')
    .controller('AttachmentDetailController', function ($scope, $rootScope, $stateParams, entity, Attachment, Treatment) {
        $scope.attachment = entity;
        $scope.load = function (id) {
            Attachment.get({id: id}, function(result) {
                $scope.attachment = result;
            });
        };
        $rootScope.$on('windoctorApp:attachmentUpdate', function(event, result) {
            $scope.attachment = result;
        });
    });
