'use strict';

angular.module('windoctorApp')
    .factory('EntityTest1Search', function ($resource) {
        return $resource('api/_search/entityTest1s/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
