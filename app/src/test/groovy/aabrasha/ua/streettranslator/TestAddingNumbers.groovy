package aabrasha.ua.streettranslator

import spock.lang.Specification

/**
*  Created by Andrii Abramov on 8/28/16.
*/
class TestAddingNumbers extends Specification {

    def "test adding some numbers"() {

        when:
        def c = a + b
        then:
        c == res
        where:
        a  | b | res
        1  | 2 | 3
        3  | 2 | 5
        5  | 8 | 13
        -1 | 4 | 3
    }

    def "test subtracting some numbers"() {

        when:
        def c = a - b
        then:
        c == res
        where:
        a  | b | res
        1  | 2 | -1
        3  | 2 | 1
        5  | 8 | -3
        -1 | 4 | -5
    }

}