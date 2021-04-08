package com.firebase.auth.example.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Table(name = "TEST_TABLE")
@Entity(name = "TestEntity")
@Data
@EqualsAndHashCode(callSuper = true)
public class TestEntity extends BasicEntityAudit {

	private String desc;

}
