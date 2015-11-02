'use strict';

angular.module('windoctorApp')
    .controller('CalendarEventsController', function ($scope, $stateParams,$modalInstance, Event, EventSearch, ParseLinks,$filter) {
        $scope.events = [];
        $scope.page = 1;
        $scope.selectedDate= null;
        $scope.loadAll = function () {
                Event.query({selectedDate:$stateParams.selectedDate, page: $scope.page, per_page: 5}, function (result, headers) {
                    $scope.selectedDate = $filter('date')($stateParams.selectedDate, 'MMM dd yyyy');
                    $scope.links = ParseLinks.parse(headers('link'));
                    $scope.events = result;
                });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Event.get({id: id}, function (result) {
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
            EventSearch.query({query: $scope.searchQuery}, function (result) {
                $scope.events = result;
            }, function (response) {
                if (response.status === 404) {
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

        $scope.cancelDelete = function () {
            $scope.clear();
        };

        $scope.cancelEventRows = function () {
            $modalInstance.dismiss('cancel');
        };
    });
