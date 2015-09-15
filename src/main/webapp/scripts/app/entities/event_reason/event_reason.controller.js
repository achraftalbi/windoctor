'use strict';

angular.module('windoctorApp')
    .controller('Event_reasonController', function ($scope, Event_reason, Event_reasonSearch) {
        $scope.event_reasons = [];
        $scope.loadAll = function() {
            Event_reason.query(function(result) {
               $scope.event_reasons = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Event_reason.get({id: id}, function(result) {
                $scope.event_reason = result;
                $('#deleteEvent_reasonConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Event_reason.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEvent_reasonConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            Event_reasonSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.event_reasons = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.event_reason = {description: null, id: null};
        };
    });
