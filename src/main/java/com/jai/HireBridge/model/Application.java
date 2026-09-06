package com.jai.HireBridge.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames= {"studentId" ,"jobId"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Application
{
  @Id
  @GeneratedValue
  private Long applicationId;
  
  private Long studentId;
  
  private Long jobId;
  
  @Enumerated(EnumType.STRING)
  private AppliStatus status;
  
  private LocalDateTime appliedAt;
	
	
}
