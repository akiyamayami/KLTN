package com.tlcn.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tlcn.model.NotifyEvent;
import com.tlcn.model.User;

public interface NotifyEventRepository extends CrudRepository<NotifyEvent, Integer>{
	@Query("select n from notifyevent n where n.notifyOfUser = ?1 order by n.notifyID desc")
	public List<NotifyEvent> getListNotifyNewest(User user);
	
}
