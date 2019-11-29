var mod = angular.module('nha.common.http-service', [
]);

mod.factory('httpService', ['$http', httpService]);

function httpService($http) {

	var url = "../api/";

	//CREATE
	function create(relativeUrl, data) {
		return $http({
			accept: "application/json",
			method: "POST",
			url: url + relativeUrl,
			data: data
		});
	}

	//READ
	function getAll(relativeUrl, cache) {
		return $http({
			cache: cache,
			accept: "application/json",
			method: "GET",
			url: url + relativeUrl
		});
	}

	function get(relativeUrl) {
		return $http({
			accept: "application/json",
			method: "GET",
			url: url + relativeUrl
		});
	}

	//UPDATE
	function update(relativeUrl, data) {
		return $http({
			accept: "application/json",
			method: "PUT",
			url: url + relativeUrl,
			data: data
		});
	}

	//DELETE
	function deleteElement(relativeUrl) {
		return $http({
			accept: "application/json",
			method: "DELETE",
			url: url + relativeUrl
		});
	}

	function deleteData(relativeUrl, data) {
		return $http({
			accept: "application/json",
			headers: {
				"Content-Type" : "application/json"
			},
			method: "DELETE",
			url: url + relativeUrl,
			data: data
		});
	}

	//OTHER
	function generateDelivery(id) {
		return $http({
			accept: "application/xml",
			method: "GET",
			url: url + "avleveringer/" + id
		});
	}

	function lastUsedStorageUnit(){
		return $http({
			accept: "application/xml",
			method: "GET",
			url: url + "lagringsenheter/sistBrukte"
		});
	}

	function userRole(){
		return $http({
			cache : false,
			accept: "application/json",
			method: "GET",
			url: url + "admin/rolle"
		});
	}

	function userName(){
		return $http({
			cache : false,
			accept: "application/json",
			method: "GET",
			url: url + "admin/bruker"
		});
	}
	function getSearchResult(searchString) {
		return $http({
			accept: "application/json",
			method: "GET",
			url: url + "sok?sokestring=" + searchString
		});
	}

	function logout(){
		return $http({
			accept: "application/json",
			method: "GET",
			url: url + "auth/logout"
		});
	}

	function getRoot() {
		return url;
	}

	return {
		create: create,
		getAll: getAll,
		get: get,
		update: update,
		deleteElement: deleteElement,
		deleteData: deleteData,
		generateDelivery: generateDelivery,
		lastUsedStorageUnit: lastUsedStorageUnit,
		userRole : userRole,
		userName : userName,
		getSearchResult: getSearchResult,
		logout: logout,
		getRoot: getRoot
	};
}