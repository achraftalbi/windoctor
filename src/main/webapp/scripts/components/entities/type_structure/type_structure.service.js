'use strict';

angular.module('windoctorApp')
    .factory('Type_structure', function ($resource, DateUtils) {
        return $resource('api/type_structures/:id', {}, {
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
