'use strict';

angular.module('windoctorApp')
    .factory('TestEntity3Search', function ($resource) {
        return $resource('api/_search/testEntity3s/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
