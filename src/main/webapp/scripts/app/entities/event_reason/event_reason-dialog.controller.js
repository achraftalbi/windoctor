'use strict';

angular.module('windoctorApp').controller('Event_reasonDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Event_reason', 'CategoryAct',
        function($scope, $stateParams, $modalInstance, entity, Event_reason, CategoryAct) {

        $scope.event_reason = entity;
        $scope.categoryActs = CategoryAct.query();
        $scope.load = function(id) {
            Event_reason.get({id : id}, function(result) {
                $scope.event_reason = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:event_reasonUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.event_reason.id != null) {
                Event_reason.update($scope.event_reason, onSaveFinished);
            } else {
                Event_reason.save($scope.event_reason, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
