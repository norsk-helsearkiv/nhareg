angular.module('nha', [
    'templates-app',
    'templates-common',
    'ui.router',
    'cfp.hotkeys',
    'tableSort',
    'underscore',
    'pascalprecht.translate',
    'nha.common.error-service',
    'nha.common.http-service',
    'nha.common.modal-service',
    'nha.common.list-service',
    'nha.common.list-view',
    'nha.common.diagnosis-service',
    'nha.home',
    'nha.state',
    'nha.register',
    'nha.register.register-service',
    'ngCookies',
    'ngIdle'
])

    .config(function myAppConfig($stateProvider, $urlRouterProvider, $translateProvider, $httpProvider, IdleProvider, KeepaliveProvider) {

        $urlRouterProvider.otherwise('/');
        $httpProvider.defaults.withCredentials = true;

        $translateProvider.useStaticFilesLoader({
            prefix: 'assets/i18n/',
            suffix: '.json'
        });
        $translateProvider.preferredLanguage('nb');

        IdleProvider.idle(10); //idle starts after 10 seconds.
        IdleProvider.timeout(30*60); //after 30 minutes idle, time the user out
        KeepaliveProvider.interval(30); //10 sec ping interval for keep-alive ping
    })

    .directive('ngEnter', function () {
        return function (scope, element, attrs) {
            element.bind("keydown keypress", function (event) {
                if (event.which === 13) {
                    scope.$apply(function () {
                        scope.$eval(attrs.ngEnter);
                    });

                    event.preventDefault();
                }
            });
        };
    })

    .directive('favoriteClass', [function() {

        return {
            restrict: 'A',
            scope: {
                avlevering: '='
            },
            link: function(scope, element, attrs, controller) {
                scope.$watch('favorites', function() {
                    element.removeClass('favorite');
                    element.removeClass('favoriteActive');
                    if (scope.avlevering.defaultAvlevering) {
                        element.addClass('favoriteActive');
                    }else{
                        element.addClass('favorite');
                    }
                }, true);
            }
        };
    }])

    .directive('focusOnShow', function($timeout) {
        return {
            restrict: 'A',
            link: function($scope, $element, $attr) {
                if ($attr.ngShow){
                    $scope.$watch($attr.ngShow, function(newValue){
                        if(newValue){
                            $timeout(function(){
                                $element[0].focus();
                            }, 0);
                        }
                    });
                }
                if ($attr.ngHide){
                    $scope.$watch($attr.ngHide, function(newValue){
                        if(!newValue){
                            $timeout(function(){
                                $element[0].focus();
                            }, 0);
                        }
                    });
                }
            }
        };
    })

    .directive('focusToMe', function($timeout) {
        return {
            restrict: 'A',
            compile: function() {
                var directiveName = this.name;

                return function(scope, elem, attrs) {
                    scope.$watch(attrs[directiveName], function(newVal, oldVal) {
                        if (newVal) {
                            $timeout(function() {
                                elem[0].focus();
                            }, 0);
                        }
                    });
                };
            }
        };
    })

    .directive('accessLevelAdmin', ['$rootScope', function($rootScope) {
        return {
            restrict: 'A',
            link: function($scope, element, attrs) {
                var prevDisp = element.css('display');
                $rootScope.$watch('userrole', function(role) {
                    if(role==="admin"){
                        element.css('display', prevDisp);
                    }
                    else{
                        element.css('display', 'none');
                    }
                });
            }
        };
    }])

    .directive('fixedHeader', function fixedHeader($timeout) {
        return {
            restrict: 'A',
            link: function link($scope, $elem, $attrs, $ctrl) {
                var elem = $elem[0];

                // wait for data to load and then transform the table
                $scope.$watch(tableDataLoaded, function(isTableDataLoaded) {
                    if (isTableDataLoaded) {
                        transformTable();
                    }
                });

                function tableDataLoaded() {
                    // first cell in the tbody exists when data is loaded but doesn't have a width
                    // until after the table is transformed
                    var firstCell = elem.querySelector('tbody tr:first-child td:first-child');
                    return firstCell && !firstCell.style.width;
                }

                function transformTable() {
                    // reset display styles so column widths are correct when measured below
                    angular.element(elem.querySelectorAll('thead, tbody, tfoot')).css('display', '');

                    // wrap in $timeout to give table a chance to finish rendering
                    $timeout(function () {
                        // set widths of columns
                        angular.forEach(elem.querySelectorAll('tr:first-child th'), function (thElem, i) {

                            var tdElems = elem.querySelector('tbody tr:first-child td:nth-child(' + (i + 1) + ')');
                            var tfElems = elem.querySelector('tfoot tr:first-child td:nth-child(' + (i + 1) + ')');

                            var columnWidth = tdElems ? tdElems.offsetWidth : thElem.offsetWidth;
                            if (tdElems) {
                                tdElems.style.width = columnWidth + 'px';
                            }
                            if (thElem) {
                                thElem.style.width = columnWidth + 'px';
                            }
                            if (tfElems) {
                                tfElems.style.width = columnWidth + 'px';
                            }
                        });

                        // set css styles on thead and tbody
                        angular.element(elem.querySelectorAll('thead, tfoot')).css('display', 'block');

                        angular.element(elem.querySelectorAll('tbody')).css({
                            'display': 'block',
                            'height': $attrs.tableHeight || 'inherit',
                            'overflow': 'auto'
                        });

                        // reduce width of last column by width of scrollbar
                        var tbody = elem.querySelector('tbody');
                        var scrollBarWidth = tbody.offsetWidth - tbody.clientWidth;
                        if (scrollBarWidth > 0) {
                            // for some reason trimming the width by 2px lines everything up better
                            scrollBarWidth -= 2;
                            var lastColumn = elem.querySelector('tbody tr:first-child td:last-child');
                            lastColumn.style.width = (lastColumn.offsetWidth - scrollBarWidth) + 'px';
                        }
                    });
                }
            }
        };
    })

    .run(function(Idle){
        // start watching when the app runs. also starts the Keepalive service by default.
        Idle.watch();
    })
    .controller('AppCtrl', function AppCtrl($http, $scope, $location, diagnosisService, httpService, $window) {
        $scope.events = [];

        $scope.$on('IdleStart', function() {
            //console.log("User entered idle-mode");
            // the user appears to have gone idle
        });

        $scope.$on('IdleWarn', function(e, countdown) {
            //console.log("User is idling");
            // follows after the IdleStart event, but includes a countdown until the user is considered timed out
            // the countdown arg is the number of seconds remaining until then.
            // you can change the title or display a warning dialog from here.
            // you can let them resume their session by calling Idle.watch()
        });

        $scope.$on('IdleTimeout', function() {
            console.log("User timed-out...");
            // the user has timed out (meaning idleDuration + timeout has passed without any activity)
            // this is where you'd log them
            httpService.logout();
            $window.location="logout";
        });

        $scope.$on('IdleEnd', function() {
            //console.log("app.js: IdleEnd");
            // the user has come back from AFK and is doing stuff. if you are warning them, you can use this to hide the dialog
        });

        $scope.$on('Keepalive', function() {
            //console.log("app.js: Keepalive");
            // do something to keep the user's session alive
        });
    });