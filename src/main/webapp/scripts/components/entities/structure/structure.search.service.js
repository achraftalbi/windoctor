'use strict';

angular.module('windoctorApp')
    .factory('StructureSearch', function ($resource) {
        return $resource('api/_search/structures/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
