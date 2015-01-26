var mod = angular.module('nha.common.list-service', [
]);

mod.factory('listService', ['$http', listService]);

function listService($http) {
	var tittel;
	var data;

	function init(t, d) {
		tittel = t;
		data = d;
	}

	function getTittel() {
		return tittel;
	}

	function getData() {
		return data;
	}

	return {
		init: init,
		
		getTittel : getTittel,
		getData: getData
	};
}