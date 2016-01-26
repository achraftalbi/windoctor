'use strict';

angular.module('windoctorApp')
    .controller('EventRequestController', function ($scope, Event, ParseLinks) {
        $scope.events = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Event.query({page: $scope.page, per_page: 5}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.events.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.events = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Test6Entity.get({id: id}, function(result) {
                $scope.events = result;
                $('#deleteTest6EntityConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Test6Entity.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteTest6EntityConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.events = {description: null, id: null};
        };
    });
