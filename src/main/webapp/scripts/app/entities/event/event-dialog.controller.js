'use strict';

angular.module('windoctorApp').controller('EventDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Event', 'Status', 'Event_reason',
        function($scope, $stateParams, $modalInstance, entity, Event, Status, Event_reason) {

        $scope.event = entity;
        $scope.statuss = Status.query();
        $scope.event_reasons = Event_reason.query();
        $scope.load = function(id) {
            Event.get({id : id}, function(result) {
                $scope.event = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:eventUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.event.id != null) {
                Event.update($scope.event, onSaveFinished);
            } else {
                Event.save($scope.event, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
