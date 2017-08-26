package io.dummymaker.data;

import io.dummymaker.annotation.special.GenForceExport;
import io.dummymaker.annotation.special.GenIgnoreExport;
import io.dummymaker.annotation.special.GenRenameExport;

/**
 * Dummy Object used as data to proceed in tests
 * This object HAS NO FIELDS TO POPULATE
 *
 * @author GoodforGod
 * @since 18.08.2017
 */
public class DummyNoPopulateFields {

    @GenRenameExport(name = "socialGroup")
    @GenForceExport
    private String group = "100";

    @GenIgnoreExport
    private String city;

    private Integer num;

    private String name;
}
