var contactService;

contactService = function($http) {
	var url = "/contacts";
	
	this.saveContact = function(contact){
		return $http.post(url, contact);
	}
	
	this.getContact = function(contactId){
		return $http.get(url,{params: {action:'get',id:contactId}});
	}
	
	this.listContact = function(contactFilter){
		return $http.get(url, {params: {action:'list',name:contactFilter.name, email:contactFilter.email, cpf:contactFilter.cpf}});
	}
	
	this.deleteContact = function(contactId){
		return $http.delete(url, {params: {id:contactId}});
	}
};

angular.module('avaliacandidatos').service("contactService", contactService);