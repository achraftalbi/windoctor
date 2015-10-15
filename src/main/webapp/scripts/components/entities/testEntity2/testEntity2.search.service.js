'use strict';

angular.module('windoctorApp')
    .factory('TestEntity2Search', function ($resource) {
        return $resource('api/_search/testEntity2s/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
