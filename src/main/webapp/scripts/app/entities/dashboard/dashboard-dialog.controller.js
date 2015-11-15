'use strict';

angular.module('windoctorApp').controller('DashboardDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Dashboard',
        function($scope, $stateParams, $modalInstance, entity, Dashboard) {

        $scope.dashboard = entity;
        $scope.load = function(id) {
            Dashboard.get({id : id}, function(result) {
                $scope.dashboard = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:dashboardUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.dashboard.id != null) {
                Dashboard.update($scope.dashboard, onSaveFinished);
            } else {
                Dashboard.save($scope.dashboard, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
