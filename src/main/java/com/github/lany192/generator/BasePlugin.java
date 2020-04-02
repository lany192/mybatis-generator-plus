package com.github.lany192.generator;

import com.github.lany192.generator.utils.JsonUtils;
import com.github.lany192.generator.utils.Log;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 基类
 */
public abstract class BasePlugin extends PluginAdapter {
    protected final String TAG = getClass().getSimpleName();
    private Map<String, Object> params = new HashMap<>();

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            if (StringUtility.stringHasValue(value)) {
                params.put(key, value);
            }
        }
        Log.i(TAG, "所有属性:" + JsonUtils.object2json(params));
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public boolean check(String key) {
        return params.containsKey(key);
    }

    public String getProperty(String key, String defaultValue) {
        String value = (String) params.get(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public boolean getProperty(String key, boolean defaultValue) {
        String value = (String) params.get(key);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        return value.equals("true");
    }
}
