angular.module( 'nha', [
  'templates-app',
  'templates-common',
  'ui.router',
  'pascalprecht.translate',
  'nha.common.error-service',
  'nha.common.http-service',
  'nha.common.modal-service',
  'nha.common.list-service',
  'nha.common.list-view',
  'nha.common.diagnose-service',
  'nha.home',
  'nha.login',
  'nha.registrering',
  'nha.registrering.registrering-service'
])

.config( function myAppConfig ($stateProvider, $urlRouterProvider, $translateProvider) {
  $urlRouterProvider.otherwise( '/' );

	$translateProvider.useStaticFilesLoader({
		prefix: 'assets/i18n/',
		suffix: '.json'
	});
	$translateProvider.preferredLanguage('nb');
})

.directive('ngEnter', function() {
  return function (scope, element, attrs) {
    element.bind("keydown keypress", function (event) {
        if(event.which === 13) {
            scope.$apply(function (){
                scope.$eval(attrs.ngEnter);
            });

            event.preventDefault();
        }
    });
  };
})

.controller( 'AppCtrl', function AppCtrl ($http, $scope, $location, diagnoseService ) {
});
