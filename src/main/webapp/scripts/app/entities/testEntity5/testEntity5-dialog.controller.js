'use strict';

angular.module('windoctorApp').controller('TestEntity5DialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'TestEntity5', 'TestEntity4',
        function($scope, $stateParams, $modalInstance, $q, entity, TestEntity5, TestEntity4) {

        $scope.testEntity5 = entity;
        $scope.testentity4s = TestEntity4.query({filter: 'testentity5-is-null'});
        $q.all([$scope.testEntity5.$promise, $scope.testentity4s.$promise]).then(function() {
            if (!$scope.testEntity5.testEntity4.id) {
                return $q.reject();
            }
            return TestEntity4.get({id : $scope.testEntity5.testEntity4.id}).$promise;
        }).then(function(testEntity4) {
            $scope.testentity4s.push(testEntity4);
        });
        $scope.load = function(id) {
            TestEntity5.get({id : id}, function(result) {
                $scope.testEntity5 = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:testEntity5Update', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.testEntity5.id != null) {
                TestEntity5.update($scope.testEntity5, onSaveFinished);
            } else {
                TestEntity5.save($scope.testEntity5, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
