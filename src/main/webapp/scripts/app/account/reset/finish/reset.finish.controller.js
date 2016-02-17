'use strict';

angular.module('windoctorApp')
    .controller('ResetFinishController', function ($scope, $stateParams, $timeout, Auth) {

        $scope.keyMissing = $stateParams.key === undefined;
        $scope.doNotMatch = null;

        $scope.resetAccount = {};
        $timeout(function (){angular.element('[ng-model="resetAccount.password"]').focus();});

        $scope.finishReset = function() {
            console.log('reset value 1');
            if ($scope.resetAccount.password !== $scope.confirmPassword) {
                $scope.doNotMatch = 'ERROR';
                console.log('reset value 2');
            } else {
                Auth.resetPasswordFinish({key: $stateParams.key, newPassword: $scope.resetAccount.password}).then(function () {
                    $scope.success = 'OK';
                    console.log('reset value 3');
                }).catch(function (response) {
                    $scope.success = null;
                    $scope.error = 'ERROR';
                    console.log('reset value 4');
                });
            }

        };
    });
