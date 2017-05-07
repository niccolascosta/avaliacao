package br.com.aurum.astrea.dao;

import java.util.List;

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
		helper.setUp();
	}

	@AfterClass
	public static void tearDown() {
		helper.tearDown();
	}

	@Test
	public void saveContactTest() {
		Long generetedCode = saveContact();
		Assert.assertNotNull("Deveria ter gerado um código", generetedCode);
	}

	@Test
	public void editContactTest() {
		Long generetedCode = saveContact();
		Contact contact = dao.getContactById(generetedCode);
		contact.setName("Niccael");
		dao.save(contact);
		contact = dao.getContactById(generetedCode);
		Assert.assertEquals("Deveria ter alterado o nome", "Niccael", contact.getName());
	}
	
	@Test
	public void deleteContactTest(){
		Long generetedCode = saveContact();
		dao.delete(generetedCode);
		Contact contact = dao.getContactById(generetedCode);
		Assert.assertNull("Deveria ter apagado o contact", contact);
	}
	
	@Test
	public void listContactsTest(){
		saveContact();
		saveContact();
		List<Contact> contacts = dao.list();
		Assert.assertTrue("Deveria conter pelo menos dois contacts", contacts != null && contacts.size() >= 2);
	}
	
	@Test
	public void listContactsByFilterNameTest(){
		Contact contact = createContact();
		Contact contact2 = createContact();
		contact2.setName("Lucas");
		dao.save(contact);
		dao.save(contact2);
		ContactFilter contactFilter = new ContactFilter();
		contactFilter.setName("Niccolas");
		List<Contact> contacts = dao.listByFilter(contactFilter);
		Assert.assertTrue("Deveria conter um contact", contacts != null && contacts.size() >= 1);
	}
	
	@Test
	public void listContactsByFilterCpfTest(){
		Contact contact = createContact();
		Contact contact2 = createContact();
		contact2.setCpf("063.461.999-33");
		dao.save(contact);
		dao.save(contact2);
		ContactFilter contactFilter = new ContactFilter();
		contactFilter.setCpf(contact.getCpf());
		List<Contact> contacts = dao.listByFilter(contactFilter);
		Assert.assertTrue("Deveria conter um contact", contacts != null && contacts.size() >= 1);
	}
	
	@Test
	public void listContactsByFilterEmailTest(){
		Contact contact = createContact();
		Contact contact2 = createContact();
		contact2.getEmails().clear();
		contact2.getEmails().add("niccolas@gmail.com");
		dao.save(contact);
		dao.save(contact2);
		ContactFilter contactFilter = new ContactFilter();
		contactFilter.setEmail("niccolas.costa@gmail.com");
		List<Contact> contacts = dao.listByFilter(contactFilter);
		Assert.assertTrue("Deveria conter um contact", contacts != null && contacts.size() >= 1);
	}

	@Test
	public void jsonToObject() {
		Contact contact = createContact();
		Gson gson = new Gson();
		String json = gson.toJson(contact);
		System.out.println(json);
	}

	private Long saveContact() {
		Contact contact = createContact();
		Long generetedCode = dao.save(contact);
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
