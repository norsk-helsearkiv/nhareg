var mod = angular.module('nha.common.authentication-service', [
    'ngCookies',
    'nha.common.http-service'
]);

mod.factory('authenticationService', ['$http', '$cookieStore', '$rootScope', 'httpService', authenticationService]);

function authenticationService($http, $cookieStore, $rootScope, httpService) {
    var service = {};
    var authFactory = {
        authData: undefined
    };

    authFactory.login = function (user) {
        return httpService.login(user);
    };
    authFactory.setAuthData = function (authData) {
        this.authData = {
            authId: authData.authId,
            authToken: authData.authToken,
            authPermission: authData.authPermission
        };
        $rootScope.$broadcast('authChanged');
    };

    authFactory.getAuthData = function () {
        return this.authData;
    };

    authFactory.isAuthenticated = function () {
        return !angular.isUndefined(this.getAuthData());
    };

    return authFactory;
}
