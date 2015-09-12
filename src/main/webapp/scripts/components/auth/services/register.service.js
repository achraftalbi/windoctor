'use strict';

angular.module('windoctorApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


