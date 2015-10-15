'use strict';

angular.module('windoctorApp')
    .factory('TestEntity4Search', function ($resource) {
        return $resource('api/_search/testEntity4s/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
