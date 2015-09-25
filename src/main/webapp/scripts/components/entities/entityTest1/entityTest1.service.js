'use strict';

angular.module('windoctorApp')
    .factory('EntityTest1', function ($resource, DateUtils) {
        return $resource('api/entityTest1s/:id', {}, {
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
