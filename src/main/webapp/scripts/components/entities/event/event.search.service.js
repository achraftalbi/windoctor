'use strict';

angular.module('windoctorApp')
    .factory('EventSearch', function ($resource) {
        return $resource('api/_search/events/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
