'use strict';

angular.module('windoctorApp').controller('EntryDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Entry', 'Charge', 'Fund',
        function($scope, $stateParams, $modalInstance, entity, Entry, Charge, Fund) {

        $scope.entry = entity;
        $scope.charges = Charge.query();
        $scope.funds = Fund.query();
        $scope.load = function(id) {
            Entry.get({id : id}, function(result) {
                $scope.entry = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:entryUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.entry.id != null) {
                Entry.update($scope.entry, onSaveFinished);
            } else {
                Entry.save($scope.entry, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
