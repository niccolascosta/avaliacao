var contactListController;

contactListController = function($scope, $http, contactService) {
	$scope.contacts = [];
	$scope.preDeletedContact = {};
	$scope.contactFilter = {};
	
	$scope.init = function() {
		$scope.listAllContacts();
	};
	
	$scope.listAllContacts = function() {
		contactService.listContact($scope.contactFilter)
		.success(function(data) {
			  $scope.contacts = data;
		})
		.error(function(data, status) {
		  console.log('Ocorreu um erro', status, data);
		});		
	};

	$scope.preDelete = function(contact) {
		$scope.preDeletedContact = contact;
		$('#myModal').modal('show');
	};

	$scope.delete = function() {
		if($scope.preDeletedContact != null) {
			console.log($scope.preDeletedContact);
			// Chamar o servlet /contacts com um método 'DELETE' para deletar um contato do banco de dados passando um parâmetro de identificação.
		}
	};

	$scope.bday = function(c) {
		if(c.birthDay==null || c.birthDay == ""){
			return "";
		} else {
			return c.birthDay + "/" + c.birthMonth + "/" + c.birthYear;
		}
	};
};

angular.module('avaliacandidatos').controller("contactListController", contactListController);