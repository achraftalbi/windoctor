'use strict';

angular.module('windoctorApp').controller('PrescriptionDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Prescription',
        function($scope, $stateParams, $modalInstance, entity, Prescription) {

        $scope.prescription = entity;
        $scope.load = function(id) {
            Prescription.get({id : id}, function(result) {
                $scope.prescription = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:prescriptionUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.prescription.id != null) {
                Prescription.update($scope.prescription, onSaveFinished);
            } else {
                Prescription.save($scope.prescription, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
