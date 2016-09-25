'use strict';

angular.module('windoctorApp')
    .factory('PlanSearch', function ($resource) {
        return $resource('api/_search/plans/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
