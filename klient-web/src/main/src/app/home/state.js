var mod = angular.module('nha.state', []);

mod.factory('stateService', ['$http', stateService]);

function stateService($http) {
    var sokState = {};
    
    this.sokState = function() {
        return sokState;
    };

    return {
        sokState : sokState
    };
}
