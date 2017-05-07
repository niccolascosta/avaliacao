package br.com.aurum.astrea.dao;

import java.util.List;
import java.util.Objects;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.ObjectifyService;

public abstract class AbstractDao<T> {
	
	abstract Class<T> getTClass();

	protected Key<T> persist(T entity) {
		return ObjectifyService.ofy().save().entity(entity).now();
	}
	
	protected T getEntityById(Long id){
		if(Objects.isNull(id)){
			return null;
		}
		Key<T> key = Key.create(getTClass(), id);
		LoadResult<T> loadResult = ObjectifyService.ofy().load().key(key);
		return loadResult.now();
	}
	
	protected void deleteEntityById(Long id){
		if(Objects.isNull(id)){
			return;
		}
		Key<T> key = Key.create(getTClass(), id);
		ObjectifyService.ofy().delete().key(key).now();
	}
	
	protected List<T> listEntities(){
		return ObjectifyService.ofy().load().type(getTClass()).list();
	}
}
