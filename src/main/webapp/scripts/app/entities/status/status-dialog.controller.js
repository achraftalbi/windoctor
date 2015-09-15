'use strict';

angular.module('windoctorApp').controller('StatusDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Status', 'Event',
        function($scope, $stateParams, $modalInstance, entity, Status, Event) {

        $scope.status = entity;
        $scope.events = Event.query();
        $scope.load = function(id) {
            Status.get({id : id}, function(result) {
                $scope.status = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:statusUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.status.id != null) {
                Status.update($scope.status, onSaveFinished);
            } else {
                Status.save($scope.status, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
