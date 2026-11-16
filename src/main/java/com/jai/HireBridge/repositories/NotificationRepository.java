package com.jai.HireBridge.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jai.HireBridge.model.Notification;

public interface NotificationRepository  extends JpaRepository<Notification,Long>{

}
