'use strict';

angular.module('windoctorApp').controller('FundDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Fund', 'Fund_history','Supply_type',
        function($scope, $stateParams, $modalInstance, entity, Fund, Fund_history,Supply_type) {

        $scope.fund = entity;
        $scope.fund_historys = Fund_history.query();
        $scope.supply_types = Supply_type.query();
            $scope.supply_type;
            $scope.fundDTO;
        $scope.load = function(id) {
            Fund.get({id : id}, function(result) {
                $scope.fund = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:fundUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            $scope.fundDTO = {fund:$scope.fund,supply_type:$scope.supply_type};
            if ($scope.fund.id != null) {
                Fund.update($scope.fundDTO, onSaveFinished);
            } else {
                Fund.save($scope.fundDTO, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
