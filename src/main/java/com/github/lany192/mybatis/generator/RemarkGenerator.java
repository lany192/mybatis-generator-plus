package com.github.lany192.mybatis.generator;


import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

/**
 * 生成的数据库注释
 *
 * @author Lany
 * @see org.mybatis.generator.internal.DefaultCommentGenerator
 */
public final class RemarkGenerator implements CommentGenerator {
    private boolean suppressDate = false;
    private boolean suppressAllComments = false;
    private boolean addRemarkComments = false;
    private SimpleDateFormat dateFormat;

    @Override
    public void addConfigurationProperties(Properties properties) {
        this.suppressDate = StringUtility.isTrue(properties.getProperty("suppressDate"));
        this.suppressAllComments = StringUtility.isTrue(properties.getProperty("suppressAllComments"));
        this.addRemarkComments = StringUtility.isTrue(properties.getProperty("addRemarkComments"));
        String dateFormatString = properties.getProperty("dateFormat");
        if (StringUtility.stringHasValue(dateFormatString)) {
            this.dateFormat = new SimpleDateFormat(dateFormatString);
        }

    }

    /**
     * 给Java文件加注释，这个注释是在文件的顶部，也就是package上面。
     */
    @Override
    public void addJavaFileComment(CompilationUnit unit) {
        if (!this.suppressAllComments) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            unit.addFileCommentLine("/*");
            unit.addFileCommentLine("*");
            unit.addFileCommentLine("* " + unit.getType().getShortName() + ".java");
            unit.addFileCommentLine("* mybatis generator plus自动生成,请勿编辑.");
            unit.addFileCommentLine("* Copyright(C) " + Calendar.getInstance().get(Calendar.YEAR) + " Lany");
            unit.addFileCommentLine("* @date " + sdf.format(new Date()) + "");
            unit.addFileCommentLine("*/");
        }
    }

    @Override
    public void addComment(XmlElement xmlElement) {
        if (!this.suppressAllComments) {
            xmlElement.addElement(new TextElement("<!--"));
            xmlElement.addElement(new TextElement("mybatis generator plus自动生成,请勿编辑."));
            xmlElement.addElement(new TextElement("-->"));
        }
    }

    @Override
    public void addRootComment(XmlElement rootElement) {

    }

    /**
     * Java类的类注释
     */
    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
        addClassComment(innerClass, introspectedTable, false);
    }

    /**
     * Java类的类注释
     */
    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
        if (!this.suppressAllComments) {
            StringBuilder sb = new StringBuilder();
            innerClass.addJavaDocLine("/**");
            innerClass.addJavaDocLine(" * mybatis generator plus自动生成,请勿编辑.");
            sb.append(" * This class corresponds to the database table ");
            sb.append(introspectedTable.getFullyQualifiedTable());
            sb.append(" * @author ");
            sb.append(System.getProperties().getProperty("user.name"));
            innerClass.addJavaDocLine(sb.toString());
            innerClass.addJavaDocLine(" */");
        }
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (!this.suppressAllComments && this.addRemarkComments) {
            topLevelClass.addJavaDocLine("/**");
            String remarks = introspectedTable.getRemarks();
            if (this.addRemarkComments && StringUtility.stringHasValue(remarks)) {
                topLevelClass.addJavaDocLine(" * Database Table Remarks:");
                String[] remarkLines = remarks.split(System.getProperty("line.separator"));
                String[] var5 = remarkLines;
                int var6 = remarkLines.length;

                for (int var7 = 0; var7 < var6; ++var7) {
                    String remarkLine = var5[var7];
                    topLevelClass.addJavaDocLine(" *   " + remarkLine);
                }
            }

            topLevelClass.addJavaDocLine(" *");
            topLevelClass.addJavaDocLine(" * 对应数据库表：" + introspectedTable.getFullyQualifiedTable());
            topLevelClass.addJavaDocLine(" */");
        }
    }

    @Override
    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
        if (!this.suppressAllComments) {
            innerEnum.addJavaDocLine("/**");
            innerEnum.addJavaDocLine(" * mybatis generator plus自动生成,请勿编辑.");
            innerEnum.addJavaDocLine(" * 对应数据库表：" + introspectedTable.getFullyQualifiedTable());
            innerEnum.addJavaDocLine(" */");
        }
    }

    /**
     * 添加entity字段的备注
     *
     * @param field              字段
     * @param introspectedTable  表
     * @param introspectedColumn 数据库字段
     */
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }
        field.addJavaDocLine("/**");
        field.addJavaDocLine(" * " + introspectedColumn.getRemarks() + "，对应字段:" + introspectedColumn.getActualColumnName());
        field.addJavaDocLine(" */");
    }

    /**
     * 添加entity字段的备注，非数据库字段
     *
     * @param field             字段
     * @param introspectedTable 表
     */
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
//        if (!this.suppressAllComments) {
//            StringBuilder sb = new StringBuilder();
//            field.addJavaDocLine("/**");
//            sb.append(" * This field corresponds to the database table ");
//            sb.append(introspectedTable.getFullyQualifiedTable());
//            field.addJavaDocLine(sb.toString());
//            field.addJavaDocLine(" */");
//        }
    }

    @Override
    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
        if (!this.suppressAllComments) {
            method.addJavaDocLine("/**");
            StringBuilder sb = new StringBuilder();
            sb.append(" * " + method.getName());
            sb.append(" * This method corresponds to the database table ");
            sb.append(introspectedTable.getFullyQualifiedTable());
            method.addJavaDocLine(sb.toString());
            method.addJavaDocLine(" */");
        }
    }

    @Override

    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (!this.suppressAllComments) {
            StringBuilder sb = new StringBuilder();
            method.addJavaDocLine("/**");
            sb.append(" * This method returns the value of the database column ");
            sb.append(introspectedTable.getFullyQualifiedTable());
            sb.append('.');
            sb.append(introspectedColumn.getActualColumnName());
            method.addJavaDocLine(sb.toString());
            method.addJavaDocLine(" *");
            sb.setLength(0);
            sb.append(" * @return the value of ");
            sb.append(introspectedTable.getFullyQualifiedTable());
            sb.append('.');
            sb.append(introspectedColumn.getActualColumnName());
            method.addJavaDocLine(sb.toString());
            method.addJavaDocLine(" */");
        }
    }

    @Override
    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (!this.suppressAllComments) {
            StringBuilder sb = new StringBuilder();
            method.addJavaDocLine("/**");
            sb.append(" * This method sets the value of the database column ");
            sb.append(introspectedTable.getFullyQualifiedTable());
            sb.append('.');
            sb.append(introspectedColumn.getActualColumnName());
            method.addJavaDocLine(sb.toString());
            method.addJavaDocLine(" *");
            Parameter parm = (Parameter) method.getParameters().get(0);
            sb.setLength(0);
            sb.append(" * @param ");
            sb.append(parm.getName());
            sb.append(" the value for ");
            sb.append(introspectedTable.getFullyQualifiedTable());
            sb.append('.');
            sb.append(introspectedColumn.getActualColumnName());
            method.addJavaDocLine(sb.toString());
            method.addJavaDocLine(" */");
        }
    }

    @Override
    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {
        imports.add(new FullyQualifiedJavaType("javax.annotation.Generated"));
        String comment = "Source Table: " + introspectedTable.getFullyQualifiedTable().toString();
        method.addAnnotation(this.getGeneratedAnnotation(comment));
    }

    @Override
    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {
        imports.add(new FullyQualifiedJavaType("javax.annotation.Generated"));
        String comment = "Source field: " + introspectedTable.getFullyQualifiedTable().toString() + "." + introspectedColumn.getActualColumnName();
        method.addAnnotation(this.getGeneratedAnnotation(comment));
    }

    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {
        imports.add(new FullyQualifiedJavaType("javax.annotation.Generated"));
        String comment = "Source Table: " + introspectedTable.getFullyQualifiedTable().toString();
        field.addAnnotation(this.getGeneratedAnnotation(comment));
    }

    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {
        imports.add(new FullyQualifiedJavaType("javax.annotation.Generated"));
        String comment = "Source field: " + introspectedTable.getFullyQualifiedTable().toString() + "." + introspectedColumn.getActualColumnName();
        field.addAnnotation(this.getGeneratedAnnotation(comment));
        if (!this.suppressAllComments && this.addRemarkComments) {
            String remarks = introspectedColumn.getRemarks();
            if (this.addRemarkComments && StringUtility.stringHasValue(remarks)) {
                field.addJavaDocLine("/**");
                field.addJavaDocLine(" * Database Column Remarks:");
                String[] remarkLines = remarks.split(System.getProperty("line.separator"));
                String[] var8 = remarkLines;
                int var9 = remarkLines.length;

                for (int var10 = 0; var10 < var9; ++var10) {
                    String remarkLine = var8[var10];
                    field.addJavaDocLine(" *   " + remarkLine);
                }

                field.addJavaDocLine(" */");
            }
        }
    }

    @Override
    public void addClassAnnotation(InnerClass innerClass, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {
        imports.add(new FullyQualifiedJavaType("javax.annotation.Generated"));
        String comment = "Source Table: " + introspectedTable.getFullyQualifiedTable().toString();
        innerClass.addAnnotation(this.getGeneratedAnnotation(comment));
    }

    private String getGeneratedAnnotation(String comment) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("@Generated(");
        if (this.suppressAllComments) {
            buffer.append('"');
        } else {
            buffer.append("value=\"");
        }

        buffer.append(MyBatisGenerator.class.getName());
        buffer.append('"');
        if (!this.suppressDate && !this.suppressAllComments) {
            buffer.append(", date=\"");
            buffer.append(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now()));
            buffer.append('"');
        }

        if (!this.suppressAllComments) {
            buffer.append(", comments=\"");
            buffer.append(comment);
            buffer.append('"');
        }

        buffer.append(')');
        return buffer.toString();
    }
}