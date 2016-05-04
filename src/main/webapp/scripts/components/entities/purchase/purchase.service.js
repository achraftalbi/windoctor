'use strict';

angular.module('windoctorApp')
    .factory('Purchase', function ($resource, DateUtils) {
        return $resource('api/purchases/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.creation_date = DateUtils.convertDateTimeFromServer(data.creation_date);
                    data.relative_date = DateUtils.convertDateTimeFromServer(data.relative_date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
