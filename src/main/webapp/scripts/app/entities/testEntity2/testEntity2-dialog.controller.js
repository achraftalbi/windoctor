'use strict';

angular.module('windoctorApp').controller('TestEntity2DialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'TestEntity2', 'TestEntity3',
        function($scope, $stateParams, $modalInstance, entity, TestEntity2, TestEntity3) {

        $scope.testEntity2 = entity;
        $scope.testentity3s = TestEntity3.query();
        $scope.load = function(id) {
            TestEntity2.get({id : id}, function(result) {
                $scope.testEntity2 = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:testEntity2Update', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.testEntity2.id != null) {
                TestEntity2.update($scope.testEntity2, onSaveFinished);
            } else {
                TestEntity2.save($scope.testEntity2, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
