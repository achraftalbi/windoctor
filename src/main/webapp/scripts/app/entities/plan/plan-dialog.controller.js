'use strict';

angular.module('windoctorApp').controller('PlanDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Plan', 'Structure',
        function($scope, $stateParams, $modalInstance, entity, Plan, Structure) {

        $scope.plan = entity;
        $scope.structures = Structure.query();
        $scope.load = function(id) {
            Plan.get({id : id}, function(result) {
                $scope.plan = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:planUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.plan.id != null) {
                Plan.update($scope.plan, onSaveFinished);
            } else {
                Plan.save($scope.plan, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
