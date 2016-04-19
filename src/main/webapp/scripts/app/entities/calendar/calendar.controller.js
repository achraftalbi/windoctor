'use strict';

angular.module('windoctorApp')
    .controller('CalendarController', function ($scope, Event, EventAll, EventSearch, $modal, moment, $state, $translate, $filter,calendarConfig,Principal) {
        $scope.calendarEvts = [];
        $scope.account;
        $scope.loadAll = function () {
            Principal.identity(false).then(function (account) {
                $scope.account = account;
                EventAll.query(function (result) {
                    $scope.calendarEvts = [];
                    $scope.events = [];
                    $scope.calendarEvts = result;
                    //calendarConfig.dateFormatter = 'moment';
                    //calendarConfig.allDateFormats.moment.date.hour = 'HH:mm';
                    calendarConfig.displayAllMonthEvents = true;
                    calendarConfig.displayEventEndTimes = true;
                    calendarConfig.showTimesOnWeekView = true;
                    calendarConfig.dateFormats.hour = 'HH:mm';
                    calendarConfig.titleFormats.week = '{month} '+$translate.instant('global.simpleWeek')+' {week} '+$translate.instant('global.of')+' {year}';
                    var cuurentDate = new Date();

                    for (var i = 0; i < $scope.calendarEvts.length; i++) {
                        var startDate = new Date($filter('date')($scope.calendarEvts[i].event_date, 'yyyy-MM-dd HH:mm', '+0000'));
                        var endDate = new Date($filter('date')($scope.calendarEvts[i].event_date_end, 'yyyy-MM-dd HH:mm', '+0000'));

                        var patient = ($scope.calendarEvts[i].user!==null && $scope.calendarEvts[i].user!==undefined ?
                        'Patient :'+$scope.calendarEvts[i].user.firstName+', '+$scope.calendarEvts[i].user.lastName:'');
                        var description = ($scope.calendarEvts[i].description!==null && $scope.calendarEvts[i].description!==undefined ?
                            $scope.calendarEvts[i].description:'');
                        console.log('$scope.account.currentUserPatient calendar '+$scope.account.currentUserPatient);
                        if ($scope.account.currentUserPatient){
                            patient='';description='';
                        }
                        $scope.events.push({
                            title: '<p>'+patient+'</p><p>'+description+'</p>',
                            type: $scope.calendarEvts[i].eventStatus === null ? 'Not applicable' :
                                ($scope.calendarEvts[i].eventStatus.id === 1 ? 'info' :
                                    ($scope.calendarEvts[i].eventStatus.id === 2 ? 'special' :
                                        ($scope.calendarEvts[i].eventStatus.id === 3 ? 'success' :
                                            ($scope.calendarEvts[i].eventStatus.id === 4 ? 'important' :
                                                ($scope.calendarEvts[i].eventStatus.id === 7 ? 'request' :
                                                    ($scope.calendarEvts[i].eventStatus.id === 8 ? 'visit' :
                                                        ($scope.calendarEvts[i].eventStatus.id === 9 ? 'block' :
                                                            ($scope.calendarEvts[i].eventStatus.id === 10 ? 'special' :
                                                                ($scope.calendarEvts[i].eventStatus.id === 11 ? 'special' :
                                                                    'Not applicable'))))))))),
                            //angular.isUndefined($scope.calendarEvts[i].eventStatus)
                            //||$scope.calendarEvts[i].eventStatus==null?null:$scope.calendarEvts[i].eventStatus.description,
                            startsAt: startDate,
                            endsAt: endDate,
                            draggable: true,
                            resizable: true
                        })
                    }
                    console.log("current 1 language " + $translate.use());
                });
            });
        };

        $scope.loadAll();

        $scope.delete = function (event) {
            Event.get({id: event.id}, function (result) {
                $scope.calendarEvts = result;
                /*$scope.deleteMessage =$scope.event.eventStatus.id === 1 ? 'info' :
                    ($scope.event.eventStatus.id === 7 ? 'request');*/
                $('#deleteEvent_reasonConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Event.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEvent_reasonConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            Event.query({query: $scope.searchQuery}, function (result) {
                $scope.calendarEvts = result;
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
            $scope.calendarEvts = {description: null, id: null};
        };


        //These variables MUST be set as a minimum for the calendar to work
        $scope.calendarView = 'month';
        $scope.calendarDay = new Date();
        /*        $scope.events = [
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
         ];*/
        moment.locale($translate.use(), {
            week: {
                dow: 1 // Monday is the first day of the week
            }
        });
        $scope.cellClicked = function (date, view) {
            // Show modal etc to add a new event. date is the start of the month, day, hour etc depending on which view you're on.
            //$('#deleteEvent_reasonConfirmation').modal('show');
            console.log('selectedDate first one '+date);
            $state.go(view, {selectedDate: date});
        };
        /*
         var currentYear = moment().year();
         var currentMonth = moment().month();
         function random(min, max) {
         return Math.floor((Math.random() * max) + min);
         }*/

        function showModal(action, event) {
            $modal.open({
                templateUrl: 'modalContent.html',
                controller: function () {
                    var vm = this;
                    vm.action = action;
                    vm.event = event;
                },
                controllerAs: 'vm'
            });
        }

        $scope.eventClicked = function ($event) {
            //showModal('Clicked', event);
            $event.preventDefault();

        };

        $scope.eventEdited = function (event) {
            showModal('Edited', event);
        };

        $scope.eventDeleted = function (event) {
            showModal('Deleted', event);
        };

        $scope.eventTimesChanged = function (event) {
            showModal('Dropped or resized', event);
        };

        $scope.toggle = function ($event, field, event) {
            $event.preventDefault();
            $event.stopPropagation();
            event[field] = !event[field];
        };


    })
;
