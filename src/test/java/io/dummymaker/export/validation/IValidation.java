package io.dummymaker.export.validation;

/**
 * "Default Description"
 *
 * @author GoodforGod
 * @since 01.09.2017
 */
public interface IValidation {
    void isSingleDummyValid(String[] dummy);

    void isTwoDummiesValid(String[] dummies);
}