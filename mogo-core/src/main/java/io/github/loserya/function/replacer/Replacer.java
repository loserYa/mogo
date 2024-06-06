/**
 * Replacer.java 代码解读
 * 这段代码定义了一个名为 Replacer 的接口，它位于 io.github.loserya.function.replacer 包中。这个接口主要用于实现某种类型的替换逻辑，通常在代理模式或者装饰器模式中使用。下面是对代码的详细解释：
 * <p>
 * 接口定义:
 * <p>
 * public interface Replacer {
 * 这行代码定义了一个名为 Replacer 的接口。
 * <p>
 * 默认方法 order:
 * <p>
 * default int order() {
 * return 1;
 * }
 * 这是一个默认方法，返回一个整数值 1。这个方法可能用于确定替换器的执行顺序。默认情况下，所有实现了 Replacer 接口的类将返回 1，除非它们自己重写了这个方法。
 * <p>
 * 抽象方法 invoke:
 * <p>
 * Object invoke(Class<?> clazz, Object proxy, Object target, Method method, Object[] args) throws Throwable;
 * 这是一个抽象方法，它定义了替换器需要实现的核心逻辑。这个方法接收五个参数：
 * <p>
 * Class<?> clazz: 被代理类的类对象。
 * Object proxy: 代理对象的实例。
 * Object target: 目标对象的实例。
 * Method method: 被调用的方法。
 * Object[] args: 方法调用时传入的参数数组。
 * 这个方法的返回类型是 Object，意味着它可以返回任何类型的对象。它可能会抛出 Throwable，这意味着在执行过程中可能会遇到错误或异常。
 * <p>
 * 方法 supplier:
 * <p>
 * BoolFunction supplier();
 * 这是一个返回 BoolFunction 类型的方法。BoolFunction 可能是一个功能性接口，用于提供某种布尔逻辑。这个方法的具体实现取决于实现 Replacer 接口的类。
 * <p>
 * 默认方法 build:
 * <p>
 * default Object[] build(Object... args) {
 * return args;
 * }
 * 这是另一个默认方法，它接受一个可变数量的参数，并将这些参数作为数组返回。这个方法可以用于构建或修改参数数组，但在默认实现中，它只是简单地返回传入的参数。
 * <p>
 * 总结来说，Replacer 接口定义了一套用于替换或修改代理对象行为的规则和逻辑。它允许实现者定义特定的替换逻辑，这些逻辑可以在代理对象调用方法时被触发和执行。
 * 通过这种方式，可以在不修改原始类代码的情况下，增加额外的功能或行为。
 */
package io.github.loserya.function.replacer;


import io.github.loserya.utils.func.BoolFunction;

import java.lang.reflect.Method;

/**
 * 替换器 只会执行首个命中的替换器
 *
 * @author loser
 * @since 1.0.0
 */
public interface Replacer {

    default int order() {
        return 1;
    }

    Object invoke(Class<?> clazz, Object proxy, Object target, Method method, Object[] args) throws Throwable;

    BoolFunction supplier();

    default Object[] build(Object... args) {
        return args;
    }

}