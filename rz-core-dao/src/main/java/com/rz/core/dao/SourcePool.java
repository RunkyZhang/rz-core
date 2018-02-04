package com.rz.core.dao;

import com.rz.core.dao.masking.DataMasker;
import com.rz.core.dao.model.ModelDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by renjie.zhang on 2/1/2018.
 */
public class SourcePool {
    public static Map<String, ModelDefinition> modelDefinitions = new HashMap<>();
    public static Map<String, DataMasker> dataMaskers = new HashMap<>();
}
