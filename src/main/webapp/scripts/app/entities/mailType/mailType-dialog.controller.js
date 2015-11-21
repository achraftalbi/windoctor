'use strict';

angular.module('windoctorApp').controller('MailTypeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'MailType',
        function($scope, $stateParams, $modalInstance, entity, MailType) {

        $scope.mailType = entity;
        $scope.load = function(id) {
            MailType.get({id : id}, function(result) {
                $scope.mailType = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:mailTypeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.mailType.id != null) {
                MailType.update($scope.mailType, onSaveFinished);
            } else {
                MailType.save($scope.mailType, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
