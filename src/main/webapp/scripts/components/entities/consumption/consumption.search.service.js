'use strict';

angular.module('windoctorApp')
    .factory('ConsumptionSearch', function ($resource) {
        return $resource('api/_search/consumptions/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
