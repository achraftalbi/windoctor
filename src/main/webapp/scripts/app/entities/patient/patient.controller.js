'use strict';

angular.module('windoctorApp')
    .controller('PatientController', function ($scope, Patient, PatientSearch, ParseLinks) {
        $scope.patients = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Patient.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.patients = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Patient.get({id: id}, function(result) {
                $scope.patient = result;
                $('#deletePatientConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Patient.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePatientConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            PatientSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.patients = result;
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
            $scope.patient = {login: null, password: null, firstName: null, lastName: null, email: null, activated: null, blocked: null, picture: null, id: null};
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
    });