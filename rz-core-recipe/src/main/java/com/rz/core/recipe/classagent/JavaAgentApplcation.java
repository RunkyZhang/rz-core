package com.rz.core.recipe.classagent;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.util.Date;

/**
 * Created by renjie.zhang on 7/5/2017.
 */
public class JavaAgentApplcation {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain**********************************");

        if (0 <= new Date().getTime() % 2) {
            inst.addTransformer(new TestClassFileTransformer());
        } else {
            // do not work
            try {
                ClassDefinition classDefinition = new ClassDefinition(
                        Class.forName("com.rz.core.practice.dynamic.InstrumentationService"),
                        TestClassFileTransformer.getJarStream(TestClassFileTransformer.className));
                inst.redefineClasses(classDefinition);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
