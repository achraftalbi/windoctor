'use strict';

angular.module('windoctorApp')
    .controller('CalendarTreatmentController', function ($scope, $stateParams, $modalInstance, Treatment, TreatmentSearch, Doctor,
                                                         ParseLinks, $filter, Event_reason, Event, Patient,Attachment) {
        $scope.treatments = [];
        $scope.page = 1;
        $scope.attachmentPage = 1;
        $scope.treatment = null;
        $scope.event = null;
        $scope.patient = null;
        $scope.doctors = null;
        $scope.defaultDoctor = null;
        $scope.treatmentToDelete = null;

        $scope.displayTreatments = true;
        $scope.displayAddEditViewPopup = false;
        $scope.displayAddEditViewAttachmentPopup = false;
        $scope.displayTreatmentView = false;
        $scope.displayAllPatientTreatments = false;
        $scope.displayTreatmentToDelete = false;
        $scope.event_reasons = null;
        $scope.defaultEventReason = null;
        $scope.selectedDate = null;
        $scope.eventReasonSelected = false;

        /*Attachments variables */
        $scope.dispalyAttachments = false;
        $scope.attachments = null;
        $scope.attachment = null;
        $scope.viewSelectedAttachment = false;

        // Display treatments Begin
        $scope.loadAll = function () {
            if ($scope.displayAllPatientTreatments === false) {
                Treatment.query({
                        eventId: $stateParams.eventId,
                        page: $scope.page,
                        per_page: 5
                    }, function (result, headers) {
                        console.log("$stateParams.eventId value "+$stateParams.eventId);
                        $scope.selectedDate = new Date($stateParams.selectedDate);
                        $scope.links = ParseLinks.parse(headers('link'));
                        $scope.treatments = result;
                        if ($scope.event === null) {
                            Event.get({id: $stateParams.eventId}, function (result) {
                                $scope.event = result;
                            });
                        }
                    }
                );
            } else {
                Treatment.query({
                        patientId: $scope.event.user.id,
                        page: $scope.page,
                        per_page: 5
                    }, function (result, headers) {
                        $scope.links = ParseLinks.parse(headers('link'));
                        $scope.treatments = result;
                    }
                )
                ;
            }
        };


        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Treatment.get({id: id}, function (result) {
                $scope.treatmentToDelete = result;
                $scope.displayTreatmentToDelete = true;
                $scope.displayTreatments = false;
            });
        };

        $scope.confirmDelete = function (id) {
            Treatment.delete({id: id},
                function () {
                    $scope.loadPage($scope.page);
                    $scope.displayTreatmentToDelete = false;
                    $scope.displayTreatments = true;
                });
        };

        $scope.search = function () {
            TreatmentSearch.query({query: $scope.searchQuery}, function (result) {
                $scope.treatments = result;
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
            $scope.treatment = {treatment_date: null, description: null, price: null, paid_price: null,doctor:null, id: null};
        };
        $scope.clearDeleteTreatment = function () {
            $scope.treatmentToDelete = {
                treatment_date: null,
                description: null,
                price: null,
                paid_price: null,
                id: null
            };
            $scope.displayTreatmentToDelete = false;
            $scope.displayTreatments = true;
        };
        $scope.cancelTreatmentRows = function () {
            $modalInstance.dismiss('cancel');
        };
        // Treatment to display all or only treatment events
        $scope.displayAllPatientTreatmentsFunction = function () {
            $scope.displayAllPatientTreatments = true;
            $scope.page = 1;
            $scope.loadPage($scope.page);
        }
        $scope.displayPatientTreatmentsEventFunction = function () {
            $scope.displayAllPatientTreatments = false;
            $scope.page = 1;
            $scope.loadPage($scope.page);
        }


        // Display treatments end



        // Add - Edit - View treatments

        $scope.addNewTreatment = function () {
            $scope.treatment = {
                treatment_date: $scope.event.event_date,
                description: null,
                price: null,
                paid_price: null,
                id: null,
                eventReason : $scope.defaultEventReason,
                doctor:$scope.defaultDoctor,
                event: {id: $stateParams.eventId}
            };
            $scope.dialogPopupTreatment();
        };
        $scope.editTreatment = function (treatment) {
            $scope.treatment = treatment;
            $scope.dialogPopupTreatment();
            $scope.loadAllAttachments(1);
        };
        $scope.dialogPopupTreatment = function (treatment) {
            $scope.displayAddEditViewPopup = true;
            $scope.displayTreatments = false;
            $scope.initEventReasons();
            $scope.initDoctors();
        }
        $scope.initEventReasons = function () {
            if ($scope.event_reasons === null) {
                Event_reason.query(function (result, headers) {
                        $scope.event_reasons = result;
                        if($scope.event_reasons!==null && $scope.event_reasons.length > 0){
                            $scope.defaultEventReason = $scope.event_reasons[0]
                            console.log(" $scope.defaultEventReason a "+$scope.defaultEventReason);
                        }
                        if($scope.treatment!==null){
                            $scope.treatment.eventReason = $scope.defaultEventReason;
                        }
                    }
                );
            }
        }
        $scope.initDoctors = function () {
            if ($scope.doctors === null) {
                Doctor.query(function (result, headers) {
                        $scope.doctors = result;
                        console.log(" $scope.defaultDoctor 3 "+$scope.defaultDoctor);
                        if($scope.doctors!==null && $scope.doctors.length > 0){
                            $scope.defaultDoctor = $scope.doctors[0]
                            console.log(" $scope.defaultDoctor 4 "+$scope.defaultDoctor);
                        }
                        if($scope.treatment!==null){
                            $scope.treatment.doctor = $scope.defaultDoctor;
                        }
                    }
                );

            }
        }

        var onSaveTreatmentFinished = function (result) {
            $scope.$emit('windoctorApp:treatmentUpdate', result);
            $scope.loadPage($scope.page);
        };

        $scope.saveTreatmentAndClose = function () {
            $scope.saveTreatment();
            $scope.displayAddEditViewPopup = false;
            $scope.displayTreatments = true;
        }

        $scope.saveTreatment = function () {
            if ($scope.treatment.id != null) {
                $scope.treatment = Treatment.update($scope.treatment, onSaveTreatmentFinished);
            } else {
                $scope.treatment = Treatment.save($scope.treatment, onSaveTreatmentFinished);
            }
            $scope.loadAllAttachments(1);
        };
        $scope.closeAddEditViewPopup = function () {
            $scope.displayAddEditViewPopup = false;
            $scope.displayTreatments = true;
            $scope.displayTreatmentView = false;
            $scope.clear();
        };
        $scope.viewTreatmentDetail = function (treatment) {
            $scope.treatment = treatment;
            $scope.displayAddEditViewPopup = true;
            $scope.displayTreatments = false;
            $scope.displayTreatments = false;
            $scope.displayTreatmentView = true;
            $scope.loadAllAttachments(1);
        };

        /********************************************************************************/
        /********************************************************************************/
        /***********************                                       ******************/
        /***********************          Attachments functions        ******************/
        /***********************                                       ******************/
        /********************************************************************************/
        /********************************************************************************/
        /********************************************************************************/

        $scope.loadAllAttachments = function (attachmentPage){
                $scope.attachmentPage = attachmentPage;
                Attachment.query({treatmentId:$scope.treatment.id,page: $scope.attachmentPage, per_page: 5}, function (result, headers) {
                    $scope.linkAttachments = ParseLinks.parse(headers('link'));
                    $scope.attachments = result;
                });
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

        $scope.setImage = function ($files, attachment) {
            if ($files[0]) {
                var file = $files[0];
                var fileReader = new FileReader();
                fileReader.readAsDataURL(file);
                fileReader.onload = function (e) {
                    var data = e.target.result;
                    var base64Data = data.substr(data.indexOf('base64,') + 'base64,'.length);
                    $scope.$apply(function() {
                        attachment.image = base64Data;
                    });
                };
            }
        };

        $scope.addNewAttachment = function () {
            $scope.attachment = {description: null, image: null, id: null,treatment: {id: $scope.treatment.id}};
            $scope.dialogPopupAttachment();
            $scope.viewSelectedAttachment = false;
        };
        $scope.editAttachment = function (attachment) {
            $scope.attachment = attachment;
            $scope.dialogPopupAttachment();
        };
        $scope.dialogPopupAttachment = function () {
            $scope.displayAddEditViewAttachmentPopup = true;
        }

        var onSaveAttachmentFinished = function (result) {
            $scope.$emit('windoctorApp:attachmentUpdate', result);
            $scope.loadAllAttachments($scope.attachmentPage);
            $scope.displayAddEditViewAttachmentPopup = false;
        };

        $scope.saveAttachment = function () {
            if ($scope.attachment.id != null) {
                $scope.attachment = Attachment.update($scope.attachment, onSaveAttachmentFinished);
            } else {
                $scope.attachment = Attachment.save($scope.attachment, onSaveAttachmentFinished);
            }
        };

        $scope.deleteAttachment = function (id) {
            Attachment.delete({id: id},
                function () {
                    $scope.loadAllAttachments($scope.attachmentPage);
                });
        };
        $scope.editAttachment = function (attachment) {
            $scope.attachment = attachment;
            $scope.dialogPopupAttachment();
            $scope.viewSelectedAttachment = false;
        };
        $scope.viewAttachment = function (attachment) {
            $scope.attachment = attachment;
            $scope.dialogPopupAttachment();
            $scope.viewSelectedAttachment = true;
        };
        $scope.clearAttachmentDialog = function () {
            $scope.attachment = {description: null, image: null, id: null,treatment: null};
            $scope.displayAddEditViewAttachmentPopup = false;
            $scope.viewSelectedAttachment = false;
        };
    })
;
