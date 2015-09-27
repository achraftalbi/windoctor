'use strict';

angular.module('windoctorApp').controller('StructureDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Structure', 'Patient',
        function($scope, $stateParams, $modalInstance, entity, Structure, Patient) {

        $scope.structure = entity;
        $scope.patients = Patient.query();
        $scope.load = function(id) {
            Structure.get({id : id}, function(result) {
                $scope.structure = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:structureUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.structure.id != null) {
                Structure.update($scope.structure, onSaveFinished);
            } else {
                Structure.save($scope.structure, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
