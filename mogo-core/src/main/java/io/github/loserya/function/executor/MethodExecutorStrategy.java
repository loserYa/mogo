/**
 * MethodExecutorStrategy.java 代码解读
 * 这段代码定义了一个名为 MethodExecutorStrategy 的接口，它用于在Java中实现方法执行策略。这个接口包含三个主要部分：
 * 一个方法类型标识、一个执行拦截方法的功能，以及一个默认方法用于替换参数数组。下面是对每个部分的详细解释：
 * <p>
 * 方法类型 (method):
 * <p>
 * 这个方法是一个抽象方法，它返回一个 ExecuteMethodEnum 类型的值。ExecuteMethodEnum 可能是一个枚举类型，用于表示不同的方法执行方式或策略。
 * 这个方法的目的是让实现该接口的类指定其使用的方法执行策略。
 * 执行拦截方法 (invoke):
 * <p>
 * 这是一个抽象方法，用于执行拦截逻辑。
 * 它接受三个参数：Class<?> clazz（表示要执行的方法所属的类），Interceptor interceptor（拦截器实例，可能用于在方法执行前后添加额外的逻辑），以及 Object[] args（方法参数数组）。
 * 这个方法的具体实现将取决于实现该接口的类，它定义了如何使用拦截器和参数来执行特定的方法。
 * 替换参数数组 (replace):
 * <p>
 * 这是一个默认方法，用于替换旧参数数组 oldArgs 中的元素为新参数数组 newArgs 中的对应元素。
 * 首先检查两个数组的长度是否相等，如果不相等，则抛出异常。
 * 如果长度相等，则使用 System.arraycopy 方法将 newArgs 中的元素复制到 oldArgs 中。
 * 这个方法提供了一种方便的方式来更新方法调用的参数，而无需修改原始的参数数组。
 * 整体来看，这个接口定义了一种灵活的方法执行策略，允许通过不同的实现来定制方法的执行方式，同时提供了一种机制来在执行方法之前或之后插入额外的逻辑（例如拦截器）。
 * 这种设计模式在需要高度可配置或可扩展的系统中非常有用。
 */
package io.github.loserya.function.executor;


import io.github.loserya.function.interceptor.Interceptor;
import io.github.loserya.hardcode.constant.ExecuteMethodEnum;
import io.github.loserya.utils.ExceptionUtils;

/**
 * 方法执行策略(解耦逻辑)
 *
 * @author loser
 * @since 1.0.0
 */
public interface MethodExecutorStrategy {

    /**
     * 方法类型
     */
    ExecuteMethodEnum method();

    /**
     * 执行拦截方法
     */
    void invoke(Class<?> clazz, Interceptor interceptor, Object[] args);

    default void replace(Object[] oldArgs, Object[] newArgs) {

        if (oldArgs.length != newArgs.length) {
            throw ExceptionUtils.mpe("args length not match");
        }
        System.arraycopy(newArgs, 0, oldArgs, 0, oldArgs.length);

    }

}
