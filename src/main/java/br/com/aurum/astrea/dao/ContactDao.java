package br.com.aurum.astrea.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

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
		Key<Contact> persistContact = this.persist(contact);
		return persistContact.getId();
	}

	public Contact getContactById(Long contactId) {
		if (contactId == null || Objects.equals(contactId, NumberUtils.LONG_ZERO)) {
			return null;
		}
		return this.getEntityById(contactId);
	}

	public List<Contact> list() {
		return this.listEntities();
	}

	public void delete(Long contactId) {
		if (contactId == null || Objects.equals(contactId, NumberUtils.LONG_ZERO)) {
			return;
		}
		this.deleteEntityById(contactId);
	}

	public List<Contact> listByFilter(ContactFilter contactFilter) {
		LoadType<Contact> loadType = ObjectifyService.ofy().load().type(Contact.class);
		String name = contactFilter.getName();
		String cpf = contactFilter.getCpf();
		String email = contactFilter.getEmail();
		Query<Contact> query = loadType.chunkAll();
		if (StringUtils.isNotEmpty(name)) {
			query = query.filter("name >= ", name).filter("name <", name + "\uFFFD");
		}
		if (StringUtils.isNotEmpty(cpf)) {
			query = query.filter("cpf >= ", cpf).filter("cpf <", cpf + "\uFFFD");
		}
		if (StringUtils.isNotEmpty(email)) {
			query = query.filter("emails in", Arrays.asList(email));
		}
		return query.list();
	}

	@Override
	Class<Contact> getTClass() {
		return Contact.class;
	}
}
