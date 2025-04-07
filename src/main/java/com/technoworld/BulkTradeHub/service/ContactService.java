package com.technoworld.BulkTradeHub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.technoworld.BulkTradeHub.entity.Contact;
import com.technoworld.BulkTradeHub.repository.ContactRepository;

@Service
public class ContactService {
	@Autowired
	private ContactRepository contactRepository;

	public Contact saveContactMessage(Contact contact) {
		return contactRepository.save(contact);
	}

	public List<Contact> getAllContactMessage() {
		List<Contact> lst = contactRepository.findAll();
		return lst;
	}
	
	public void deleteConatctMessage(int id){
		contactRepository.deleteById(id);
	}
}
