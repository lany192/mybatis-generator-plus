/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.lany192.mybatis.generator.template.model;

import lombok.Getter;
import lombok.Setter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.io.Serializable;

/**
 * @author liuzh
 * @since 3.4.5
 */
@Getter
@Setter
public class ColumnField implements Serializable {
    private TableClass tableClass;
    private String columnName;
    private String jdbcType;
    private String fieldName;
    private String remarks;
    private FullyQualifiedJavaType type;
    private String typePackage;
    private String shortTypeName;
    private String fullTypeName;
    private boolean identity;
    private boolean nullable;
    private boolean blobColumn;
    private boolean stringColumn;
    private boolean jdbcCharacterColumn;
    private boolean jdbcDateColumn;
    private boolean jdbcTimeColumn;
    private boolean sequenceColumn;
    private int length;
    private int scale;
}
