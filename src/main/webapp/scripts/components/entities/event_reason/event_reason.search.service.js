'use strict';

angular.module('windoctorApp')
    .factory('Event_reasonSearch', function ($resource) {
        return $resource('api/_search/event_reasons/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
