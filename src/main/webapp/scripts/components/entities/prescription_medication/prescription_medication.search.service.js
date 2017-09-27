'use strict';

angular.module('windoctorApp')
    .factory('Prescription_medicationSearch', function ($resource) {
        return $resource('api/_search/prescription_medications/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
