'use strict';

angular.module('windoctorApp')
    .factory('EventSearch', function ($resource) {
        return $resource('api/_search/events/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    })
    .factory('EventSearchBlock', function ($resource) {
        return $resource('api/_search/eventsBlock/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
