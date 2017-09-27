'use strict';

angular.module('windoctorApp')
    .factory('MedicationSearch', function ($resource) {
        return $resource('api/_search/medications/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
