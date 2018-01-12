var mod = angular.module('nha.common.http-service', [
]);

mod.factory('httpService', ['$http', httpService]);

function httpService($http) {
	var url = "../api/";
    function logout(){
        return $http({
           accept: "application/json",
            method: "GET",
            url: url+"auth/logout"
        });
    }

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

	function slett(relativUrl, data) {
		return $http({
			accept: "application/json",
			headers: {
				"Content-Type" : "application/json"
			},
			method: "DELETE",
			url: url + relativUrl,
			data: data
		});
	}

	//ANDRE
	function genererAvlevering(id) {
		return $http({
			accept: "application/xml",
			method: "GET",
			url: url + "avleveringer/" + id
		});
	}

	function sistBrukteLagringsenhet(){
		return $http({
			accept: "application/xml",
			method: "GET",
			url: url + "lagringsenheter/sistBrukte"
		});
	}

	function brukerRolle(){
		return $http({
			cache : false,
			accept: "application/json",
			method: "GET",
			url: url + "admin/rolle"
		});
	}

	function brukerNavn(){
		return $http({
			cache : false,
			accept: "application/json",
			method: "GET",
			url: url + "admin/bruker"
		});
	}
	function getSokeresultat(sokestring) {
		return $http({
			accept: "application/json",
			method: "GET",
			url: url + "sok?sokestring=" + sokestring
		});
	}

	function getRoot() {
		return url;
	}

	function setCredentials(authdata) {
		$http.defaults.headers.common['Authorization'] = 'Basic ' + authdata;
	}

	return {
        logout: logout,
		ny: ny,
		hentAlle: hentAlle,
		hent: hent,
		oppdater: oppdater,
		deleteElement: deleteElement,
		slett: slett,

		sistBrukteLagringsenhet: sistBrukteLagringsenhet,
		genererAvlevering: genererAvlevering,
		getSokeresultat: getSokeresultat,
		brukerRolle : brukerRolle,
		brukerNavn : brukerNavn,
		getRoot: getRoot,
		setCredentials: setCredentials

	};
}