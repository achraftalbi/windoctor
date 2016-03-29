'use strict';

angular.module('windoctorApp').expandCalendarEventsControllerToDialog =
    function ($scope, $stateParams, Event, EventDTO , Patient, ParseLinks, PatientSearch, $filter, Principal) {

        //$scope.event = entity;
        $scope.showListPatients = false;
        $scope.account = null;
        $scope.visit = false;
        $scope.searchCalledPatients = false;
        $scope.load = function (id) {
            Event.get({id: id}, function (result) {
                $scope.event = result;
            });
        };
        Principal.identity(false).then(function (account) {
            $scope.account = account;
            if ($scope.account.currentUserPatient && ($scope.event.user === null || $scope.event.user === undefined)) {
                $scope.event.user = {id: $scope.account.id, firstName: $scope.account.firstName};
            }
        });
        $scope.callDataPicker = function () {
            if ($scope.eventDTO === null || $scope.eventDTO === undefined) {
                console.log('test eventId sent to server ' + $scope.event.id);
                EventDTO.query({
                    selectedDate: $stateParams.selectedDate + '',
                    eventId: $scope.event.id
                }, function (result) {
                    $scope.endDateTab = [];
                    $scope.eventDTO = result;
                    console.log('$scope.event.eventStatus value' + $scope.event.eventStatus);
                    console.log('$scope.event.eventStatus.id value' + $scope.event.eventStatus.id);
                    if ($scope.event.eventStatus.id !== null && $scope.event.eventStatus.id !== 8) {
                        $scope.visit = false;
                        if ($scope.event.id !== null && $scope.event.id !== undefined) {
                            console.log('test eventId 2' + $scope.event.id);
                            $scope.displayDate = true;
                            $scope.startDateValue = $filter('date')($scope.event.event_date, 'HH:mm', '+0000');
                            $scope.endDateValue = $filter('date')($scope.event.event_date_end, 'HH:mm', '+0000');
                            var findFirstEndDate = false;
                            for (var i = 0; i < $scope.eventDTO.endDateList.length; i++) {
                                console.log("3  $scope.$scope.eventDTO.endDateList[i] " + $scope.eventDTO.endDateList[i]);
                                if ($scope.eventDTO.endDateList[i].localeCompare($scope.startDateValue) > 0 && $scope.eventDTO.endDateList[i] !== 'NO' && (!findFirstEndDate)) {
                                    findFirstEndDate = true;
                                    $scope.endDateTab.push($scope.eventDTO.endDateList[i]);
                                    console.log("2  $scope.$scope.eventDTO.endDateList[i] " + $scope.eventDTO.endDateList[i]);
                                } else if ($scope.eventDTO.endDateList[i] === 'NO' && findFirstEndDate) {
                                    break;
                                } else if (findFirstEndDate) {
                                    $scope.endDateTab.push($scope.eventDTO.endDateList[i]);
                                }
                            }
                        }
                        console.log('test eventId 3' + $scope.event.id);
                        for (var i = 0; i < $scope.eventDTO.startDateList.length; i++) {
                            console.log("1  $scope.$scope.eventDTO.startDateList[i] " + $scope.eventDTO.startDateList[i]);
                            if ($scope.eventDTO.startDateList[i] !== 'NO') {
                                $scope.startDateTab.push($scope.eventDTO.startDateList[i]);
                                console.log("2  $scope.$scope.eventDTO.startDateList[i] " + $scope.eventDTO.startDateList[i]);
                            }
                        }
                        $scope.startDateTabInit = $scope.eventDTO.startDateList;
                        $scope.endDateTabInit = $scope.eventDTO.endDateList;
                        $.picker.batch('.pickerstartdate', 'destroy');
                        $('.pickerstartdate').picker({
                            items: $scope.startDateTab,
                            selectedCustomClass: 'label label-success',
                            mustAccept: false,
                            hideOnSelect: true,
                            placement: 'right',
                            showFooter: false,
                            input: '> .btn i',
                            component: '> .btn',
                            templates: {
                                popover: '<div class="picker-popover popover" style="display: block; top: 50px; left: -102.328px; max-width: 769.672px;">' +
                                '<div class="arrow"></div><div class="popover-title"></div><div class="popover-content"></div></div>'
                            }
                        })
                        $('.pickerstartdate').data('picker').pickerValue = null;
                        $('.picker-item').bind('click', function () {
                            $scope.changeDataPickerStartDate();
                        });
                        console.log("test pickerstartdate " + $('.pickerstartdate').data('picker').pickerValue);
                        console.log("startDateTab " + $scope.startDateTab);
                        $.picker.batch('.pickerenddate', 'destroy');
                        $('.pickerenddate').picker1({
                            items: $scope.endDateTab,
                            selectedCustomClass: 'label label-success',
                            mustAccept: false,
                            hideOnSelect: true,
                            placement: 'right',
                            showFooter: false,
                            input: '> .btn i',
                            component: '> .btn',
                            templates: {
                                popover: '<div class="picker1-popover1 popover1" style="display: block; top: 50px; left: -102.328px; max-width: 769.672px;">' +
                                '<div class="arrow"></div><div class="popover1-title"></div><div class="popover1-content"></div></div>'
                            }
                        });
                        $('.pickerenddate').data('picker1').picker1Value = null;
                        $('.picker1-item').bind('click', function () {
                            $scope.changeDataPickerEndDate();
                        });
                        console.log("test pickerenddate " + $('.pickerenddate').data('picker1').picker1Value);
                        console.log("endDateTab " + $scope.endDateTab);
                    }else if($scope.event.eventStatus.id !== null && $scope.event.eventStatus.id === 8){
                        $scope.visit = true;
                        var curentDate = new Date();
                        if($scope.event.id===null || $scope.event.id === undefined) {
                            $scope.event.event_date = new Date($filter('date')($stateParams.selectedDate, 'MM/dd/yyyy').
                                    replace('00:00:00', moment(curentDate).format('HH:mm') + ':00') + ' +00:00');
                            console.log("$scope.event.event_date rrrr"+$scope.event.event_date);
                            $scope.event.event_date_end = $scope.event.event_date;
                            $scope.startDateValue = moment(curentDate).format('HH:mm') + '';
                            $scope.endDateValue = $scope.startDateValue;
                        }else{
                            $scope.startDateValue = $filter('date')($scope.event.event_date, 'HH:mm', '+0000');
                            $scope.endDateValue = $filter('date')($scope.event.event_date_end, 'HH:mm', '+0000');
                        }
                    }
                });
            }
        };
        $scope.loadInitialData = function () {
            $scope.eventDTO = null;
            $scope.startDateTabInit = [];
            $scope.endDateTabInit = [];
            $scope.startDateTab = [];
            $scope.endDateTab = [];
            $scope.displayDate = false;
            $scope.startDateValue = null;
            $scope.endDateValue = null;
            console.log('call init function');
            $scope.callDataPicker();
            if($scope.patients===null || $scope.patients===undefined
                || $scope.patients.length===0){
                $scope.patients = Patient.query();
            }
        };
        $scope.loadInitialData();
        $scope.changeDataPickerStartDate = function () {
            if ($('.pickerstartdate').data('picker').pickerValue !== null &&
                $('.pickerstartdate').data('picker').pickerValue !== undefined) {
                var startDate = $filter('date')($stateParams.selectedDate, 'MM/dd/yyyy').replace('00:00:00', $('.pickerstartdate').data('picker').pickerValue + ':00');
                var startDateUTC = $filter('date')($stateParams.selectedDate, 'MM/dd/yyyy').replace('00:00:00', $('.pickerstartdate').data('picker').pickerValue + ':00') + ' +00:00';
                console.log("99999 startDateUTC " + startDateUTC);
                $scope.event.event_date = new Date(startDateUTC);
                $scope.startDateValue = $filter('date')(new Date(startDate), 'HH:mm');
                var findFirstEndDate = false;
                $scope.endDateTab = [];
                $('.pickerenddate').data('picker1').picker1Value = null;
                for (var i = 0; i < $scope.eventDTO.endDateList.length; i++) {
                    console.log("3  $scope.$scope.eventDTO.endDateList[i] " + $scope.eventDTO.endDateList[i]);
                    if ($scope.eventDTO.endDateList[i].localeCompare($scope.startDateValue) > 0 && $scope.eventDTO.endDateList[i] !== 'NO' && (!findFirstEndDate)) {
                        findFirstEndDate = true;
                        $scope.endDateTab.push($scope.eventDTO.endDateList[i]);
                        console.log("2  $scope.$scope.eventDTO.endDateList[i] " + $scope.eventDTO.endDateList[i]);
                    } else if ($scope.eventDTO.endDateList[i] === 'NO' && findFirstEndDate) {
                        break;
                    } else if (findFirstEndDate) {
                        $scope.endDateTab.push($scope.eventDTO.endDateList[i]);
                    }
                }
                console.log("99 endDateTab " + $scope.endDateTab);
                $.picker1.batch('.picker1-element', 'destroy');
                $('.pickerenddate').picker1({
                    items: $scope.endDateTab,
                    selectedCustomClass: 'label label-success',
                    mustAccept: false,
                    hideOnSelect: true,
                    placement: 'right',
                    showFooter: false,
                    input: '> .btn i',
                    component: '> .btn',
                    templates: {
                        popover: '<div class="picker1-popover1 popover1" style="display: block; top: 50px; left: -102.328px; max-width: 769.672px;">' +
                        '<div class="arrow"></div><div class="popover1-title"></div><div class="popover1-content"></div></div>'
                    }
                });
                $('.pickerenddate').data('picker1').picker1Value = null;
                $('.picker1-item').bind('click', function () {
                    $scope.changeDataPickerEndDate();
                });
                $scope.endDateValue = null;
            }
        };
        $scope.changeDataPickerEndDate = function () {
            console.log("Here1 endDateTab " + $scope.endDateTab);
            if ($('.pickerenddate').data('picker1').picker1Value !== null &&
                $('.pickerenddate').data('picker1').picker1Value !== undefined) {
                console.log("Here2 endDateTab " + $scope.endDateTab);
                var endDate = $filter('date')($stateParams.selectedDate, 'MM/dd/yyyy').replace('00:00:00', $('.pickerenddate').data('picker1').picker1Value + ':00');
                var endDateUTC = $filter('date')($stateParams.selectedDate, 'MM/dd/yyyy').replace('00:00:00', $('.pickerenddate').data('picker1').picker1Value + ':00') + ' +00:00';
                $scope.event.event_date_end = new Date(endDateUTC);
                console.log("end display event_date_end " + $scope.event.event_date_end);
                console.log("end endDateUTC " + endDateUTC);
                $scope.endDateValue = $filter('date')(new Date(endDate), 'HH:mm');
                console.log("end display 2 endDateValue " + $scope.endDateValue);
            }
        };
        //Begin Patient pages treatement
        $scope.pagePatients = 1;
        $scope.loadAllPatients = function () {
            Patient.query({page: $scope.pagePatients, per_page: 5}, function (result, headers) {
                $scope.linksPatients = ParseLinks.parse(headers('link'));
                $scope.patients = result;
            });
        };
        $scope.loadPagePatients = function (page) {
            $scope.pagePatients = page;
            if($scope.searchCalledPatients){
                $scope.loadAllSearchPatient();
            }else{
                $scope.loadAllPatients();
            }
        };

        $scope.searchPatient = function () {
            $scope.pagePatients = 1;
            $scope.searchCalledPatients = true;
            $scope.loadAllSearchPatient();
        };

        $scope.loadAllSearchPatient = function () {
            PatientSearch.query({query: $scope.searchQueryPatient,
                page: $scope.pagePatients, per_page: 5}, function (result, headers) {
                $scope.linksPatients = ParseLinks.parse(headers('link'));
                $scope.patients = result;
            }, function (response) {
                if (response.status === 404) {
                    $scope.loadAllPatients();
                }
            });
        };

        $scope.displayListPatients = function () {
            $scope.pagePatients = 1;
            $scope.loadAllPatients();
            $scope.showListPatients = true;
        };
        $scope.setPatient = function (patient) {
            $scope.event.user = patient;
            $scope.showListPatients = false;
        };

        var onSaveFinishedEventDialog = function (result) {
            $scope.$emit('windoctorApp:eventUpdate', result);
            $scope.loadPageEvents(1);
            $scope.returnToEventsPage();
        };

        var onUpdateFinishedEventDialog = function (result) {
            $scope.$emit('windoctorApp:eventUpdate', result);
            var orderBy = $filter('orderBy');
            $scope.events = orderBy($scope.events, 'event_date', false);
            $scope.returnToEventsPage();
        };

        $scope.saveEventDialog = function () {
            var eventTmp = {};
            var userTmp = {};
            angular.copy($scope.event, eventTmp);
            userTmp={id:$scope.event.user.id};
            eventTmp.user=userTmp;
            if (eventTmp.id != null) {
                Event.update(eventTmp, onUpdateFinishedEventDialog);
            } else {
                Event.save(eventTmp, onSaveFinishedEventDialog);
            }
        };

        $scope.clear = function () {
            $scope.returnToEventsPage();
        };
    };
