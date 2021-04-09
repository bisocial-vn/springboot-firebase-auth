package com.firebase.auth.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class BasicEntityAudit extends AbstractPersistable<Long> {

	@Column(name = "CREATED_DATE", nullable = false, updatable = false)
	@CreatedDate
	private Date createdDate;

	@Column(name = "MODIFIED_DATE")
	@LastModifiedDate
	private Date modifiedDate;

}
