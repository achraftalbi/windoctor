'use strict';

angular.module('windoctorApp')
    .factory('Type_structureSearch', function ($resource) {
        return $resource('api/_search/type_structures/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
