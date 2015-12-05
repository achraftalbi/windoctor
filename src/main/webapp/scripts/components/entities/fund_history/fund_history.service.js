'use strict';

angular.module('windoctorApp')
    .factory('Fund_history', function ($resource, DateUtils) {
        return $resource('api/fund_historys/:id', {}, {
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
