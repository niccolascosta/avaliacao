package br.com.aurum.astrea.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;

import br.com.aurum.astrea.dao.ContactDao;
import br.com.aurum.astrea.domain.Contact;
import br.com.aurum.astrea.filter.ContactFilter;
import br.com.aurum.astrea.util.JsonParse;

public class ContactServlet extends HttpServlet {

	private static final long serialVersionUID = -889510107250504038L;
	public static final String ID = "id";
	public static final String EMAIL_FILTER = "email";
	public static final String CPF_FILTER = "cpf";
	public static final String NAME_FILTER = "name";
	public static final String ACTION_LISTAGEM = "list";
	public static final String ACTION_GET_CONTACT = "get";
	public static final String ACTION = "action";
	private static final String UTF_8 = "UTF-8";
	private static final String APPLICATION_JSON = "application/json";
	private static final ContactDao DAO = new ContactDao();

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String json = IOUtils.toString(req.getInputStream());
		Contact contact = (Contact) JsonParse.convertJsonToObject(json, Contact.class);
		if (contact == null) {
			return;
		}
		ContactServlet.DAO.save(contact);
		this.setResponseData(resp, contact);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		this.setResponseData(resp, this.executeAction(req));
	}

	@Override
	public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		long contactId = NumberUtils.toLong(req.getParameter(ContactServlet.ID));
		ContactServlet.DAO.delete(contactId);
		this.setResponseData(resp, "OK");
	}

	private void setResponseData(HttpServletResponse resp, Object data) throws IOException {
		resp.setContentType(ContactServlet.APPLICATION_JSON);
		resp.setCharacterEncoding(ContactServlet.UTF_8);
		PrintWriter out = resp.getWriter();
		out.print(JsonParse.ObjectToJson(data));
		out.flush();
	}

	private Object executeAction(HttpServletRequest req) {
		String action = req.getParameter(ContactServlet.ACTION);
		switch (action) {
		case ACTION_LISTAGEM:
			return this.listContacts(req);
		case ACTION_GET_CONTACT:
			return this.getContact(req);
		default:
			return null;
		}
	}

	private Object getContact(HttpServletRequest req) {
		Long contactId = NumberUtils.toLong(req.getParameter(ContactServlet.ID));
		return ContactServlet.DAO.getContactById(contactId);
	}

	private Object listContacts(HttpServletRequest req) {
		ContactFilter contactFilter = new ContactFilter();
		contactFilter.setCpf(req.getParameter(ContactServlet.CPF_FILTER));
		contactFilter.setEmail(req.getParameter(ContactServlet.EMAIL_FILTER));
		contactFilter.setName(req.getParameter(ContactServlet.NAME_FILTER));
		return ContactServlet.DAO.listByFilter(contactFilter);
	}
}
