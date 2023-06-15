package io.rapidw.easybpmn

import jakarta.el.BeanELResolver
import jakarta.el.ExpressionFactory
import jakarta.el.StandardELContext
import spock.lang.Specification

class ElSpec extends Specification {

    def "test"() {
        given:
        def variable = new MyVariable()
        variable.setX(1)
        def expressFactory = ExpressionFactory.newInstance()
        def context = new StandardELContext(expressFactory)
        context.addELResolver(new BeanELResolver())
        context.putContext(MyVariable.class, variable)
        def res = expressFactory.createValueExpression(context, "${variable.x + 1}", Integer.class).getValue(context)
        expect:
        res == 2
    }

}
