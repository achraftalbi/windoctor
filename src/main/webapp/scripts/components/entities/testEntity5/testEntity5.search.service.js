'use strict';

angular.module('windoctorApp')
    .factory('TestEntity5Search', function ($resource) {
        return $resource('api/_search/testEntity5s/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
