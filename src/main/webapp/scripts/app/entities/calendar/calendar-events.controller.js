'use strict';

angular.module('windoctorApp')
    .controller('CalendarEventsController', function ($scope, $stateParams, $modalInstance, Event, EventSearch, ParseLinks, $filter, Principal,$translate) {
        $scope.events = [];
        $scope.page = 1;
        $scope.selectedDate = null;
        $scope.selectedDateString = null;
        $scope.account = null;
        $scope.userCanAddRequest = false;
        $scope.eventsEmpty = false;
        $scope.selectedDateObject = new Date($stateParams.selectedDate);
        $scope.searchCalled = false;
        console.log('selectedDateString '+$scope.selectedDateString);
        $scope.loadAll = function () {
            console.info('first $stateParams.selectedDate' + $stateParams.selectedDate);
            Event.query({
                selectedDate: $stateParams.selectedDate + '',
                page: $scope.page,
                per_page: 5
            }, function (result, headers) {
                $scope.selectedDate = $filter('date')($stateParams.selectedDate, 'MMM dd yyyy');
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.events = result;
                if($scope.events!==null && $scope.events!== undefined && $scope.events.length===0){
                    $scope.eventsEmpty = true;
                }
                console.log('events eventsEmpty '+$scope.eventsEmpty);
            });
        };
        Principal.identity(true).then(function (account) {
            $scope.account = account;
            console.log('$scope.account.currentUserPatient ' + $scope.account.currentUserPatient);
            console.log('$scope.account.maxEventsReached ' + $scope.account.maxEventsReached);
            $scope.userCanAddRequest = $scope.account.currentUserPatient && !$scope.account.maxEventsReached;
        });

        $("a").tooltip();

        $scope.loadPage = function (page) {
            $scope.page = page;
            if($scope.searchCalled){
                $scope.loadAllSearch();
            }else{
                $scope.loadAll();
            }
        };
        $scope.loadAll();

        $scope.delete = function (event) {
            Event.get({id: event.id}, function (result) {
                $scope.event = result;
                $scope.deleteMessage();
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
            $scope.page = 1;
            $scope.searchCalled = true;
            $scope.loadAllSearch();
        };

        $scope.loadAllSearch = function () {
            EventSearch.query({query: $scope.searchQuery,selectedDate: $stateParams.selectedDate + '',
                page: $scope.page, per_page: 5}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
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
            $scope.$emit('windoctorApp:eventUpdate');
            $modalInstance.dismiss('cancel');
        };

        //End Patient pages treatement
        //Annul an appointment
        $scope.annul = function (event) {
            // Annul an appointment
            event.eventStatus.id = 2;
            $scope.save(event);
        };
        $scope.annulByPatient = function (event) {
            // Annul an appointment
            event.eventStatus.id = 10;
            $scope.save(event);
        };
        $scope.accept = function (event) {
            // Accept an appointment
            event.eventStatus.id = 1;
            $scope.save(event);
        };
        $scope.reject = function (event) {
            // Reject an appointment
            var eventTmp = {};
            angular.copy(event, eventTmp);
            console.log("$scope.events.length before " + $scope.events.length);
            $scope.events.splice($scope.events.indexOf(event), 1);
            console.log("$scope.events.length next " + $scope.events.length);
            eventTmp.eventStatus.id = 6;
            $scope.save(eventTmp);
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


        $scope.deleteMessage = function () {
            $scope.messageToDeleted = $translate.instant('windoctorApp.calendar.appointment.delete.question',{ param : $filter('date')($scope.event.event_date, 'HH:mm', '+0000')});
            console.log("event to delete  "+$scope.event);
            if($scope.event.eventStatus.id===1){
                // In progress
                $scope.messageToDeleted = $translate.instant('windoctorApp.calendar.appointment.delete.question',{ param : $filter('date')($scope.event.event_date, 'HH:mm', '+0000')});
            }else if($scope.event.eventStatus.id===2){
                // Annuled
                $scope.messageToDeleted = $translate.instant('windoctorApp.calendar.appointment.delete.questionAnnuled',{ param : $filter('date')($scope.event.event_date, 'HH:mm', '+0000')});
            }else if($scope.event.eventStatus.id===4){
                // Abondonned
                $scope.messageToDeleted = $translate.instant('windoctorApp.calendar.appointment.delete.questionAbondonned',{ param : $filter('date')($scope.event.event_date, 'HH:mm', '+0000')});
            }else if($scope.event.eventStatus.id===7){
                // Request
                $scope.messageToDeleted = $translate.instant('windoctorApp.calendar.request.question',{ param : $filter('date')($scope.event.event_date, 'HH:mm', '+0000')});
            }else if($scope.event.eventStatus.id===8){
                // Visit
                $scope.messageToDeleted = $translate.instant('windoctorApp.calendar.visit.question',{ param : $filter('date')($scope.event.event_date, 'HH:mm', '+0000')});
            }else if($scope.event.eventStatus.id===10 || $scope.event.eventStatus.id===11){
                // Annuled by patient
                $scope.messageToDeleted = $translate.instant('windoctorApp.calendar.appointment.delete.questionAnnuledByPatient',{ param : $filter('date')($scope.event.event_date, 'HH:mm', '+0000')});
            }else{
                $scope.messageToDeleted = $translate.instant('windoctorApp.calendar.appointment.delete.question',{ param : $filter('date')($scope.event.event_date, 'HH:mm', '+0000')});
            }
            return $scope.messageToDeleted;
        };


    });
