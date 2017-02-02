'use strict';
angular.module('windoctorApp', ['LocalStorageModule', 'tmh.dynamicLocale', 'pascalprecht.translate',
               'mwl.calendar', // Add calendar to my application
               'ui.bootstrap', // for modal dialogs
               'chart.js', //For include chard in application
            //Used for inclufing loading bar
               'ui.sortable',
               'ngLocale',
               'ncy-angular-breadcrumb',
                'datePicker','ui.select','ngSanitize','webcam',
    'ngResource', 'ui.router', 'ngCookies', 'ngCacheBuster', 'ngFileUpload', 'infinite-scroll' ,'ngAnimate', 'angular-loading-bar'])

    .run(function ($rootScope, $location, $window, $http, $state, $translate, Language, Auth, Principal, ENV, VERSION) {
        $rootScope.ENV = ENV;
        $rootScope.VERSION = VERSION;
        $rootScope.elements=[

            // Top right Big
            {id:28,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3,heightDraw:1/3,xDraw:200,yDraw:-250,zRotation:40,width:31,height:23,x:253,y:166},
            {id:27,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3,heightDraw:1/3,xDraw:210,yDraw:-215,zRotation:30,width:29,height:20,x:250,y:139},
            {id:26,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3+1/40,heightDraw:1/3+1/40,xDraw:158,yDraw:-239,zRotation:37,width:27,height:16,x:242,y:111},
            {id:25,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3,heightDraw:1/3,xDraw:134,yDraw:-236,zRotation:37,width:21,height:16,x:235,y:90},
            {id:24,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3,heightDraw:1/3,xDraw:132,yDraw:-215,zRotation:31,width:37,height:21,x:224,y:68},
            {id:23,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/20,heightDraw:1/3-1/20,xDraw:172,yDraw:-137,zRotation:6,width:22,height:21,x:209,y:44},
            {id:22,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/13,heightDraw:1/3-1/13,xDraw:172,yDraw:-80,zRotation:-11,width:20,height:19,x:187,y:26},
            {id:21,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/20,heightDraw:1/3-1/20,xDraw:153,yDraw:5,zRotation:-40,width:20,height:23,x:160,y:10},
            // Top children
            {id:65,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/15,heightDraw:1/3-1/15,xDraw:205,yDraw:160,zRotation:-40,width:38,height:18,x:213,y:169},
            {id:64,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/15,heightDraw:1/3-1/15,xDraw:149,yDraw:188,zRotation:-56,width:22,height:17,x:209,y:142},
            {id:63,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/15,heightDraw:1/3-1/15,xDraw:221,yDraw:-77,zRotation:6,width:18,height:14,x:199,y:121},
            {id:62,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/13,heightDraw:1/3-1/13,xDraw:203,yDraw:-42,zRotation:-3,width:17,height:17,x:183,y:101},
            {id:61,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/20,heightDraw:1/3-1/20,xDraw:153,yDraw:83,zRotation:-40,width:16,height:26,x:160,y:87},
            {id:51,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/20,heightDraw:1/3-1/20,xDraw:125,yDraw:82,zRotation:-40,width:16,height:24,x:133,y:87},
            {id:52,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/13,heightDraw:1/3-1/13,xDraw:48,yDraw:132,zRotation:-65,width:17,height:18,x:111,y:98},
            {id:53,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/15,heightDraw:1/3-1/15,xDraw:-35,yDraw:143,zRotation:-87,width:23,height:17,x:91,y:116},
            {id:54,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/15,heightDraw:1/3-1/15,xDraw:-155,yDraw:85,zRotation:-125,width:31,height:18,x:78,y:140},
            {id:55,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/15,heightDraw:1/3-1/15,xDraw:-182,yDraw:83,zRotation:-125,width:28,height:22,x:73,y:166},
            // Bottom children
            {id:75,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/40,heightDraw:1/3-1/40,xDraw:120,yDraw:-292,zRotation:67,width:29,height:24,x:209,y:199},
            {id:74,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/40,heightDraw:1/3-1/40,xDraw:31,yDraw:-330,zRotation:90,width:25,height:25,x:198,y:225},
            {id:73,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/15,heightDraw:1/3-1/15,xDraw:48,yDraw:-331,zRotation:92,width:20,height:18,x:186,y:250},
            {id:72,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/10,heightDraw:1/3-1/10,xDraw:-112,yDraw:-320,zRotation:125,width:10,height:18,x:173,y:263},
            {id:71,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/10,heightDraw:1/3-1/10,xDraw:-172,yDraw:-289,zRotation:140,width:10,height:20,x:158,y:268},
            {id:81,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/7-1/130,heightDraw:1/3-1/7-1/130,xDraw:-152,yDraw:-289,zRotation:140,width:12,height:20,x:138,y:269},
            {id:82,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/8-1/130,heightDraw:1/3-1/8-1/130,xDraw:-200,yDraw:-243,zRotation:155,width:10,height:19,x:121,y:264},
            {id:83,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/15,heightDraw:1/3-1/15,xDraw:-270,yDraw:-121,zRotation:185,width:19,height:16,x:98,y:251},
            {id:84,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/25,heightDraw:1/3-1/25,xDraw:-266,yDraw:8,zRotation:215,width:28,height:15,x:78,y:231},
            {id:85,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/17,heightDraw:1/3-1/17,xDraw:-238,yDraw:8,zRotation:215,width:28,height:18,x:71,y:201},
            // Bottom right Big
            {id:38,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/2,heightDraw:1/2,xDraw:170,yDraw:-303,zRotation:55,width:28,height:22,x:253,y:199},
            {id:37,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/2,heightDraw:1/2,xDraw:147,yDraw:-333,zRotation:65,width:30,height:19,x:245,y:230},
            {id:36,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/2-1/40,heightDraw:1/2-1/40,xDraw:176,yDraw:-333,zRotation:65,width:32,height:25,x:238,y:257},
            {id:35,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/2-1/40,heightDraw:1/2-1/40,xDraw:180,yDraw:-349,zRotation:70,width:30,height:20,x:231,y:286},
            {id:34,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/2-1/40,heightDraw:1/2-1/40,xDraw:187,yDraw:-356,zRotation:73,width:27,height:12,x:217,y:311},
            {id:33,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/2-1/10,heightDraw:1/2-1/10,xDraw:37,yDraw:-415,zRotation:100,width:24,height:16,x:208,y:335},
            {id:32,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/2-1/10,heightDraw:1/2-1/10,xDraw:-73,yDraw:-417,zRotation:120,width:18,height:23,x:186,y:346},
            {id:31,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/2-1/20,heightDraw:1/2-1/20,xDraw:-183,yDraw:-380,zRotation:140,width:17,height:31,x:163,y:346},
            // Bottom left Big
            {id:41,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/2-1/20,heightDraw:1/2-1/20,xDraw:-154,yDraw:-380,zRotation:140,width:15,height:31,x:134,y:349},
            {id:42,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/2-1/10,heightDraw:1/2-1/10,xDraw:-244,yDraw:-310,zRotation:160,width:17,height:25,x:109,y:344},
            {id:43,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/2-1/20,heightDraw:1/2-1/20,xDraw:-321,yDraw:-190,zRotation:185,width:16,height:27,x:84,y:323},
            {id:44,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/2-1/7,heightDraw:1/2-1/7,xDraw:-326,yDraw:-112,zRotation:200,width:16,height:17,x:70,y:312},
            {id:45,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/2-1/7,heightDraw:1/2-1/7,xDraw:-301,yDraw:-108,zRotation:200,width:30,height:20,x:59,y:290},
            {id:46,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/2-1/8,heightDraw:1/2-1/8,xDraw:-287,yDraw:-58,zRotation:210,width:25,height:31,x:47,y:260},
            {id:47,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/2-1/8,heightDraw:1/2-1/8,xDraw:-261,yDraw:-18,zRotation:220,width:29,height:18,x:31,y:234},
            {id:48,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/2-1/8,heightDraw:1/2-1/8,xDraw:-230,yDraw:-15,zRotation:220,width:29,height:19,x:29,y:202},
            // Top left Big
            {id:18,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3+1/25,heightDraw:1/3+1/25,xDraw:-175,yDraw:60,zRotation:-118,width:23,height:23,x:32,y:162},
            {id:17,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3,heightDraw:1/3,xDraw:-136,yDraw:72,zRotation:-112,width:21,height:17,x:37,y:136},
            {id:16,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3+1/40,heightDraw:1/3+1/40,xDraw:-115,yDraw:64,zRotation:-115,width:22,height:21,x:43,y:107},
            {id:15,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3,heightDraw:1/3,xDraw:-77,yDraw:70,zRotation:-110,width:11,height:15,x:53,y:85},
            {id:14,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3,heightDraw:1/3,xDraw:-40,yDraw:77,zRotation:-103,width:18,height:15,x:63,y:62},
            {id:13,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/20,heightDraw:1/3-1/20,xDraw:22,yDraw:78,zRotation:-80,width:20,height:15,x:79,y:40},
            {id:12,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/13,heightDraw:1/3-1/13,xDraw:81,yDraw:54,zRotation:-60,width:20,height:15,x:105,y:25},
            {id:11,img:null,img_green:null,img_red:null,img_blue:null,img_yellow:null,img_orange:null,widthDraw:1/3-1/20,heightDraw:1/3-1/20,xDraw:123,yDraw:5,zRotation:-40,width:19,height:23,x:131,y:10}

        ];
        var typeImg = '.png';
        var pathNormalImage = 'images/elements/teethschame';
        var green = '_green';
        var red = '_red';
        var blue = '_blue';
        var yellow = '_yellow';
        var orange = '_orange';
        for(var i=0;i<$rootScope.elements.length;i++){
            var img = new Image(),img_green = new Image(),img_red = new Image(),
                img_blue = new Image(),img_yellow = new Image(),img_orange = new Image();
            img.src = pathNormalImage+'/'+$rootScope.elements[i].id+typeImg;
            img_green.src = pathNormalImage+green+'/'+$rootScope.elements[i].id+green+typeImg;
            img_red.src = pathNormalImage+red+'/'+$rootScope.elements[i].id+red+typeImg;
            img_blue.src = pathNormalImage+blue+'/'+$rootScope.elements[i].id+blue+typeImg;
            img_yellow.src = pathNormalImage+yellow+'/'+$rootScope.elements[i].id+yellow+typeImg;
            img_orange.src = pathNormalImage+orange+'/'+$rootScope.elements[i].id+orange+typeImg;
            $rootScope.elements[i].img = img;
            $rootScope.elements[i].img_green = img_green;
            $rootScope.elements[i].img_red = img_red;
            $rootScope.elements[i].img_blue = img_blue;
            $rootScope.elements[i].img_orange = img_orange;
            $rootScope.elements[i].img_yellow = img_yellow;
        }

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
