'use strict';

angular.module('windoctorApp').expandPatientController =
    function ($scope, $stateParams, Patient) {
            $scope.error = null;
            $scope.errorUserExists = null;
            $scope.errorEmailExists = null;
            $scope.dateValue = moment(new Date()).utc();
            $scope.maxDateValue =  moment(new Date()).utc();
            if($scope.patient.phoneNumber===null || $scope.patient.phoneNumber===undefined
                || $scope.patient.phoneNumber.length!==14){
                $scope.patient.phoneNumber = '00212' + $scope.patient.phoneNumber;
            }
            $scope.savePatientInformationsOnGoing = false;
            $scope.captureAnImageScreen = false;
            $scope.initBirthDate = function () {
                $scope.maxDateValue = moment(new Date()).utc();
                $scope.dateValue = moment($scope.patient.birthDate===null || $scope.patient.birthDate===undefined?new Date():$scope.patient.birthDate).utc();
            };
            /*$scope.load = function () {
                $scope.patient.phoneNumber = '00212' + $scope.patient.phoneNumber;
            };
            $scope.load();*/
            $scope.initBirthDate();
            var onSaveFinishedPatientInformation = function (result) {
                $scope.$emit('windoctorApp:patientUpdate', result);
                $scope.addPatientField= false;
                $scope.editPatientField= false;
                $scope.savePatientInformationsOnGoing = false;
                if($scope.displayChartPage===true){
                    return;
                }
                if($scope.event!==null && $scope.event!==undefined
                    && $scope.event.id!==null){
                    $scope.displayTreatmentsPage = true;
                    return;
                }
                $scope.displayPatientPage = true;
                $scope.loadAll();
                //$modalInstance.close(result);
            };
            var onSaveFailedPatientInformation = function (response) {
                $scope.savePatientInformationsOnGoing = false;
                if (response.status === 400 && response.data.code === 'U-02') {
                    $scope.errorUserExists = 'ERROR';
                } else if (response.status === 400 && response.data.code === 'U-01') {
                    $scope.errorEmailExists = 'ERROR';
                } else {
                    $scope.error = 'ERROR';
                }
            };

            $scope.savePatientInformation = function () {
                $scope.patient.birthDate = new Date($scope.dateValue);
                $scope.savePatientInformationsOnGoing = true;
                if ($scope.patient.id != null) {
                    Patient.update($scope.patient, onSaveFinishedPatientInformation, onSaveFailedPatientInformation);
                } else {
                    Patient.save($scope.patient, onSaveFinishedPatientInformation, onSaveFailedPatientInformation);
                }
            };

            $scope.changeBlockStatus = function () {
                $scope.patient.blocked = !$scope.patient.blocked;
                console.log('$scope.patient.blocked ' + $scope.patient.blocked);
            }

            $scope.changeSmokingStatus = function () {
                $scope.patient.smoking = !$scope.patient.smoking;
                console.log('$scope.patient.smoking ' + $scope.patient.smoking);
            }

            $scope.changeBleedingWhileBrushingStatus = function () {
                $scope.patient.bleedingWhileBrushing = !$scope.patient.bleedingWhileBrushing;
                console.log('$scope.patient.bleedingWhileBrushing ' + $scope.patient.bleedingWhileBrushing);
            }

            $scope.changeToothSensitivityStatus = function () {
                $scope.patient.toothSensitivity = !$scope.patient.toothSensitivity;
                console.log('$scope.patient.toothSensitivity ' + $scope.patient.toothSensitivity);
            }

            $scope.clearPatientInformation = function () {
                $scope.addPatientField= false;
                $scope.editPatientField= false;
                if($scope.displayChartPage===true){
                    return;
                }
                if($scope.event!==null && $scope.event!==undefined
                    && $scope.event.id!==null){
                    $scope.displayTreatmentsPage = true;
                    return;
                }
                $scope.loadAll();
                $scope.displayPatientPage = true;
                //$modalInstance.dismiss('cancel');
            };

            $scope.abbreviate = function (text) {
                if (!angular.isString(text)) {
                    return '';
                }
                if (text.length < 30) {
                    return text;
                }
                return text ? (text.substring(0, 15) + '...' + text.slice(-10)) : '';
            };

            $scope.byteSize = function (base64String) {
                if (!angular.isString(base64String)) {
                    return '';
                }
                function endsWith(suffix, str) {
                    return str.indexOf(suffix, str.length - suffix.length) !== -1;
                }

                function paddingSize(base64String) {
                    if (endsWith('==', base64String)) {
                        return 2;
                    }
                    if (endsWith('=', base64String)) {
                        return 1;
                    }
                    return 0;
                }

                function size(base64String) {
                    return base64String.length / 4 * 3 - paddingSize(base64String);
                }

                function formatAsBytes(size) {
                    return size.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ") + " bytes";
                }

                return formatAsBytes(size(base64String));
            };

    };
