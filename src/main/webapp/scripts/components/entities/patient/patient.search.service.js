'use strict';

angular.module('windoctorApp')
    .factory('PatientSearch', function ($resource) {
        return $resource('api/_search/patients/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
