'use strict';

angular.module('windoctorApp').controller('MailSettingDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'MailSetting', 'Structure',
        function($scope, $stateParams, $modalInstance, $q, entity, MailSetting, Structure) {

        $scope.mailSetting = entity;
        $scope.structures = Structure.query({filter: 'mailsetting-is-null'});
        $q.all([$scope.mailSetting.$promise, $scope.structures.$promise]).then(function() {
            if (!$scope.mailSetting.structure.id) {
                return $q.reject();
            }
            return Structure.get({id : $scope.mailSetting.structure.id}).$promise;
        }).then(function(structure) {
            $scope.structures.push(structure);
        });
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
