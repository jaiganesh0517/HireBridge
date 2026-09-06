package com.jai.HireBridge.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobPosting 
{
  @Id
  @GeneratedValue
  private Long jobPostId;
  
  private Long recruiterId;
  
  @NotNull
  private String title;
  
  private String descrip;
  
  private BigDecimal ctc;
  
  private double minCgpa;
  @Enumerated(EnumType.STRING)
  private Status jobStatus;
  
  @NotNull
  private LocalDateTime deadline;
  
  
}
