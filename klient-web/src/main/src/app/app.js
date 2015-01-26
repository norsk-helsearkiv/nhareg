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
  'nha.home',
  'nha.login',
  'nha.registrering'
])

.config( function myAppConfig ($stateProvider, $urlRouterProvider, $translateProvider) {
  $urlRouterProvider.otherwise( '/' );

	$translateProvider.useStaticFilesLoader({
		prefix: 'assets/i18n/',
		suffix: '.json'
	});
	$translateProvider.preferredLanguage('nb');
})


.controller( 'AppCtrl', function AppCtrl ( $scope, $location ) {
});