'use strict';

angular.module('windoctorApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
