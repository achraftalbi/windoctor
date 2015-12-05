'use strict';

angular.module('windoctorApp').controller('Fund_historyDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Fund_history', 'Fund', 'Treatment', 'Product',
        function($scope, $stateParams, $modalInstance, entity, Fund_history, Fund, Treatment, Product) {

        $scope.fund_history = entity;
        $scope.funds = Fund.query();
        $scope.treatments = Treatment.query();
        $scope.products = Product.query();
        $scope.load = function(id) {
            Fund_history.get({id : id}, function(result) {
                $scope.fund_history = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:fund_historyUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.fund_history.id != null) {
                Fund_history.update($scope.fund_history, onSaveFinished);
            } else {
                Fund_history.save($scope.fund_history, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
