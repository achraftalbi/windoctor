'use strict';

angular.module('windoctorApp').controller('Test6EntityDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Test6Entity',
        function($scope, $stateParams, $modalInstance, entity, Test6Entity) {

        $scope.test6Entity = entity;
        $scope.load = function(id) {
            Test6Entity.get({id : id}, function(result) {
                $scope.test6Entity = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:test6EntityUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.test6Entity.id != null) {
                Test6Entity.update($scope.test6Entity, onSaveFinished);
            } else {
                Test6Entity.save($scope.test6Entity, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
