'use strict';

describe('MailSetting Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockMailSetting, MockStructure;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockMailSetting = jasmine.createSpy('MockMailSetting');
        MockStructure = jasmine.createSpy('MockStructure');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'MailSetting': MockMailSetting,
            'Structure': MockStructure
        };
        createController = function() {
            $injector.get('$controller')("MailSettingDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'windoctorApp:mailSettingUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
