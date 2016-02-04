'use strict';

angular.module('windoctorApp').controller('EventBlockDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Event', 'Status','$rootScope', 'Event_reason','Principal',
        function ($scope, $stateParams, $modalInstance, entity, Event, Status,$rootScope,Event_reason,Principal) {

            $scope.event = entity;
            $scope.statuss = Status.query();
            $scope.event_reasons = Event_reason.query();
            $scope.dateValue= moment(new Date()).utc();
            $scope.minDateValue =  moment(new Date()).utc();
            $scope.account;
            //$scope.editFormBlock.event_date.$setValidity('required', false);

            $scope.load = function (id) {
                Event.get({id: id}, function (result) {
                    $scope.event = result;
                });
            };

            Principal.identity().then(function(account) {
                $scope.account = account;
            });

            $scope.changeDate = function (modelName, newDate) {
                console.log(modelName + ' has had a date change. New value is TEST  ' + newDate.format());
                console.log(modelName + ' has had a date change. New value is ' + newDate.format().split('T')[0]+'T00:00:00+00:00');
                $scope.dateValue = moment(new Date(newDate.format().split('T')[0]+'T00:00:00+00:00')).utc();
                console.log(modelName + ' has had a date change. $scope.dateValue ' +new Date(moment(new Date($scope.dateValue)).utc()));
            }

            $scope.options = {
                view: 'date',
                format: 'lll',
                maxView: false,
                minView: 'hours',
            };

            $scope.initCalendars = function () {
                /*$('#field_event_date').element.datetimepicker();
                $('#field_event_date_end').element.datetimepicker({
                    useCurrent: false //Important! See issue #1075
                });
                $("#field_event_date").on("dp.change", function (e) {
                    $('#field_event_date_end').data("DateTimePicker").minDate(e.date);
                });
                $("#field_event_date_end").on("dp.change", function (e) {
                    $('#field_event_date').data("DateTimePicker").maxDate(e.date);
                });*/
                $scope.minDateValue = moment(new Date()).utc();

            };

            $scope.open1 = function() {
                $scope.popup1.opened = true;
            };

            $scope.initCalendars();

            var onSaveFinished = function (result) {
                $scope.$emit('windoctorApp:eventUpdate', result);
                $modalInstance.close(result);
            };

            $scope.save = function () {
                console.log('1 save blocked day executed');
                $scope.event.event_date = new Date($scope.dateValue);
                $scope.event.event_date_end = new Date($scope.dateValue);
                $scope.event.user = {id:$scope.account.id};
                if ($scope.event.id != null) {
                    Event.update($scope.event, onSaveFinished);
                } else {
                    console.log('save blocked day executed');
                    Event.save($scope.event, onSaveFinished);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
