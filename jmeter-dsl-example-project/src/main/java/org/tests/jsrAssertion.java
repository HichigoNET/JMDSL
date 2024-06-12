package org.tests;
import us.abstracta.jmeter.javadsl.core.samplers.BaseSampler;
import org.apache.jmeter.assertions.JSR223Assertion;
import org.apache.jmeter.testelement.TestElement;
public class jsrAssertion extends BaseSampler<jsrAssertion> {

    private String myProp;

    private jsrAssertion(String name) {
        super(name, null); // you can pass null here if custom sampler is a test bean
    }

    public jsrAssertion myProp(String val) {
        this.myProp = val;
        return this;
    }

    @Override
    protected TestElement buildTestElement() {
        JSR223Assertion ret = new JSR223Assertion();
//        ret.setProperty();
        return ret;
    }

    public static jsrAssertion customSampler(String name) {
        return new jsrAssertion(name);
    }
}
