var contactAddEditController;

contactAddEditController = function($scope, $http, $stateParams, contactService) {
	$scope.contact = {};
	$scope.contact.emails = [''];
	$scope.contact.phones = [''];
	$scope.submitted = false;
	
	if($stateParams.contactId){
		contactService.getContact($stateParams.contactId)
		.success(function(data) {
			  $scope.contact = data;
		})
		.error(function(data, status) {
		  console.log('Ocorreu um erro', status, data);
		});	
	}
	
	$scope.save = function() {
		$scope.submitted = true;

		if ($scope.contact.name != null && $scope.contact.name != "") {

			contactService.saveContact($scope.contact)
			.success(function(data) {
				 if(!$scope.contact.id){
					 alert("Contato cadastrado com sucesso");
				 }else{
					 alert("Contato atualizado com sucesso");
				 }
				 $scope.contact = data;
			})
			.error(function(data, status) {
			  console.log('Ocorreu um erro', status, data);
			});	
		}

	};

	$scope.addMorePhones = function() {
		$scope.contact.phones.push('');
	}; 

	$scope.addMoreEmails = function() {
		$scope.contact.emails.push('');
	};

	$scope.deletePhone = function(index){
		if (index > -1) {
    		$scope.contact.phones.splice(index, 1);
		}

		if ($scope.contact.phones.length < 1){
			$scope.addMorePhones();
		}
	};

	$scope.deleteEmail = function(index){
		if (index > -1) {
    		$scope.contact.emails.splice(index, 1);
		}

		if ($scope.contact.emails.length < 1){
			$scope.addMoreEmails();
		}
	};

};

angular.module('avaliacandidatos').controller("contactAddEditController", contactAddEditController);