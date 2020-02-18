var mod = angular.module('nha.state', []);

mod.factory('stateService', [stateService]);

function stateService() {
    var sokState = {};
    
    this.sokState = function() {
        return sokState;
    };

    return {
        sokState : sokState
    };
}