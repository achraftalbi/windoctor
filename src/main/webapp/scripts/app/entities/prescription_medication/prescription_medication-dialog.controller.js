'use strict';

angular.module('windoctorApp').controller('Prescription_medicationDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Prescription_medication', 'Prescription', 'Medication',
        function($scope, $stateParams, $modalInstance, entity, Prescription_medication, Prescription, Medication) {

        $scope.prescription_medication = entity;
        $scope.prescriptions = Prescription.query();
        $scope.medications = Medication.query();
        $scope.load = function(id) {
            Prescription_medication.get({id : id}, function(result) {
                $scope.prescription_medication = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:prescription_medicationUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.prescription_medication.id != null) {
                Prescription_medication.update($scope.prescription_medication, onSaveFinished);
            } else {
                Prescription_medication.save($scope.prescription_medication, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
