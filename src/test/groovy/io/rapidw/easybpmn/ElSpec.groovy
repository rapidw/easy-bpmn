package io.rapidw.easybpmn


import jakarta.el.ELManager
import spock.lang.Specification

class ElSpec extends Specification {

    def "test"() {
        given:
        def variable = new MyVariable()
        variable.setX(1)
        def elManager = new ELManager()
        elManager.defineBean("variable", variable)
        def context = elManager.getELContext()
        def exp = ELManager.getExpressionFactory().createValueExpression(context, '${variable.x + 1}', Integer.class)
        def res = exp.getValue(context)
        expect:
        res == 2
    }

}
