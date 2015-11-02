'use strict';

angular.module('windoctorApp').controller('TreatmentDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Treatment', 'Attachment', 'Event_reason',
        function($scope, $stateParams, $modalInstance, entity, Treatment, Attachment, Event_reason) {

        $scope.treatment = entity;
        $scope.attachments = Attachment.query();
        $scope.event_reasons = Event_reason.query();
        $scope.load = function(id) {
            Treatment.get({id : id}, function(result) {
                $scope.treatment = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:treatmentUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.treatment.id != null) {
                Treatment.update($scope.treatment, onSaveFinished);
            } else {
                Treatment.save($scope.treatment, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
