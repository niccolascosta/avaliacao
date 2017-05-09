package br.com.aurum.astrea.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gson.reflect.TypeToken;

import br.com.aurum.astrea.domain.Contact;
import br.com.aurum.astrea.service.ContactServlet;
import br.com.aurum.astrea.util.DelegatingServletInputStream;
import br.com.aurum.astrea.util.JsonParse;

public class ContactServletTest extends Mockito {

	private static final String APPLICATION_JSON = "application/json";
	private static final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	private HttpServletRequest request;
	private HttpServletResponse response;

	@AfterClass
	public static void afterClass() {
		ContactServletTest.helper.tearDown();
	}

	@BeforeClass
	public static void beforeClass() {
		ContactServletTest.helper.setUp();
	}

	@Before
	public void setUp() {
		this.request = Mockito.mock(HttpServletRequest.class);
		this.response = Mockito.mock(HttpServletResponse.class);
	}

	@Test
	public void saveContactServletTest() throws ServletException, IOException {
		StringWriter out = this.createContactPost();
		Contact contact = (Contact) JsonParse.convertJsonToObject(out.toString(), Contact.class);
		Assert.assertTrue("Deveria ter cadastrado o contato",
				Objects.nonNull(contact) && Objects.nonNull(contact.getId()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void listContactServletTest() throws Exception {
		this.createContactPost();
		this.request = Mockito.mock(HttpServletRequest.class);
		this.response = Mockito.mock(HttpServletResponse.class);
		Mockito.when(this.request.getParameter(ContactServlet.ACTION)).thenReturn(ContactServlet.ACTION_LISTAGEM);
		StringWriter out = new StringWriter();
		Mockito.when(this.response.getWriter()).thenReturn(new PrintWriter(out, true));
		ContactServlet contactServlet = new ContactServlet();
		contactServlet.doGet(this.request, this.response);
		Type type = new TypeToken<ArrayList<Contact>>() {
		}.getType();
		List<Contact> contacts = (ArrayList<Contact>) JsonParse.convertJsonToObject(out.toString(), type);
		Assert.assertTrue("Deveria conter um contato", Objects.nonNull(contacts) && contacts.size() >= 1);
	}

	@Test
	public void getContactServletTest() throws Exception {
		StringWriter out = this.createContactPost();
		JsonParse.convertJsonToObject(out.toString(), Contact.class);
		this.request = Mockito.mock(HttpServletRequest.class);
		this.response = Mockito.mock(HttpServletResponse.class);
		Mockito.when(this.request.getParameter(ContactServlet.ACTION)).thenReturn(ContactServlet.ACTION_GET_CONTACT);
		Mockito.when(this.request.getParameter(ContactServlet.ID)).thenReturn(null);
		StringWriter outGet = new StringWriter();
		Mockito.when(this.response.getWriter()).thenReturn(new PrintWriter(outGet, true));
		ContactServlet contactServlet = new ContactServlet();
		contactServlet.doGet(this.request, this.response);
		Contact contactGet = (Contact) JsonParse.convertJsonToObject(outGet.toString(), Contact.class);
		Assert.assertTrue("Deveria ter retornado um contato", Objects.nonNull(contactGet));
	}

	private StringWriter createContactPost() throws IOException {
		String contactJson = JsonParse.ObjectToJson(this.createContact());
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				contactJson.getBytes(StandardCharsets.UTF_8));
		DelegatingServletInputStream delegatingServletInputStream = new DelegatingServletInputStream(
				byteArrayInputStream);
		Mockito.when(this.request.getInputStream()).thenReturn(delegatingServletInputStream);
		Mockito.when(this.request.getContentType()).thenReturn(ContactServletTest.APPLICATION_JSON);
		StringWriter out = new StringWriter();
		Mockito.when(this.response.getWriter()).thenReturn(new PrintWriter(out, true));
		ContactServlet contactServlet = new ContactServlet();
		contactServlet.doPost(this.request, this.response);
		return out;
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
