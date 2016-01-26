'use strict';

angular.module('windoctorApp')
    .factory('Test6Entity', function ($resource, DateUtils) {
        return $resource('api/test6Entitys/:id', {}, {
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
