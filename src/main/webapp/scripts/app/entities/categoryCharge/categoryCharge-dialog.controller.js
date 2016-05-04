'use strict';

angular.module('windoctorApp').controller('CategoryChargeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'CategoryCharge', 'Structure',
        function($scope, $stateParams, $modalInstance, entity, CategoryCharge, Structure) {

        $scope.categoryCharge = entity;
        $scope.structures = Structure.query();
        $scope.load = function(id) {
            CategoryCharge.get({id : id}, function(result) {
                $scope.categoryCharge = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:categoryChargeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.categoryCharge.id != null) {
                CategoryCharge.update($scope.categoryCharge, onSaveFinished);
            } else {
                CategoryCharge.save($scope.categoryCharge, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
