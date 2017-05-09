package br.com.aurum.astrea.dao;

import java.util.List;
import java.util.Objects;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gson.Gson;

import br.com.aurum.astrea.domain.Contact;
import br.com.aurum.astrea.filter.ContactFilter;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContactDaoTest {

	private final ContactDao dao = new ContactDao();
	private static final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	@BeforeClass
	public static void beforeClass() {
		ContactDaoTest.helper.setUp();
	}

	@AfterClass
	public static void afterClass() {
		ContactDaoTest.helper.tearDown();
	}

	@Test
	public void saveContactTest() {
		Long generetedCode = this.saveContact();
		Assert.assertNotNull("Deveria ter gerado um código", generetedCode);
	}

	@Test
	public void editContactTest() {
		Long generetedCode = this.saveContact();
		Contact contact = this.dao.getContactById(generetedCode);
		contact.setName("Niccael");
		this.dao.save(contact);
		contact = this.dao.getContactById(generetedCode);
		Assert.assertEquals("Deveria ter alterado o nome", "Niccael", contact.getName());
	}

	@Test
	public void deleteContactTest() {
		Long generetedCode = this.saveContact();
		this.dao.delete(generetedCode);
		Contact contact = this.dao.getContactById(generetedCode);
		Assert.assertNull("Deveria ter apagado o contato", contact);
	}

	@Test
	public void listContactsTest() {
		this.saveContact();
		this.saveContact();
		List<Contact> contacts = this.dao.list();
		Assert.assertTrue("Deveria conter pelo menos dois contatos", Objects.nonNull(contacts) && contacts.size() >= 2);
	}

	@Test
	public void listContactsByFilterNameTest() {
		Contact contact = this.createContact();
		Contact contact2 = this.createContact();
		contact2.setName("Lucas");
		this.dao.save(contact);
		this.dao.save(contact2);
		ContactFilter contactFilter = new ContactFilter();
		contactFilter.setName("Niccolas");
		List<Contact> contacts = this.dao.listByFilter(contactFilter);
		Assert.assertTrue("Deveria conter um contato", Objects.nonNull(contacts) && contacts.size() >= 1);
	}

	@Test
	public void listContactsByFilterCpfTest() {
		Contact contact = this.createContact();
		Contact contact2 = this.createContact();
		contact2.setCpf("063.461.999-33");
		this.dao.save(contact);
		this.dao.save(contact2);
		ContactFilter contactFilter = new ContactFilter();
		contactFilter.setCpf(contact.getCpf());
		List<Contact> contacts = this.dao.listByFilter(contactFilter);
		Assert.assertTrue("Deveria conter um contato", Objects.nonNull(contacts) && contacts.size() >= 1);
	}

	@Test
	public void listContactsByFilterEmailTest() {
		Contact contact = this.createContact();
		Contact contact2 = this.createContact();
		contact2.getEmails().clear();
		contact2.getEmails().add("niccolas@gmail.com");
		this.dao.save(contact);
		this.dao.save(contact2);
		ContactFilter contactFilter = new ContactFilter();
		contactFilter.setEmail("niccolas.costa@gmail.com");
		List<Contact> contacts = this.dao.listByFilter(contactFilter);
		Assert.assertTrue("Deveria conter um contato", Objects.nonNull(contacts) && contacts.size() >= 1);
	}

	@Test
	public void jsonToObject() {
		Contact contact = this.createContact();
		Gson gson = new Gson();
		String json = gson.toJson(contact);
		System.out.println(json);
	}

	private Long saveContact() {
		Contact contact = this.createContact();
		Long generetedCode = this.dao.save(contact);
		return generetedCode;
	}

	private Contact createContact() {
		Contact contact = new Contact();
		contact.setName("Niccolas");
		contact.setAddress("Rua Jose Lino Kretzer");
		contact.setBirthDay("02");
		contact.setBirthMonth("09");
		contact.setBirthYear("1989");
		contact.setCpf("063.461.999-35");
		contact.setObservation("Observação");
		contact.setRg("4.228.540");
		contact.getEmails().add("niccolas.costa@gmail.com");
		return contact;
	}
}
