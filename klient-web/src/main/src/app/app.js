angular.module( 'nha', [
  'templates-app',
  'templates-common',
  'ui.router',
  'pascalprecht.translate',
  'nha.home'
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