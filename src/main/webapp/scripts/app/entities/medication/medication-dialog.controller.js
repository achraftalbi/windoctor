'use strict';

angular.module('windoctorApp').controller('MedicationDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Medication',
        function($scope, $stateParams, $modalInstance, entity, Medication) {

        $scope.medication = entity;
        $scope.load = function(id) {
            Medication.get({id : id}, function(result) {
                $scope.medication = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:medicationUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.medication.id != null) {
                Medication.update($scope.medication, onSaveFinished);
            } else {
                Medication.save($scope.medication, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
