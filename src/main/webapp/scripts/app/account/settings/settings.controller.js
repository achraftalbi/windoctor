'use strict';

angular.module('windoctorApp')
    .controller('SettingsController', function ($scope, Principal, Auth, Language, $translate,moment) {
        $scope.success = null;
        $scope.error = null;
        Principal.identity(true).then(function(account) {
            $scope.settingsAccount = account;
        });

        $scope.save = function () {
            Auth.updateAccount($scope.settingsAccount).then(function() {
                $scope.error = null;
                $scope.success = 'OK';
                Principal.identity().then(function(account) {
                    $scope.settingsAccount = account;
                });
                Language.getCurrent().then(function(current) {
                    if ($scope.settingsAccount.langKey !== current) {
                        $translate.use($scope.settingsAccount.langKey);
                        moment.locale($scope.settingsAccount.langKey);
                    }
                });
            }).catch(function() {
                $scope.success = null;
                $scope.error = 'ERROR';
            });
        };
    });
