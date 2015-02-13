var mod = angular.module('nha.common.list-service', [
]);

mod.factory('listService', ['$http', listService]);

function listService($http) {
	var tittel;
	var data;
	var avlevering = null;
	var sok = null;

	function init(t, d) {
		tittel = t;
		data = d;
	}

	function setAvlevering(a) {
		avlevering = a;
		sok = null;
	}

	function setSok(s) {
		sok = s;
		avlevering = null;
	}

	function getTittel() {
		return tittel;
	}

	function getData() {
		return data;
	}

	function getQuery() {
		if(avlevering !== null && avlevering !== undefined) {
			return "&avlevering=" + avlevering;
		}
		if(sok !== null && sok !== undefined) {
			return "&sok=" + sok;	
		}
		return '';
	}

	return {
		init: init,

		setAvlevering: setAvlevering,
		setSok: setSok,
		
		getTittel : getTittel,
		getData: getData,
		getQuery: getQuery
	};
}