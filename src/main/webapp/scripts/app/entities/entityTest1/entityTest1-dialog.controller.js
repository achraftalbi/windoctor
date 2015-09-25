'use strict';

angular.module('windoctorApp').controller('EntityTest1DialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'EntityTest1',
        function($scope, $stateParams, $modalInstance, entity, EntityTest1) {

        $scope.entityTest1 = entity;
        $scope.load = function(id) {
            EntityTest1.get({id : id}, function(result) {
                $scope.entityTest1 = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:entityTest1Update', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.entityTest1.id != null) {
                EntityTest1.update($scope.entityTest1, onSaveFinished);
            } else {
                EntityTest1.save($scope.entityTest1, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
