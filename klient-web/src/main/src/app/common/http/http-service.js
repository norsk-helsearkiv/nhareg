var mod = angular.module('nha.common.http-service', [
]);

mod.factory('httpService', ['$http', httpService]);

function httpService($http) {
	var url = "assets/filer/";

	function getAvtaler() {
		return $http({
			cache: true,
			accept: "application/json",
			method: "GET",
			url: url + "avtaler"
		});
	}

	//TODO: fjern denne om ikke den trengs
	/*
	function getAvleveringer(id) {
		return $http({
			cache: true,
			accept: "application/json",
			method: "GET",
			url: url + "avleveringer/" + id
		});
	}*/

	return {

		getAvtaler: getAvtaler
		//getAvleveringer : getAvleveringer(id)

	};
}