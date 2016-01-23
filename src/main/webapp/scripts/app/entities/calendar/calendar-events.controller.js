'use strict';

angular.module('windoctorApp')
    .controller('CalendarEventsController', function ($scope, $stateParams,$modalInstance, Event, EventSearch, ParseLinks,$filter,Principal) {
        $scope.events = [];
        $scope.page = 1;
        $scope.selectedDate= null;
        $scope.account= null;
        $scope.userCanAddRequest= false;
        $scope.loadAll = function () {
                console.info('first $stateParams.selectedDate'+ $stateParams.selectedDate);
                Event.query({selectedDate:$stateParams.selectedDate+'', page: $scope.page, per_page: 5}, function (result, headers) {
                    $scope.selectedDate = $filter('date')($stateParams.selectedDate, 'MMM dd yyyy');
                    $scope.links = ParseLinks.parse(headers('link'));
                    $scope.events = result;
                });
        };
        Principal.identity(true).then(function(account) {
            $scope.account = account;
            console.log('$scope.account.currentUserPatient '+$scope.account.currentUserPatient);
            console.log('$scope.account.maxEventsReached '+$scope.account.maxEventsReached);
            $scope.userCanAddRequest = $scope.account.currentUserPatient && !$scope.account.maxEventsReached;
        });
        $("a").tooltip();
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

        //End Patient pages treatement
        //Annul an appointment
        $scope.annul = function (event) {
            // Annul an appointment
            event.eventStatus.id = 2;
            $scope.save(event);
        };
        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:eventUpdate', result);
            //$modalInstance.close(result);
        };

        $scope.save = function (event) {
            if (event.id != null) {
                Event.update(event, onSaveFinished);
            } else {
                Event.save(event, onSaveFinished);
            }
        };

    });
