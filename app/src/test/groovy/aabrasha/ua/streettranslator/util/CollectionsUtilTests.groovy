package aabrasha.ua.streettranslator.util

import spock.lang.Specification

/**
 *  Created by Andrii Abramov on 8/28/16.
 */
class CollectionsUtilTests extends Specification {

    def "Check size of result array"() {

        when:
        def array = CollectionsUtil.toArray(items)

        then:
        array.length == expected

        where:
        items           | expected
        ['a', 'b', 'c'] | 3
        ['a', 'b']      | 2
        ['a']           | 1
        []              | 0


    }

    def "Check content of result"() {

        when:
        def array = CollectionsUtil.toArray(items)

        then:
        array == expected

        where:

        items           | expected
        ['a', 'b', 'c'] | ['a', 'b', 'c']
        [1, 2, 3]       | [1, 2, 3]
        []              | []

    }

}