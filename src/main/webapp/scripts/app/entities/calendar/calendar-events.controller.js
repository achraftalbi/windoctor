'use strict';

angular.module('windoctorApp')
    .controller('CalendarEventsController', function ($scope, Event, EventSearch) {
        $scope.events = [];
        $scope.loadAll = function() {
            Event.query(function(result) {
               $scope.events = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Event.get({id: id}, function(result) {
                $scope.event = result;
                $('#deleteEventConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Event.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEventConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            EventSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.events = result;
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
            $scope.event = {event_date: null, description: null, id: null};
        };
    });
