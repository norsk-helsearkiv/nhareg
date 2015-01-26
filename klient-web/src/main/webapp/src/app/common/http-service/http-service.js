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
			url: url + "avtaler/avtaler"
		});
	}

	function getAvleveringer(id) {
		return $http({
			cache: true,
			accept: "application/json",
			method: "GET",
			url: url + "avtaler/" + id + "/" + id
		});
	}

	function genererAvlevering(id) {
		return $http({
			accept: "application/json",
			method: "GET",
			url: url + "avleveringer/generer"
		});
	}

	function getPasientjournal(id) {
		return $http({
			accept: "application/json",
			method: "GET",
			url: url + "pasientjournaler/" + id
		});		
	}

	function getPasientjournaler(id) {
		return $http({
			accept: "application/json",
			method: "GET",
			url: url + "avleveringer/" + id + "/pasientjournaler"
		});
	}

	function getSokeresultat(sokestring) {
		return $http({
			accept: "application/json",
			method: "GET",
			url: url + "sok?sokestring=" + sokestring
		});
	}


	return {

		getAvtaler: getAvtaler,
		getAvleveringer : getAvleveringer,
		getPasientjournal : getPasientjournal,
		getPasientjournaler : getPasientjournaler,
		getSokeresultat : getSokeresultat,

		genererAvlevering : genererAvlevering

	};
}