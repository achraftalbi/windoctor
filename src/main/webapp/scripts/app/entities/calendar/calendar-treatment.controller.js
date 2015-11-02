'use strict';

angular.module('windoctorApp')
    .controller('CalendarTreatmentController', function ($scope, $stateParams, $modalInstance, Treatment, TreatmentSearch,
                                                         ParseLinks, $filter, Event_reason, Event, Patient, Product,Attachment) {
        $scope.treatments = [];
        $scope.page = 1;
        $scope.attachmentPage = 1;
        $scope.treatment = null;
        $scope.event = null;
        $scope.patient = null;
        $scope.product = null;
        $scope.treatmentToDelete = null;

        $scope.displayTreatments = true;
        $scope.displayAddEditViewPopup = false;
        $scope.displayAddEditViewAttachmentPopup = false;
        $scope.displayTreatmentView = false;
        $scope.displayAllPatientTreatments = false;
        $scope.displayTreatmentToDelete = false;
        $scope.event_reasons = null;
        $scope.selectedDate = null;
        $scope.eventReasonSelected = false;

        /*Attachments variables */
        $scope.dispalyAttachments = false;
        $scope.attachments = null;
        $scope.attachment = null;

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
                                if ($scope.patient === null) {
                                    Product.get({id: 1}, function (result) {
                                        //$scope.patient = result;
                                        $scope.product = result;
                                    });
                                }

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
            $scope.treatment = {treatment_date: null, description: null, price: null, paid_price: null, id: null};
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
                event: {id: $stateParams.eventId}
            };
            $scope.dialogPopupTreatment();
        };
        $scope.editTreatment = function (treatment) {
            $scope.treatment = treatment;
            $scope.dialogPopupTreatment();
            $scope.valueChanged();
            $scope.loadAllAttachments(1);
        };
        $scope.dialogPopupTreatment = function (treatment) {
            $scope.displayAddEditViewPopup = true;
            $scope.displayTreatments = false;
            $scope.initEventReason();
        }
        $scope.initEventReason = function () {
            if ($scope.event_reasons === null) {
                $scope.event_reasons = Event_reason.query();
            }
        }
        $scope.valueChanged = function () {
            if ($scope.treatment.eventReason === null) {
                $scope.eventReasonSelected = false;
            } else {
                $scope.eventReasonSelected = true;
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
        };

        // Attachments functions
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
                $scope.attachment = Treatment.update($scope.attachment, onSaveAttachmentFinished);
            } else {
                $scope.attachment = Treatment.save($scope.attachment, onSaveAttachmentFinished);
            }
        };

    })
;
