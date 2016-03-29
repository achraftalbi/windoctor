'use strict';

angular.module('windoctorApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal, ENV) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
            $scope.structureName = Principal.structureName;
        });

        $scope.$state = $state;
        $scope.inProduction = ENV === 'prod';

        $scope.logout = function () {
            Auth.logout();
            $state.go('home');
        };

    });
