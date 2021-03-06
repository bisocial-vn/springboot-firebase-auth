package com.bi.firebase.auth.example.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Table(name = "tbl_tests")
@Entity(name = "TestEntity")
@Data
@EqualsAndHashCode(callSuper = true)
public class TestEntity extends BasicEntityAudit {
	private String desception;
	private Date testDate;
}
