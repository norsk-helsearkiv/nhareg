var mod = angular.module('nha.common.http-service', [
]);

mod.factory('httpService', ['$http', httpService]);

function httpService() {

	var avtaler = "attriutt data";

	return {

		getAvtaler: function(data) {
			console.log(avtaler);
		}

	};
  
}