 'use strict';

angular.module('windoctorApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-windoctorApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-windoctorApp-params')});
                }
                return response;
            },
        };
    });