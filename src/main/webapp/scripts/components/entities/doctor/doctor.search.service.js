'use strict';

angular.module('windoctorApp')
    .factory('DoctorSearch', function ($resource) {
        return $resource('api/_search/doctors/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
