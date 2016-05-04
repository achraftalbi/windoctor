'use strict';

angular.module('windoctorApp').controller('ConsumptionDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Consumption', 'Product',
        function($scope, $stateParams, $modalInstance, entity, Consumption, Product) {

        $scope.consumption = entity;
        $scope.products = Product.query();
        $scope.load = function(id) {
            Consumption.get({id : id}, function(result) {
                $scope.consumption = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:consumptionUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.consumption.id != null) {
                Consumption.update($scope.consumption, onSaveFinished);
            } else {
                Consumption.save($scope.consumption, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
