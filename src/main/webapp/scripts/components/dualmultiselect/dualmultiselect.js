/**
 * Created by achraftalbi on 2/20/17.
 */
angular.module("windoctorApp").directive("dualmultiselect", [function() {
    return {
        restrict: 'E',
        scope: {
            options: '='
        },
        controller: function($scope) {
        },
        templateUrl:'scripts/components/dualmultiselect/dualmultiselect.html'
    };
}]);
