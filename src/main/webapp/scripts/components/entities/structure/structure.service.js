'use strict';

angular.module('windoctorApp')
    .factory('Structure', function ($resource, DateUtils) {
        return $resource('api/structures/:id', {}, {
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
