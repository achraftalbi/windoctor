'use strict';

angular.module('windoctorApp')
    .controller('CalendarController', function ($scope, Event_reason, Event_reasonSearch, $modal, moment,$state, $translate) {
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




        //These variables MUST be set as a minimum for the calendar to work
        $scope.calendarView = 'month';
        $scope.calendarDay = new Date();
        $scope.events = [
            {
                title: 'An event',
                type: 'warning',
                startsAt: moment().startOf('week').subtract(2, 'days').add(8, 'hours').toDate(),
                endsAt: moment().startOf('week').add(1, 'week').add(9, 'hours').toDate(),
                draggable: false,
                resizable: false
            }, {
                title: '<i class="glyphicon glyphicon-asterisk"></i> <span class="text-primary">Another event</span>, with a <i>html</i> title',
                type: 'info',
                startsAt: moment().subtract(1, 'day').toDate(),
                endsAt: moment().add(5, 'days').toDate(),
                draggable: false,
                resizable: false
            }, {
                title: 'This is a really long event title that occurs on every year',
                type: 'important',
                startsAt: moment().startOf('day').add(7, 'hours').toDate(),
                endsAt: moment().startOf('day').add(19, 'hours').toDate(),
                recursOn: 'year',
                draggable: false,
                resizable: false
            }
        ];
        moment.locale($translate.use(), {
            week : {
                dow : 1 // Monday is the first day of the week
            }
        });
        $scope.cellClicked = function(date,view) {
            // Show modal etc to add a new event. date is the start of the month, day, hour etc depending on which view you're on.
            //$('#deleteEvent_reasonConfirmation').modal('show');
            $state.go(view, { currentDate: date });
        };
        /*
         var currentYear = moment().year();
         var currentMonth = moment().month();
         function random(min, max) {
         return Math.floor((Math.random() * max) + min);
         }
         for (var i = 0; i < 1000; i++) {
         var start = new Date(currentYear,random(0, 11),random(1, 28),random(0, 24),random(0, 59));
         vm.events.push({
         title: 'Event ' + i,
         type: 'warning',
         startsAt: start,
         endsAt: moment(start).add(2, 'hours').toDate()
         })
         }*/

        function showPopup(action, event) {
            //$modal.open({
            //    templateUrl: 'modalContent.html',
            //    controller: function() {
            //        var vm = this;
            //        vm.action = action;
            //        vm.event = event;
            //    },
            //    controllerAs: 'vm'
            //});
            $modal.open({
                templateUrl: 'scripts/app/entities/event_reason/event_reason-dialog.html',
                controller: 'Event_reasonDialogController',
                size: 'lg',
                resolve: {
                    entity: ['Event_reason', function(Event_reason) {
                        return Event_reason.get({id : $stateParams.id});
                    }]
                }
            }).result.then(function(result) {
                    $state.go('event_reason', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
        }

        function showModal(action, event) {
            $modal.open({
                templateUrl: 'modalContent.html',
                controller: function() {
                    var vm = this;
                    vm.action = action;
                    vm.event = event;
                },
                controllerAs: 'vm'
            });
        }

        $scope.eventClicked = function(event) {
            showModal('Clicked', event);
        };

        $scope.eventEdited = function(event) {
            showModal('Edited', event);
        };

        $scope.eventDeleted = function(event) {
            showModal('Deleted', event);
        };

        $scope.eventTimesChanged = function(event) {
            showModal('Dropped or resized', event);
        };

        $scope.toggle = function($event, field, event) {
            $event.preventDefault();
            $event.stopPropagation();
            event[field] = !event[field];
        };





    })
;
