package com.rz.core.springaction.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Qualifier(value = "houhou")
@Service
public class ServiceDevManagerImpl implements DevManager {

}
