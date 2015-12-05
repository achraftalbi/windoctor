'use strict';

angular.module('windoctorApp').controller('Supply_typeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Supply_type',
        function($scope, $stateParams, $modalInstance, entity, Supply_type) {

        $scope.supply_type = entity;
        $scope.load = function(id) {
            Supply_type.get({id : id}, function(result) {
                $scope.supply_type = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:supply_typeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.supply_type.id != null) {
                Supply_type.update($scope.supply_type, onSaveFinished);
            } else {
                Supply_type.save($scope.supply_type, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
