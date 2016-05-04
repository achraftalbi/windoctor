'use strict';

angular.module('windoctorApp').controller('PurchaseDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Purchase', 'Product', 'Fund',
        function($scope, $stateParams, $modalInstance, entity, Purchase, Product, Fund) {

        $scope.purchase = entity;
        $scope.products = Product.query();
        $scope.funds = Fund.query();
        $scope.load = function(id) {
            Purchase.get({id : id}, function(result) {
                $scope.purchase = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:purchaseUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.purchase.id != null) {
                Purchase.update($scope.purchase, onSaveFinished);
            } else {
                Purchase.save($scope.purchase, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
