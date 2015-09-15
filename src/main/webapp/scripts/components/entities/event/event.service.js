'use strict';

angular.module('windoctorApp')
    .factory('Event', function ($resource, DateUtils) {
        return $resource('api/events/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.event_date = DateUtils.convertDateTimeFromServer(data.event_date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
