package com.firebase.auth.example.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table(name = "TEST_TABLE")
@Entity(name = "TestEntity")
@Data
public class TestEntity {

	@Id
	@GeneratedValue
	private long id;
	private String desc;

}
