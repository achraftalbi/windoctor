'use strict';

angular.module('windoctorApp')
    .factory('FundSearch', function ($resource) {
        return $resource('api/_search/funds/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
