'use strict';
angular.module('windoctorApp', ['LocalStorageModule', 'tmh.dynamicLocale', 'pascalprecht.translate',
               'mwl.calendar', // Add calendar to my application
               'ui.bootstrap', // for modal dialogs
               'chart.js', //For include chard in application
            //Used for inclufing loading bar
               'ngLocale',
               'ncy-angular-breadcrumb',
                'datePicker','ui.select','ngSanitize','webcam',
    'ngResource', 'ui.router', 'ngCookies', 'ngCacheBuster', 'ngFileUpload', 'infinite-scroll' ,'ngAnimate', 'angular-loading-bar'])

    .run(function ($rootScope, $location, $window, $http, $state, $translate, Language, Auth, Principal, ENV, VERSION) {
        $rootScope.ENV = ENV;
        $rootScope.VERSION = VERSION;
        $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams) {
            $rootScope.toState = toState;
            $rootScope.toStateParams = toStateParams;

            if (Principal.isIdentityResolved()) {
                Auth.authorize();
            }

            // Update the language
            Language.getCurrent().then(function (language) {
                $translate.use(language);
            });

        });

        $rootScope.$on('$stateChangeSuccess',  function(event, toState, toParams, fromState, fromParams) {
            var titleKey = 'global.title' ;

            $rootScope.previousStateName = fromState.name;
            $rootScope.previousStateParams = fromParams;

            // Set the page title key to the one configured in state or use default one
            if (toState.data.pageTitle) {
                titleKey = toState.data.pageTitle;
            }

            $translate(titleKey).then(function (title) {
                // Change window title with translated one
                $window.document.title = title;
            });

        });

        $rootScope.back = function() {
            // If previous state is 'activate' or do not exist go to 'home'
            if ($rootScope.previousStateName === 'activate' || $state.get($rootScope.previousStateName) === null) {
                $state.go('home');
            } else {
                $state.go($rootScope.previousStateName, $rootScope.previousStateParams);
            }
        };
        $rootScope.isUndefinedOrNull = function(val) {
            return angular.isUndefined(val) || val === null;
        }
    })
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider, $translateProvider, tmhDynamicLocaleProvider, httpRequestInterceptorCacheBusterProvider) {

        //enable CSRF
        $httpProvider.defaults.xsrfCookieName = 'CSRF-TOKEN';
        $httpProvider.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';

        //Cache everything except rest api requests
        httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*api.*/, /.*protected.*/], true);

        $urlRouterProvider.otherwise('/');
        $stateProvider.state('site', {
            'abstract': true,
            views: {
                'navbar@': {
                    templateUrl: 'scripts/components/navbar/navbar.html',
                    controller: 'NavbarController'
                }
            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                }]
            }
        });

        $httpProvider.interceptors.push('errorHandlerInterceptor');
        $httpProvider.interceptors.push('authExpiredInterceptor');
        $httpProvider.interceptors.push('notificationInterceptor');

        // Initialize angular-translate
        $translateProvider.useLoader('$translatePartialLoader', {
            urlTemplate: 'i18n/{lang}/{part}.json'
        });

        $translateProvider.preferredLanguage('en');
        $translateProvider.useCookieStorage();
        $translateProvider.useSanitizeValueStrategy('escaped');
        $translateProvider.addInterpolation('$translateMessageFormatInterpolation');

        tmhDynamicLocaleProvider.localeLocationPattern('bower_components/angular-i18n/angular-locale_{{locale}}.js');
        tmhDynamicLocaleProvider.useCookieStorage();
        tmhDynamicLocaleProvider.storageKey('NG_TRANSLATE_LANG_KEY');

    })
    .config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.latencyThreshold = 1;
    }])
    /*.config(['calendarConfig', function(calendarConfig) {
        calendarConfig.showTimesOnWeekView = true;
    }])*/
    .config(function($breadcrumbProvider) {
        $breadcrumbProvider.setOptions({
            translations: true,
            template: '<ol class="breadcrumb">'+
            '<div style="font-size:18px;">' +
                '<span ng-repeat="step in steps" >' +
            '<a  ng-bind-html="step.ncyBreadcrumbLabel.replace(step.ncyBreadcrumbLabel.split(\'>\')[2],\'\')" href="{{step.ncyBreadcrumbLink}}" ng-show="!$last"></a>&nbsp;<a href="{{step.ncyBreadcrumbLink}}" ng-show="!$last">{{step.ncyBreadcrumbLabel.split(\'>\')[2]|translate}}</a><span  ng-show="!$last">&nbsp;&nbsp;>&nbsp;</span>'+
            '<span  ng-bind-html="step.ncyBreadcrumbLabel.replace(step.ncyBreadcrumbLabel.split(\'>\')[2],\'\')" ng-show="$last"></span>&nbsp;<span ng-show="$last" >{{step.ncyBreadcrumbLabel.split(\'>\')[2]|translate}}&nbsp;</span>'+
                '</span>' +
            '</div>'+
            '</ol>'

        });
    }
);
angular.module('windoctorApp').factory('filteredListService', function () {


    this.searched = function (valLists,toSearch) {
        return _.filter(valLists,
            function (i) {
                /* Search Text in all 3 fields */
                return searchUtil(i, toSearch);
            });
    };

    this.paged = function (valLists,pageSize)
    {
        var retVal = [];
        for (var i = 0; i < valLists.length; i++) {
            if (i % pageSize === 0) {
                retVal[Math.floor(i / pageSize)] = [valLists[i]];
            } else {
                retVal[Math.floor(i / pageSize)].push(valLists[i]);
            }
        }
        return retVal;
    };

});
