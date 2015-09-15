'use strict';

angular.module('windoctorApp')
    .factory('StatusSearch', function ($resource) {
        return $resource('api/_search/statuss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
