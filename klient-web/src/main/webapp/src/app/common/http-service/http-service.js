var mod = angular.module('nha.common.http-service', [
]);

mod.factory('httpService', ['$http', httpService]);

function httpService($http) {
	var url = "http://localhost:8080/api/";

	//CREATE
	function ny(relativUrl, data) {
		return $http({
			accept: "application/json",
			method: "POST",
			url: url + relativUrl,
			data: data
		});
	}

	//READ
	function hentAlle(relativUrl, cache) {
		return $http({
			cache: cache,
			accept: "application/json",
			method: "GET",
			url: url + relativUrl
		});
	}

	function hent(relativUrl) {
		return $http({
			accept: "application/json",
			method: "GET",
			url: url + relativUrl
		});	
	}

	//UPDATE
	function oppdater(relativUrl, data) {
		return $http({
			accept: "application/json",
			method: "PUT",
			url: url + relativUrl,
			data: data
		});	
	}

	//DELETE
	function deleteElement(relativUrl) {
		return $http({
			accept: "application/json",
			method: "DELETE",
			url: url + relativUrl
		});
	}

	//ANDRE
	function genererAvlevering(id) {
		return $http({
			accept: "application/json",
			method: "GET",
			url: url + "avleveringer/generer"
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
		ny: ny,
		hentAlle: hentAlle,
		hent: hent,
		oppdater: oppdater,
		deleteElement: deleteElement,

		genererAvlevering: genererAvlevering,
		getSokeresultat: getSokeresultat

	};
}