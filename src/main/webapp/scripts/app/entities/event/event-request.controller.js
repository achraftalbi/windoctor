'use strict';

angular.module('windoctorApp')
    .controller('EventRequestController', function ($scope, EventsNotification,Event, ParseLinks) {
        $scope.events = [];
        $scope.page = 1;
        $scope.eventsCount = 0;
        $scope.notificationCountColor=null;
        $scope.loadAll = function() {
            EventsNotification.query({page: $scope.page, per_page: 5}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.events = result;
                $scope.eventsCount = $scope.events.length;
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

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.events = {description: null, id: null};
        };
        $scope.notificationCountColor = function (event) {
            if ($scope.events.length > 0) {
                return { background: "#ff0e1a" }
            }
        };
        $scope.setColor = function (event) {
            if (event.eventStatus.id === 7) {
                return { background: "rgb(255, 170, 4)" }
            }else if (event.eventStatus.id === 10) {
                return { background: "#800080" }
            }
        };
        $scope.accept = function (event) {
            $scope.treatmentFunction(event,1);
        };
        $scope.reject = function (event) {
            $scope.treatmentFunction(event,6);
        };
        $scope.read = function (event) {
            $scope.treatmentFunction(event,11);
        };
        $scope.treatmentFunction = function (event,value) {
            var eventTmp = {};
            angular.copy(event, eventTmp);
            $scope.events.splice($scope.events.indexOf(event), 1);
            eventTmp.eventStatus.id = value;
            $scope.eventsCount = $scope.eventsCount - 1;
            $scope.notificationCountColor();
            $scope.save(eventTmp);

        };
        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:eventUpdate', result);
        };

        $scope.save = function (event) {
            if (event.id != null) {
                Event.update(event, onSaveFinished);
            } else {
                Event.save(event, onSaveFinished);
            }
        };
    });
