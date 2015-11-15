'use strict';

angular.module('windoctorApp')
    .factory('DashboardSearch', function ($resource) {
        return $resource('api/_search/dashboards/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
