'use strict';

angular.module('windoctorApp').controller('MailSettingDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'MailSetting', 'Structure', 'MailType',
        function($scope, $stateParams, $modalInstance, entity, MailSetting, Structure, MailType) {

        $scope.mailSetting = entity;
        $scope.structures = Structure.query();
        $scope.mailtypes = MailType.query();
        $scope.load = function(id) {
            MailSetting.get({id : id}, function(result) {
                $scope.mailSetting = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:mailSettingUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.mailSetting.id != null) {
                MailSetting.update($scope.mailSetting, onSaveFinished);
            } else {
                MailSetting.save($scope.mailSetting, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
