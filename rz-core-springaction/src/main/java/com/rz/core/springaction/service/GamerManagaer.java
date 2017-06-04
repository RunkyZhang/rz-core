package com.rz.core.springaction.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GamerManagaer {
	@Autowired
	private GameMachineManager gameMachineManager;

	private GameManager gameManager;

	@Resource(name = "appUserName")
	private String name;

	@Resource
	private String appPassword;

	@Value(value = "${app.user.mail}")
	private String mail;

	@Value(value = "${app.user.phoneNumber:13818530000}")
	private String phoneNumber;

	@Value(value = "#{T(java.lang.Math).PI * 2}")
	private double number;

	// appUserName is bean name
	@Value(value = "#{'' != appUserName ? true : false}")
	private boolean sex;

	@PostConstruct
	private void start() {

	}

	@Autowired
	public void buildGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public void play() {

	}
	
	public void play(int timeout) {

	}

	@Override
	public String toString() {
		this.gameMachineManager.toString();
		this.gameManager.toString();

		return super.toString();
	}
}
