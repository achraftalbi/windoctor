'use strict';

angular.module('windoctorApp')
    .factory('TestEntity3', function ($resource, DateUtils) {
        return $resource('api/testEntity3s/:id', {}, {
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
