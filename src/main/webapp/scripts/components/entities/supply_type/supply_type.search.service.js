'use strict';

angular.module('windoctorApp')
    .factory('Supply_typeSearch', function ($resource) {
        return $resource('api/_search/supply_types/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
