'use strict';

angular.module('windoctorApp')
    .controller('MailSettingController', function ($scope, MailSetting, MailSettingSearch, ParseLinks) {
        $scope.mailSettings = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            MailSetting.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.mailSettings = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            MailSetting.get({id: id}, function(result) {
                $scope.mailSetting = result;
                $('#deleteMailSettingConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            MailSetting.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMailSettingConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            MailSettingSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.mailSettings = result;
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
            $scope.mailSetting = {
                mailOnEventCreation: null,
                mailOnEventCancelation: null,
                mailOnEventEdition: null,
                remaidingBeforeEventMail: null,
                remaidingAfterEventMail: null,
                patientCreationAccountMail: null,
                id: null
            };
        };
    });
