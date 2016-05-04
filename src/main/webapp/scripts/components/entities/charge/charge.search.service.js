'use strict';

angular.module('windoctorApp')
    .factory('ChargeSearch', function ($resource) {
        return $resource('api/_search/charges/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
