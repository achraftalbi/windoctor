'use strict';

angular.module('windoctorApp')
    .factory('Patient', function ($resource, DateUtils) {
        return $resource('api/patients/:id', {}, {
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
