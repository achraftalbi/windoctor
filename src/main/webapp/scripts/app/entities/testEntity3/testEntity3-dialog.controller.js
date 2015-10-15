'use strict';

angular.module('windoctorApp').controller('TestEntity3DialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'TestEntity3', 'TestEntity2',
        function($scope, $stateParams, $modalInstance, entity, TestEntity3, TestEntity2) {

        $scope.testEntity3 = entity;
        $scope.testentity2s = TestEntity2.query();
        $scope.load = function(id) {
            TestEntity3.get({id : id}, function(result) {
                $scope.testEntity3 = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:testEntity3Update', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.testEntity3.id != null) {
                TestEntity3.update($scope.testEntity3, onSaveFinished);
            } else {
                TestEntity3.save($scope.testEntity3, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
