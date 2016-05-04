'use strict';

angular.module('windoctorApp')
    .factory('CategoryChargeSearch', function ($resource) {
        return $resource('api/_search/categoryCharges/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
