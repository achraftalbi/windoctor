'use strict';

angular.module('windoctorApp')
    .factory('CategoryAct', function ($resource, DateUtils) {
        return $resource('api/categoryActs/:id', {}, {
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
