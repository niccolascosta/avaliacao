var contactService;

contactService = function($http) {
	var url = "/contacts";
	
	this.saveContact = function(contact){
		return $http.post(url, contact);
	}
	
	this.getContact = function(idContact){
		return $http.get(url,{params: {action:'get',id:idContact}});
	}
	
	this.listContact = function(contactFilter){
		return $http.get(url, {params: {action:'list',name:contactFilter.name, email:contactFilter.email, cpf:contactFilter.cpf}});
	}
};

angular.module('avaliacandidatos').service("contactService", contactService);