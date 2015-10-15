'use strict';

angular.module('windoctorApp').controller('TestEntity4DialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'TestEntity4', 'TestEntity5',
        function($scope, $stateParams, $modalInstance, entity, TestEntity4, TestEntity5) {

        $scope.testEntity4 = entity;
        $scope.testentity5s = TestEntity5.query();
        $scope.load = function(id) {
            TestEntity4.get({id : id}, function(result) {
                $scope.testEntity4 = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:testEntity4Update', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.testEntity4.id != null) {
                TestEntity4.update($scope.testEntity4, onSaveFinished);
            } else {
                TestEntity4.save($scope.testEntity4, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
