'use strict';

angular.module('windoctorApp')
    .factory('Plan', function ($resource, DateUtils) {
        return $resource('api/plans/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.creation_date = DateUtils.convertDateTimeFromServer(data.creation_date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
