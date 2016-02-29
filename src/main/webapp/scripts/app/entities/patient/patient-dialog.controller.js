'use strict';

angular.module('windoctorApp').controller('PatientDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Patient',
        function ($scope, $stateParams, $modalInstance, entity, Patient) {
            $scope.doNotMatch = null;
            $scope.error = null;
            $scope.errorUserExists = null;
            $scope.errorEmailExists = null;
            $scope.dateValue = moment(new Date()).utc();
            $scope.maxDateValue =  moment(new Date()).utc();
            $scope.patient = entity;
            $scope.password=null;
            $scope.confirmPassword=null;
            $scope.password = 'Empty_Password_1';
            $scope.confirmPassword = 'Empty_Password_1';
            console.log('$scope.patient.login '+$scope.patient.login);
            $scope.captureAnImageScreen = false;
            $scope.load = function (id) {
                if($stateParams.id !== null && $stateParams.id !==undefined){
                    Patient.get({id: id}, function (result) {
                        $scope.patient = result;
                    });
                }
            };
            $scope.initBirthDate = function () {
                $scope.maxDateValue = moment(new Date()).utc();
            };
            $scope.initBirthDate();

            var onSaveFinished = function (result) {
                $scope.$emit('windoctorApp:patientUpdate', result);
                $modalInstance.close(result);
            };
            var onSaveFailed = function (response) {
                if (response.status === 400 && response.data.code === 'U-02') {
                    $scope.errorUserExists = 'ERROR';
                } else if (response.status === 400 && response.data.code === 'U-01') {
                    $scope.errorEmailExists = 'ERROR';
                } else {
                    $scope.error = 'ERROR';
                }
            };

            $scope.changeDate = function (modelName, newDate) {
                console.log(modelName + ' has had a date change. New value is TEST  ' + newDate.format());
                console.log(modelName + ' has had a date change. New value is ' + newDate.format().split('T')[0] + 'T00:00:00+00:00');
                $scope.dateValue = moment(new Date(newDate.format().split('T')[0] + 'T00:00:00+00:00')).utc();
                console.log(modelName + ' has had a date change. $scope.dateValue ' + new Date(moment(new Date($scope.dateValue)).utc()));
            }

            $scope.save = function () {
                if ($scope.password !== $scope.confirmPassword) {
                    $scope.doNotMatch = 'ERROR';
                } else {
                    $scope.patient.birthDate = new Date($scope.dateValue);
                    $scope.patient.password = $scope.password;
                    if ($scope.patient.id != null) {
                        Patient.update($scope.patient, onSaveFinished, onSaveFailed);
                    } else {
                        if($scope.password!=='Empty_Password_1'){
                            Patient.save($scope.patient, onSaveFinished, onSaveFailed);
                        }else{
                            $scope.doNotMatch = 'ERROR';
                            $scope.password=null;
                            $scope.confirmPassword=null;
                        }
                    }
                }
            };

            $scope.changeBlockStatus = function(){
                $scope.patient.blocked = !$scope.patient.blocked;
                console.log('$scope.patient.blocked '+$scope.patient.blocked);
            }

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
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

            $scope.setPicture = function ($files, patient) {
                if ($files[0]) {
                    var file = $files[0];
                    var fileReader = new FileReader();
                    fileReader.readAsDataURL(file);
                    fileReader.onload = function (e) {
                        var data = e.target.result;
                        var base64Data = data.substr(data.indexOf('base64,') + 'base64,'.length);
                        $scope.$apply(function () {
                            patient.picture = base64Data;
                        });
                    };
                }
            };

            /********************************************************************************/
            /********************************************************************************/
            /***********************                                       ******************/
            /***********************      Manage live capture image        ******************/
            /***********************                                       ******************/
            /********************************************************************************/
            /********************************************************************************/
            /********************************************************************************/

            $scope.captureAnImage = function () {
                $scope.captureAnImageScreen = true;
            };
            $scope.cancelImageCapture = function () {
                $scope.captureAnImageScreen = false;
            };

            var _video = null,
                patData = null;

            $scope.patOpts = {x: 0, y: 0, w: 25, h: 25};

            // Setup a channel to receive a video property
            // with a reference to the video element
            // See the HTML binding in main.html
            $scope.channel = {};

            $scope.webcamError = false;
            $scope.onError = function (err) {
                $scope.$apply(
                    function () {
                        $scope.webcamError = err;
                    }
                );
            };

            $scope.onSuccess = function () {
                // The video element contains the captured camera data
                _video = $scope.channel.video;
                $scope.$apply(function () {
                    $scope.patOpts.w = _video.width;
                    $scope.patOpts.h = _video.height;
                    //$scope.showDemos = true;
                });
            };

            $scope.onStream = function (stream) {
                // You could do something manually with the stream.
            };

            $scope.makeSnapshot = function () {
                if (_video) {
                    var patCanvas = document.querySelector('#snapshot');
                    if (!patCanvas) return;

                    patCanvas.width = _video.width;
                    patCanvas.height = _video.height;
                    var ctxPat = patCanvas.getContext('2d');

                    var idata = getVideoData($scope.patOpts.x, $scope.patOpts.y, $scope.patOpts.w, $scope.patOpts.h);
                    ctxPat.putImageData(idata, 0, 0);

                    sendSnapshotToServer(patCanvas.toDataURL("image/png").replace("image/png", "image/octet-stream"));

                    patData = idata;
                    $scope.captureAnImageScreen = false;
                }
            };

            /**
             * Redirect the browser to the URL given.
             * Used to download the image by passing a dataURL string
             */
            $scope.downloadSnapshot = function downloadSnapshot(dataURL) {
                window.location.href = dataURL;
            };

            var getVideoData = function getVideoData(x, y, w, h) {
                var hiddenCanvas = document.createElement('canvas');
                hiddenCanvas.width = _video.width;
                hiddenCanvas.height = _video.height;
                var ctx = hiddenCanvas.getContext('2d');
                ctx.drawImage(_video, 0, 0, _video.width, _video.height);
                return ctx.getImageData(x, y, w, h);
            };

            /**
             * This function could be used to send the image data
             * to a backend server that expects base64 encoded images.
             *
             * In this example, we simply store it in the scope for display.
             */
            var sendSnapshotToServer = function sendSnapshotToServer(imgBase64) {
                $scope.setPicture([dataURItoBlob(imgBase64)], $scope.patient);

            };

            function dataURItoBlob(dataURI) {
                // convert base64/URLEncoded data component to raw binary data held in a string
                var byteString;
                if (dataURI.split(',')[0].indexOf('base64') >= 0)
                    byteString = atob(dataURI.split(',')[1]);
                else
                    byteString = unescape(dataURI.split(',')[1]);

                // separate out the mime component
                var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

                // write the bytes of the string to a typed array
                var ia = new Uint8Array(byteString.length);
                for (var i = 0; i < byteString.length; i++) {
                    ia[i] = byteString.charCodeAt(i);
                }

                return new Blob([ia], {type: mimeString});
            }
        }]);
