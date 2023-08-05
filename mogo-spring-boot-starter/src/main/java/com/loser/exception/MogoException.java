/*
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.loser.exception;


/**
 * mogo 异常类
 *
 * @author loser
 * @date 2023/2/5 17:29
 */
public class MogoException extends RuntimeException {

    public MogoException(String message) {
        super(message);
    }

    public MogoException(Throwable throwable) {
        super(throwable);
    }

    public MogoException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
