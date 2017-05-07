package br.com.aurum.astrea.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;

import br.com.aurum.astrea.domain.Contact;
import br.com.aurum.astrea.filter.ContactFilter;

public class ContactDao extends AbstractDao<Contact> {
	
	static {
		ObjectifyService.register(Contact.class);
	}
	
	public Long save(Contact contact) {
		Key<Contact> persistContact = persist(contact);
		return persistContact.getId();
	}
	
	public Contact getContactById(Long idContact){
		return getEntityById(idContact);
	}

	public List<Contact> list() {
		return listEntities(); 
	}
	
	public void delete(Long contactId) {
		deleteEntityById(contactId);
	}
	
	public List<Contact> listByFilter(ContactFilter contactFilter){
		LoadType<Contact> loadType = ObjectifyService.ofy().load().type(Contact.class);
		String name = contactFilter.getName();
		String cpf = contactFilter.getCpf();
		String email = contactFilter.getEmail();
		Query<Contact> query = loadType.chunkAll();
		if(StringUtils.isNotEmpty(name)){
			query = query.filter("name >= ", name).filter("name <", name + "\uFFFD");
		}
		if(StringUtils.isNotEmpty(cpf)){
			query = query.filter("cpf", cpf );
		}
		if(StringUtils.isNotEmpty(email)){
			query = query.filter("emails in", Arrays.asList(email));
		}
		return query.list();
	}

	@Override
	Class<Contact> getTClass() {
		return Contact.class;
	}
}
