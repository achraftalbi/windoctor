'use strict';

angular.module('windoctorApp').controller('Type_structureDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Type_structure', 'Structure',
        function($scope, $stateParams, $modalInstance, entity, Type_structure, Structure) {

        $scope.type_structure = entity;
        $scope.structures = Structure.query();
        $scope.load = function(id) {
            Type_structure.get({id : id}, function(result) {
                $scope.type_structure = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:type_structureUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.type_structure.id != null) {
                Type_structure.update($scope.type_structure, onSaveFinished);
            } else {
                Type_structure.save($scope.type_structure, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
