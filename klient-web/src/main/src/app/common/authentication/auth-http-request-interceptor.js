var mod = angular.module('nha.common.auth-http-request-interceptor', [
    'ngCookies',
    'nha.common.http-service'
]);

mod.factory('authHttpRequestInterceptor', ['$rootScope', '$injector', function ($rootScope, $injector) {
    var authHttpRequestInterceptor = {
        request: function ($request) {
            var authFactory = $injector.get('authenticationService');
            if (authFactory.isAuthenticated()) {
                $request.headers['auth-id'] = authFactory.getAuthData().authId;
                $request.headers['auth-token'] = authFactory.getAuthData().authToken;
            }
            return $request;
        }
    };

    return authHttpRequestInterceptor;
}]);

mod.config(function ($httpProvider) {
    $httpProvider.interceptors.push('authHttpRequestInterceptor');
});
