'use strict';

angular.module('windoctorApp').controller('CategoryDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Category', 'Product',
        function($scope, $stateParams, $modalInstance, entity, Category, Product) {

        $scope.category = entity;
        $scope.products = Product.query();
        $scope.load = function(id) {
            Category.get({id : id}, function(result) {
                $scope.category = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:categoryUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.category.id != null) {
                Category.update($scope.category, onSaveFinished);
            } else {
                Category.save($scope.category, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
