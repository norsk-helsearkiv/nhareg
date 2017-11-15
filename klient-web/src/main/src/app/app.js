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
        'nha.common.diagnose-service',
        'nha.home',
        'nha.state',
        //'nha.login',
        'nha.registrering',
        'nha.registrering.registrering-service'
    ])

    .config(function myAppConfig($stateProvider, $urlRouterProvider, $translateProvider, $httpProvider) {
        $urlRouterProvider.otherwise('/');

        $translateProvider.useStaticFilesLoader({
            prefix: 'assets/i18n/',
            suffix: '.json'
        });
        $translateProvider.preferredLanguage('nb');

        var interceptor = ['$rootScope', '$q', '$window', function (scope, $q, $window) {

            function success(response) {
                return response;
            }

            function error(response) {
                var status = response.status;

                if (status === 403) {
                    var deferred = $q.defer();
                    var req = {
                        config: response.config,
                        deferred: deferred
                    };
                    // Refresh token!
                    $injector.get('AuthenticationFactory').getToken().then(function (token) {
                        response.config.headers.Authorization = token;

                        $http(response.config).then(deferred.resolve, deferred.reject);
                    });

                    return deferred.promise;
                }
                // otherwise
                return $q.reject(response);

            }

            return function (promise) {
                return promise.then(success, error);
            };

        }];
        $httpProvider.responseInterceptors.push(interceptor);
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

    .controller('AppCtrl', function AppCtrl($http, $scope, $location, diagnoseService) {
    });

