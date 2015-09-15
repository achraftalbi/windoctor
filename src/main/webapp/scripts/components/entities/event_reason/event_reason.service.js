'use strict';

angular.module('windoctorApp')
    .factory('Event_reason', function ($resource, DateUtils) {
        return $resource('api/event_reasons/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
