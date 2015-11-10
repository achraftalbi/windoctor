'use strict';

angular.module('windoctorApp').controller('CalendarDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Event', 'Status', 'Event_reason', 'Patient', 'ParseLinks', 'PatientSearch','Principal',
        function ($scope, $stateParams, $modalInstance, entity, Event, Status, Event_reason, Patient, ParseLinks, PatientSearch,Principal) {

            $scope.event = entity;
            $scope.statuss = Status.query();
            $scope.event_reasons = Event_reason.query();
            $scope.patients = Patient.query();
            $scope.count = 0;
            $scope.showListPatients = false;
            $scope.account = null;
            console.log('patients ' + $scope.patients.length)
            $scope.load = function (id) {
                Event.get({id: id}, function (result) {
                    $scope.event = result;
                });
            };
            Principal.identity(true).then(function(account) {
                $scope.account = account;
                if($scope.account.currentUserPatient && ($scope.event.user === null || $scope.event.user === undefined)){
                    $scope.event.user = {id:$scope.account.id,firstName:$scope.account.firstName};
                }
            });


            //Begin Patient pages treatement
            $scope.page = 1;
            $scope.loadAll = function () {
                Patient.query({page: $scope.page, per_page: 5}, function (result, headers) {
                    $scope.links = ParseLinks.parse(headers('link'));
                    $scope.patients = result;
                });
            };
            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.loadAll();
            };
            $scope.loadAll();
            $scope.searchPatient = function () {
                PatientSearch.query({query: $scope.searchQueryPatient}, function (result) {
                    $scope.patients = result;
                }, function (response) {
                    if (response.status === 404) {
                        $scope.loadAll();
                    }
                });
            };
            $scope.displayListPatients = function () {
                $scope.page = 1;
                $scope.loadAll();
                $scope.showListPatients = true;
            };
            $scope.setPatient = function (patient) {
                $scope.event.user = patient;
                $scope.showListPatients = false;
            };
            //End Patient pages treatement


            var onSaveFinished = function (result) {
                $scope.$emit('windoctorApp:eventUpdate', result);
                $modalInstance.close(result);
            };

            $scope.save = function () {
                if ($scope.event.id != null) {
                    Event.update($scope.event, onSaveFinished);
                } else {
                    Event.save($scope.event, onSaveFinished);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
