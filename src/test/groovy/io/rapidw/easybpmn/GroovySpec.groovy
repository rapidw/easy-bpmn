package io.rapidw.easybpmn

import spock.lang.Specification


class GroovySpec extends Specification {

    def "test"() {
        given:
        GroovyShell shell = new GroovyShell()
        def res = shell.evaluate('1+1')

        expect:
        res == 2
    }

}
