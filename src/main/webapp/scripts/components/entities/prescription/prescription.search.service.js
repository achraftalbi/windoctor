'use strict';

angular.module('windoctorApp')
    .factory('PrescriptionSearch', function ($resource) {
        return $resource('api/_search/prescriptions/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
