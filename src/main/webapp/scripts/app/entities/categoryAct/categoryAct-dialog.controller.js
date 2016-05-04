'use strict';

angular.module('windoctorApp').controller('CategoryActDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'CategoryAct', 'Structure',
        function($scope, $stateParams, $modalInstance, entity, CategoryAct, Structure) {

        $scope.categoryAct = entity;
        $scope.structures = Structure.query();
        $scope.load = function(id) {
            CategoryAct.get({id : id}, function(result) {
                $scope.categoryAct = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:categoryActUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.categoryAct.id != null) {
                CategoryAct.update($scope.categoryAct, onSaveFinished);
            } else {
                CategoryAct.save($scope.categoryAct, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
