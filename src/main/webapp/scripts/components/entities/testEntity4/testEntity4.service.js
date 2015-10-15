'use strict';

angular.module('windoctorApp')
    .factory('TestEntity4', function ($resource, DateUtils) {
        return $resource('api/testEntity4s/:id', {}, {
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
