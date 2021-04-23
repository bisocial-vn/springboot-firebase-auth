package com.bi.firebase.auth.example.entity;

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

	@Column(name = "created_date", updatable = false)
	@CreatedDate
	private Date createdDate;

	@Column(name = "modified_date")
	@LastModifiedDate
	private Date modifiedDate;

}
